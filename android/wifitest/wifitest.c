/**  
 *  Copyright (C) 2017 daolong.li@gmail.com
 */
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <sys/ioctl.h>
#include <net/if.h>
#include <net/if_arp.h>
#include <errno.h>
#include <sys/stat.h>
#include <fcntl.h>

#ifndef MAC2STR
#define MAC2STR(a) (a)[0], (a)[1], (a)[2], (a)[3], (a)[4], (a)[5]
#define MACSTR "%02x:%02x:%02x:%02x:%02x:%02x"

/*
 * Compact form for string representation of MAC address
 * To be used, e.g., for constructing dbus paths for P2P Devices
 */
#define COMPACT_MACSTR "%02x%02x%02x%02x%02x%02x"
#endif

#define DERROR printf
#define DEBUG printf

static int get_mac_addr(const char *ifname, char *mac)
{
    int fd;
    struct ifreq ifr;
    
    if (!ifname || !mac) {
        return -1;
    }
	
    fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (fd < 0) {
        DERROR("open socket failed errno(%d), %s\n", errno, strerror(errno));
        return -1;
    }
    ifr.ifr_addr.sa_family = AF_INET;    
    strncpy(ifr.ifr_name, (const char *)ifname, IFNAMSIZ);

    if (ioctl(fd, SIOCGIFHWADDR, &ifr)) {
		DERROR("Could not get interface %s hwaddr: %s\n", ifname, strerror(errno));
		close(fd);
		return -1;
	}
    memcpy(mac, (unsigned char *)ifr.ifr_hwaddr.sa_data, ETH_ALEN);
    close(fd);
    return 0;
}

static int str2mac(const char *macStr, unsigned char *mac)
{
	if (macStr == NULL || mac == NULL)
		return 0;
	
	int len = (int)strlen(macStr);
	int i = 0, j = 0;
	char *str_o = (char *)macStr;
    char *str = str_o;
    char *s = str_o;
	char end;
    for (i = 0; i < len; i++) {
      if (str[i] == ':') {
          str[i] = '\0';
          //DEBUG("i:%s\n", s);
          mac[j++] = (unsigned char)strtoul(s, 0, 16);
          s = str_o + i + 1;
      }
    }
    if ((s != str_o) && (s - str_o < len)) {
      //DEBUG("o:%s\n", s);
      mac[j++] = (unsigned char)strtoul(s, 0, 16);
    }
	
	return j;
}

static int set_mac_addr(const char *ifname, const unsigned char *mac)
{
    int fd;
    struct ifreq ifr;

    if (!ifname || !mac ) {
        return -1;
    }
	
    fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (fd < 0) {
        DERROR("open socket failed errno(%d), %s\n", errno, strerror(errno));
        return -1;
    }
    ifr.ifr_addr.sa_family = ARPHRD_ETHER;
    strncpy(ifr.ifr_name, (const char *)ifname, IFNAMSIZ);
    memcpy((unsigned char *)ifr.ifr_hwaddr.sa_data, mac, ETH_ALEN);
    
    if (ioctl(fd, SIOCSIFHWADDR, &ifr)) {
        DERROR("Could not set interface %s hwaddr: %s\n", ifname, strerror(errno));
		close(fd);
		return -1;
    }
    close(fd);
    return 0;
}

static int if_updown(const char *ifname, int dev_up)
{
    int fd;
    struct ifreq ifr;        

    if (!ifname) {
        return -1;
    }

    fd = socket(AF_INET, SOCK_DGRAM, 0 );
    if (fd < 0) {
        DERROR("open %s failed : %s\n", ifname, strerror(errno));
        return -1;
    }
    
    ifr.ifr_addr.sa_family = AF_INET;
    strncpy(ifr.ifr_name, (const char *)ifname, IFNAMSIZ);

	if (ioctl(fd, SIOCGIFFLAGS, &ifr) == 0) {
		if (dev_up) {
			if (ifr.ifr_flags & IFF_UP) {
				DEBUG("%s already up\n", ifname);
				close(fd);
				return -1;
			}
			ifr.ifr_flags |= IFF_UP;
		} else {
			if (!(ifr.ifr_flags & IFF_UP)) {
				DEBUG("%s already down\n", ifname);
				close(fd);
				return -1;
			}
			ifr.ifr_flags &= ~IFF_UP;
		}
		if (ioctl(fd, SIOCSIFFLAGS, &ifr)) {
			DERROR("Could not set interface %s flags (%s): error %s \n", ifname, dev_up ? "UP" : "DOWN", strerror(errno));
			close(fd);
			return -1;
		}
	} else {
		DERROR("Could not read interface %s flags: %s\n", ifname, strerror(errno));
		close(fd);
		return -1;
	}
    close(fd);
    return 0;
}


