###############################################################
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := seraphim
### Add all source file names to be included in lib separated by a whitespace
LOCAL_SRC_FILES := seraphim.c

#LOCAL_CFLAGS += -I$(LOCAL_PATH)/../math/include -std=c99
#LOCAL_STATIC_LIBRARIES := td_math  td_x264
#include $(BUILD_EXECUTABLE)
