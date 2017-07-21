LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_C_INCLUDES += \
  $(LOCAL_PATH)


LOCAL_SRC_FILES := \
  wifitest.c

LOCAL_SHARED_LIBRARIES := \
    libandroid_runtime \
    libnativehelper \
    libutils \
    libbinder \
    liblog \
    libcutils
    
#LOCAL_REQUIRED_MODULES := \


LOCAL_CFLAGS := -g -O2 -Wall -Werror -MD -MP

LOCAL_MODULE_TAGS := optional

LOCAL_MODULE := wifitest.x

LOCAL_STRIP_MODULE := false

include $(BUILD_EXECUTABLE)
