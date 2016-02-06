#--------------------------------------------------------------------------
#
#  ARToolKit5
#  ARToolKit for Android
#
#  This file is part of ARToolKit.
#
#  ARToolKit is free software: you can redistribute it and/or modify
#  it under the terms of the GNU Lesser General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  ARToolKit is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU Lesser General Public License for more details.
#
#  You should have received a copy of the GNU Lesser General Public License
#  along with ARToolKit.  If not, see <http://www.gnu.org/licenses/>.
#
#  As a special exception, the copyright holders of this library give you
#  permission to link this library with independent modules to produce an
#  executable, regardless of the license terms of these independent modules, and to
#  copy and distribute the resulting executable under terms of your choice,
#  provided that you also meet, for each linked independent module, the terms and
#  conditions of the license of that module. An independent module is a module
#  which is neither derived from nor based on this library. If you modify this
#  library, you may extend this exception to your version of the library, but you
#  are not obligated to do so. If you do not wish to do so, delete this exception
#  statement from your version.
#
#  Copyright 2015 Daqri, LLC.
#  Copyright 2011-2015 ARToolworks, Inc.
#
#  Authors: Julian Looser, Philip Lamb
#
#--------------------------------------------------------------------------

MY_LOCAL_PATH := $(call my-dir)

#
# Local variables: MY_CFLAGS, MY_FILES
#

# Make sure DEBUG is defined for debug builds. (NDK already defines NDEBUG for release builds.)
MY_CFLAGS :=
ifeq ($(APP_OPTIM),debug)
    $(info ARToolKit5 Android MK file ($(TARGET_ARCH_ABI) DEBUG))
    MY_CFLAGS += -DDEBUG
else
    $(info ARToolKit5 Android MK file ($(TARGET_ARCH_ABI)))
endif

ARTOOLKIT_ROOT := $(MY_LOCAL_PATH)/../..

#--------------------------------------------------------------------------
# libAR
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)
MY_FILES := $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/AR/*.c)
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
MY_FILES += $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/AR/arLabelingSub/*.c)
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
  # Rather than using LOCAL_ARM_NEON := true, just compile the one file in NEON mode.
  MY_FILES := $(subst arImageProc.c,arImageProc.c.neon,$(MY_FILES))
  LOCAL_CFLAGS += -DHAVE_ARM_NEON=1
endif
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS)
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include
#LOCAL_C_INCLUDES += $(ARTOOLKIT_ROOT)/include/android-$(TARGET_ARCH_ABI)
LOCAL_STATIC_LIBRARIES := aricp
LOCAL_MODULE := ar
include $(BUILD_STATIC_LIBRARY)


#--------------------------------------------------------------------------
# libARICP
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)
MY_FILES := $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/ARICP/*.c)
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS)
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include
LOCAL_STATIC_LIBRARIES := ar
LOCAL_MODULE := aricp
include $(BUILD_STATIC_LIBRARY)


#--------------------------------------------------------------------------
# libARMulti
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)
MY_FILES := $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/ARMulti/*.c)
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS)
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include
LOCAL_MODULE := armulti
include $(BUILD_STATIC_LIBRARY)

#--------------------------------------------------------------------------
# libARgsub_es
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)
MY_FILES := $(ARTOOLKIT_ROOT)/lib/SRC/Gl/gsub_es.c $(ARTOOLKIT_ROOT)/lib/SRC/Gl/glStateCache.c $(ARTOOLKIT_ROOT)/lib/SRC/Gl/gsub_mtx.c
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
  # Rather than using LOCAL_ARM_NEON := true, just compile the one file in NEON mode.
  MY_FILES := $(subst gsub_es.c,gsub_es.c.neon,$(MY_FILES))
  LOCAL_CFLAGS += -DHAVE_ARM_NEON=1
endif
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS)
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include
LOCAL_MODULE := argsub_es
include $(BUILD_STATIC_LIBRARY)


#--------------------------------------------------------------------------
# libARgsub_es2
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)
MY_FILES := $(ARTOOLKIT_ROOT)/lib/SRC/Gl/gsub_es2.c $(ARTOOLKIT_ROOT)/lib/SRC/Gl/glStateCache2.c $(ARTOOLKIT_ROOT)/lib/SRC/Gl/gsub_mtx.c
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS)
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include
LOCAL_MODULE := argsub_es2
include $(BUILD_STATIC_LIBRARY)


#--------------------------------------------------------------------------
# libARosg
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)
MY_FILES := $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/ARosg/*.cpp)
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS) -Wno-extern-c-compat
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include
LOCAL_MODULE := arosg
include $(BUILD_STATIC_LIBRARY)


#--------------------------------------------------------------------------
# libEden
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)
MY_FILES := $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/Eden/*.c $(ARTOOLKIT_ROOT)/lib/SRC/Eden/*.cpp)
MY_FILES += $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/Eden/gluttext/*.c)
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS)
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include
LOCAL_MODULE := eden
include $(BUILD_STATIC_LIBRARY)


#--------------------------------------------------------------------------
# libKPM
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)

MY_FILES := $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/KPM/*.cpp $(ARTOOLKIT_ROOT)/lib/SRC/KPM/*.c)
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/detectors/DoG_scale_invariant_detector.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/detectors/gaussian_scale_space_pyramid.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/detectors/gradients.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/detectors/harris.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/detectors/orientation_assignment.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/detectors/pyramid.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/facade/visual_database_facade.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/framework/date_time.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/framework/image.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/framework/logger.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/framework/timers.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/matchers/freak.cpp
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher/matchers/hough_similarity_voting.cpp
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS) -Wno-extern-c-compat -Wno-null-conversion
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include $(ARTOOLKIT_ROOT)/lib/SRC/KPM/FreakMatcher
LOCAL_MODULE := kpm
include $(BUILD_STATIC_LIBRARY)


#--------------------------------------------------------------------------
# libAR2
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)
MY_FILES := $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/AR2/*.c)
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS)
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include
LOCAL_MODULE := ar2
include $(BUILD_STATIC_LIBRARY)

#--------------------------------------------------------------------------
# libUtil
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)
MY_FILES := $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/Util/*.c)
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS)
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include
LOCAL_MODULE := util
include $(BUILD_STATIC_LIBRARY)

#--------------------------------------------------------------------------
# libARvideo
#--------------------------------------------------------------------------
include $(CLEAR_VARS)
LOCAL_PATH := $(MY_LOCAL_PATH)
CURL_DIR := $(MY_LOCAL_PATH)/curl
MY_FILES := $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/Video/*.c)
MY_FILES += $(ARTOOLKIT_ROOT)/lib/SRC/VideoAndroid/videoAndroid.c $(ARTOOLKIT_ROOT)/lib/SRC/VideoAndroid/cparamSearch.c $(ARTOOLKIT_ROOT)/lib/SRC/VideoAndroid/nxjson.c $(ARTOOLKIT_ROOT)/lib/SRC/VideoAndroid/sqlite3.c
MY_FILES += $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/VideoDummy/*.c)
MY_FILES += $(wildcard $(ARTOOLKIT_ROOT)/lib/SRC/VideoImage/*.c)
MY_FILES := $(MY_FILES:$(LOCAL_PATH)/%=%)
# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := $(MY_FILES)
LOCAL_CFLAGS += $(MY_CFLAGS)
LOCAL_C_INCLUDES := $(ARTOOLKIT_ROOT)/include/android $(ARTOOLKIT_ROOT)/include
LOCAL_C_INCLUDES += $(CURL_DIR)/include
LOCAL_MODULE := arvideo
include $(BUILD_STATIC_LIBRARY)

