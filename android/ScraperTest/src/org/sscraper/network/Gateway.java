package org.sscraper.network;

import org.sscraper.*;
import org.sscraper.utils.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.nio.channels.SelectionKey.*;

public class Gateway implements Runnable {

    static final String TAG = "gateway-loop";

    private final IHandler handler;

    private final Selector selector;
    private final ServerSocketChannel serverChannel;


    private Thread serverThread;

    // shared, single thread
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 64);
    
    
    public Gateway(String ip, int port, IHandler handler) throws IOException {
        this.handler = handler;        
        
        this.selector = Selector.open();
        this.serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(ip, port));
        serverChannel.register(selector, OP_ACCEPT);        
    }
    
    void accept(SelectionKey key) {
        ServerSocketChannel ch = (ServerSocketChannel) key.channel();
        SocketChannel s;
        try {
            while ((s = ch.accept()) != null) {
                s.configureBlocking(false);
                s.register(selector, OP_READ, null);
            }
        } catch (Exception e) {
            // eg: too many open files. do not quit
            Log.e(TAG, "accept got exception");
            Log.printStackTrace(e);
        }
    }
    
    private void closeKey(final SelectionKey key, int status) {
        try {
            key.channel().close();
        } catch (Exception ignore) {
        }
    }
    
    
    private void doRead(final SelectionKey key) {
        SocketChannel ch = (SocketChannel) key.channel();
        try {
            buffer.clear(); // clear for read
            int read = ch.read(buffer);
            if (read == -1) {
                // remote entity shut the socket down cleanly.
                closeKey(key, -1);
            } else if (read > 0) {
                buffer.flip(); // flip for read
                dumpBuffer(buffer);
            }
        } catch (IOException e) { // the remote forcibly closed the connection
            closeKey(key, -1);
        }
    }
    
    private void doWrite(SelectionKey key) {
        /*
        ServerAtta atta = (ServerAtta) key.attachment();
        SocketChannel ch = (SocketChannel) key.channel();
        try {
            // the sync is per socket (per client). virtually, no contention
            // 1. keep byte data order, 2. ensure visibility
            synchronized (atta) {
                LinkedList<ByteBuffer> toWrites = atta.toWrites;
                int size = toWrites.size();
                if (size == 1) {
                    ch.write(toWrites.get(0));
                    // TODO investigate why needed.
                    // ws request for write, but has no data?
                } else if (size > 0) {
                    ByteBuffer buffers[] = new ByteBuffer[size];
                    toWrites.toArray(buffers);
                    ch.write(buffers, 0, buffers.length);
                }
                Iterator<ByteBuffer> ite = toWrites.iterator();
                while (ite.hasNext()) {
                    if (!ite.next().hasRemaining()) {
                        ite.remove();
                    }
                }
                // all done
                if (toWrites.size() == 0) {
                    if (atta.isKeepAlive()) {
                        key.interestOps(OP_READ);
                    } else {
                        closeKey(key, CLOSE_NORMAL);
                    }
                }
            }
        } catch (IOException e) { // the remote forcibly closed the connection
            closeKey(key, CLOSE_AWAY);
        }
        */
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                if (selector.select() <= 0) {
                    continue;
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                for (SelectionKey key : selectedKeys) {
                    // TODO I do not know if this is needed
                    // if !valid, isAcceptable, isReadable.. will Exception
                    // run hours happily after commented, but not sure.
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        doRead(key);
                    } else if (key.isWritable()) {
                        doWrite(key);
                    }
                }
                selectedKeys.clear();
            } catch (ClosedSelectorException ignore) {
                return; // stopped
                // do not exits the while IO event loop. if exits, then will not process any IO event
                // jvm can catch any exception, including OOM
            } catch (Throwable e) { // catch any exception(including OOM), print it
                Log.d(TAG, "http server loop error, should not happen");
                Log.printStackTrace(e);
            }
        }     
    }

    public void start() throws IOException {
        serverThread = new Thread(this, TAG);
        serverThread.start();
    }

    public void stop(int timeout) {
        try {
            serverChannel.close(); // stop accept any request
        } catch (IOException ignore) {
        }

        // wait all requests to finish, at most timeout milliseconds
        //handler.close(timeout);

        // close socket, notify on-close handlers
        if (selector.isOpen()) {
            Set<SelectionKey> t = selector.keys();
            SelectionKey[] keys = t.toArray(new SelectionKey[t.size()]);
            for (SelectionKey k : keys) {
                /**
                 * 1. t.toArray will fill null if given array is larger.
                 * 2. compute t.size(), then try to fill the array, if in the mean time, another
                 *    thread close one SelectionKey, will result a NPE
                 *
                 * https://github.com/http-kit/http-kit/issues/125
                 */
                if (k != null)
                    closeKey(k, 0); // 0 => close by server
            }

            try {
                selector.close();
            } catch (IOException ignore) {
            }
        }
    }

    public int getPort() {
        return this.serverChannel.socket().getLocalPort();
    }
    
    private void dumpBuffer(ByteBuffer buffer) {
        CharBuffer charBuffer = null;
        try {
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer);
            buffer.flip();
            Log.d(TAG, charBuffer.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
}
