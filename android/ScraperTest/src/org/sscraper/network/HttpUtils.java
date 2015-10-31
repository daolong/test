/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.network;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.sscraper.utils.Log;


public class HttpUtils {
    private final static String TAG = "HttpUtils";
    
    // Connect Timeout
    public final static int CONNECT_TIMEOUT = 10 * 1000;
    // Read Timeout
    public final static int READ_TIMEOUT = 10 * 1000;
    
    private static final String USER_AGENT = "Mozilla/5.0";
    
    @SuppressWarnings("deprecation")
    public static String httpGet(String urlString, int connectTimeout,
            int readTimeout) {
        
        Log.d(TAG, "HttpGet url : " + urlString);
        try {
            DefaultHttpClient client = new DefaultHttpClient();            
            
            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                    readTimeout);
            
            HttpGet httpRequest = new HttpGet(urlString);
            HttpResponse response = client.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                String rev = EntityUtils.toString(entity);
                return rev;
            }
        } catch (Exception e1) {
            Log.printStackTrace(e1);
        }
        
        return null;
    }

    public static String httpGet(String urlString) {
        return httpGet(urlString, CONNECT_TIMEOUT, READ_TIMEOUT);
    }
    
    @SuppressWarnings("deprecation")
    public static String httpPost(String urlString,
            List<NameValuePair> params, int connectTimeout, int readTimeout) {
        
        Log.d(TAG, "HttpPost url : " + urlString);
        try {
            HttpPost httpRequest = new HttpPost(urlString);
            // 发出HTTP request
            if (params != null) {
                httpRequest
                        .setEntity((HttpEntity) new UrlEncodedFormEntity(params,
                                HTTP.UTF_8));
            }
            
            // 取得HTTP response
            DefaultHttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                    readTimeout);
            HttpResponse response = client.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                String rev = EntityUtils.toString(entity);
                return rev;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            urlString = null;
        }
        return urlString;
    }
    
    
    public static String httpPost(String urlString, List<NameValuePair> params) {
        return httpPost(urlString, params, CONNECT_TIMEOUT, READ_TIMEOUT);
    }
    
    public static String decodeHttpParam(String param, String charset) {
        try {
            String newParam = URLDecoder.decode(param, charset);
            return newParam;
        } catch (UnsupportedEncodingException e) {
            return param;
        }
    }
}
