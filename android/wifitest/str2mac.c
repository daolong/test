#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define DERROR printf
#define DEBUG printf

static int
str2mac(const char *macStr, unsigned char *mac)
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
          DEBUG("i:%s\n", s);
          mac[j++] = (unsigned char)strtoul(s, 0, 16);
          s = str_o + i + 1;
      }
    }
    if ((s != str_o) && (s - str_o < len)) {
      DEBUG("o:%s\n", s);
      mac[j++] = (unsigned char)strtoul(s, 0, 16);
    }
	
	return j;
}

int main(int argc, char **argv) {
	unsigned char mac[6];
	memset((void *)&mac, 0, 6);
	int ret = str2mac((const char *)argv[1], (unsigned char *)&mac);
	int i = 0;
	for (i = 0; i < ret; i++) {
		DEBUG("mac[%d] : %d - %x \n", i, mac[i], mac[i]);
	}
}