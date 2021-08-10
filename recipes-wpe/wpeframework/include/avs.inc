SRC_URI += " \
	git://github.com/WebPlatformForEmbedded/WPEPluginAVS;protocol=git;name=pluginavs;branch=master;destsuffix=git/WPEPluginAVS \
        file://0001-cmake-list.patch \
        file://0002-cmake-list-plugin.patch \
 "

SRCREV_pluginavs = "${AUTOREV}"
SRCREV_FORMAT="thunderNanoServices_pluginavs"

#Add Smart Screen dependency conditionally.
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'alexa_smart_screen', 'alexa-ss-sdk', ' ', d)}"

#------------- Plugin Configuration ---------------#
AVS_AUTOSTART ?= "false"
AVS_PLATFORM  ?= "rpi3"
AVS_DATA_PATH ?= "/usr/share/WPEFramework"
AVS_LOG_LEVEL ?= "DEBUG9"
## "PORTAUDIO" OR "BluetoothRemoteControl"
AVS_AUDIOSOURCE ?= "BluetoothRemoteControl"
AVS_PORTAUDIO_SUPPORT ?= "OFF"
AVS_SMART_SCREEN_SUPPORT ?= "OFF"
AVS_ENABLE_SMART_SCREEN ?= "false"
AVS_KWD_SUPPORT ?= "OFF"
AVS_ENABLE_KWD ?= "false"

#---------------------------------------------------#
PACKAGECONFIG[avs]     = " \
 -DPLUGIN_AVS=ON \
 -DPLUGIN_AVS_AUTOSTART=${AVS_AUTOSTART} \
 -DPLUGIN_AVS_PLATFORM=${AVS_PLATFORM} \
 -DPLUGIN_AVS_DATA_PATH=${AVS_DATA_PATH} \
 -DPLUGIN_AVS_LOG_LEVEL=${AVS_LOG_LEVEL} \
 -DPLUGIN_AVS_AUDIOSOURCE=${AVS_AUDIOSOURCE} \
 -DPLUGIN_AVS_ENABLE_PORTAUDIO_SUPPORT=${AVS_PORTAUDIO_SUPPORT} \
 -DPLUGIN_AVS_ENABLE_SMART_SCREEN_SUPPORT=${AVS_SMART_SCREEN_SUPPORT} \
 -DPLUGIN_AVS_ENABLE_SMART_SCREEN=${AVS_ENABLE_SMART_SCREEN} \
 -DPLUGIN_AVS_ENABLE_KWD_SUPPORT=${AVS_KWD_SUPPORT} \
 -DPLUGIN_AVS_ENABLE_KWD=${AVS_ENABLE_KWD} \
 -DAVSDSDK_INCLUDE_DIRS=${STAGING_INCDIR} \
 -DTHUNDER_INCLUDE_DIRS=${STAGING_INCDIR}/WPEFramework, \
 -DPLUGIN_AVS=OFF,"
