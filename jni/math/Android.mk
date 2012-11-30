LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := td_math
### Add all source file names to be included in lib separated by a whitespace
##LV_SRC :=$(wildcard *.c)
##LOCAL_SRC_FILES := $(LV_SRC)
LOCAL_SRC_FILES := td_add.c td_sub.c td_mul.c
LOCAL_CFLAGS := -std=c99
#include $(BUILD_EXECUTABLE)
include $(BUILD_STATIC_LIBRARY)