static int linux_get_ifhwaddr(const char *ifname, char *addr) {
	struct ifreq ifr;
	int sock = socket(PF_INET, SOCK_DGRAM, 0);
	if (sock < 0) {
		DERROR("open %s failed : %s\n", ifname, strerror(errno));
		return -1;
	}
	
	memset(&ifr, 0, sizeof(ifr));
	strncpy(ifr.ifr_name, ifname, IFNAMSIZ);
	if (ioctl(sock, SIOCGIFHWADDR, &ifr)) {
		DERROR("Could not get interface %s hwaddr: %s\n", ifname, strerror(errno));
		close(sock);
		return -1;
	}
	if (ifr.ifr_hwaddr.sa_family != ARPHRD_ETHER) {
		DERROR("%s: Invalid HW-addr family 0x%04x\n", ifname, ifr.ifr_hwaddr.sa_family);
		close(sock);
		return -1;
	}
	memcpy(addr, ifr.ifr_hwaddr.sa_data, ETH_ALEN);
	close(sock);
	return 0;
}

#define WIFI_ADDR "/sys/class/net/wlan0/address"

static void toUpperCase(char *string) {
	int i = 0, len = 0;
	if (string == NULL) return;
	
	len = strlen(string);
	for (i = 0; i < len; i++) {
		if (string[i] >= 'a' && string[i] <= 'z') {
			string[i] = 'A' + (string[i] - 'a');
		}
	}
}

static void test() {
	int fd = open(WIFI_ADDR, O_RDONLY);
	char data[18]; //9c:44:3d:e0:1b:ab	
	if (fd < 0) {
		DERROR("Could not open %s : %s\n", WIFI_ADDR, strerror(errno));
		return;
	}
	int ret = read(fd, data, 17);
	if (ret != 17) {
		DERROR("Read %s failed : %d\n", WIFI_ADDR, ret);
	}
	data[17] = '\0';
	toUpperCase((char *)&data);
	DEBUG("mac = %s \n", data);
}

static void printUsage(const char *self) {
	DEBUG("Usge: %s [get/get2/set/up/down] [ifname] [addr]\n", self);
}

int main(int argc, char **argv)
{
	int ret = 0;
	const char *self = argv[0];
	if (argc < 2) {
		test();
		printUsage(self);
		exit(0);
	}

	const char *cmd = argv[1];
	if (strncmp(cmd, "get", 3) == 0) {
		if (argc < 3) {
			printUsage(self);
			exit(0);
		}
		const char *ifname = argv[2];
		char addr[ETH_ALEN + 1];
		memset((void *)&addr, '\0' , ETH_ALEN + 1);
		ret = linux_get_ifhwaddr(ifname, (char *)&addr);
		if (ret == 0) {
			char mac[19]; // 9C:44:3D:E0:1B:AB
			memset((void *)&mac, '\0' , 19);
			snprintf((char *)&mac, 18, MACSTR, MAC2STR(addr));
			toUpperCase((char *)&mac);
			DEBUG("mac addr of %s : %s \n", ifname, mac);
		}
	} else if (strncmp(cmd, "get2", 4) == 0) {
		if (argc < 3) {
			printUsage(self);
			exit(0);
		}
		const char *ifname = argv[2];
		char addr[ETH_ALEN + 1];
		memset((void *)&addr, '\0' , ETH_ALEN + 1);
		ret = get_mac_addr(ifname, (char *)addr);
		if (ret == 0) {
			char mac[19];
			memset((void *)&mac, '\0' , 19);
			snprintf((char *)&mac, 18, MACSTR, MAC2STR(addr));
			toUpperCase((char *)&mac);
			DEBUG("mac addr of %s : %s \n", ifname, mac);
		}
	} else if (strncmp(cmd, "set", 3) == 0) {
		if (argc < 4) {
			printUsage(self);
			exit(0);			
		}
		const char *ifname = argv[2];
		const char *addr = argv[3];
		unsigned char mac[6];
		memset((void *)&mac, 0, 6);
		ret = str2mac(addr, (unsigned char *)&mac);
		if (ret == 6) {
			ret = set_mac_addr(ifname, (unsigned char*)&mac);
			if (ret == 0) {
				DEBUG("set mac address of (%s) success!\n", ifname);
			}
		} else {
			DERROR("set mac error format %s\n", addr);
		}
	} else if (strncmp(cmd, "up", 2) == 0) {
		if (argc < 3) {
			printUsage(self);
			exit(0);
		}
		const char *ifname = argv[2];
		ret = if_updown(ifname, 1);
		if (ret == 0) {
			DEBUG("turn on %s success!\n", ifname);
		}
	} else if (strncmp(cmd, "down", 4) == 0) {
		if (argc < 3) {
			printUsage(self);
			exit(0);
		}
		const char *ifname = argv[2];
		ret = if_updown(ifname, 0);
		if (ret == 0) {
			DEBUG("turn off %s success!\n", ifname);
		}
	} else {
		printUsage(self);
	}
	return 0;
}