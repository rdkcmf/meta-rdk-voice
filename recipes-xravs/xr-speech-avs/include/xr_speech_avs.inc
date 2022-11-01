SRCREV = "${AUTOREV}"
PV = "1.0+git${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/avs/xr-speech-avs-sdk;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"

#Add Smart Screen dependency conditionally.
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'alexa_smart_screen', 'alexa-ss-sdk', ' ', d)}"
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'alexa-sdk-3.0', 'alexa-web-sdk apl-core websocketpp alexa-sampleapp-lib', ' ', d)}"
RDEPENDS_${PN} += " ${@bb.utils.contains('DISTRO_FEATURES', 'alexa-sdk-3.0', 'libwebsockets alexa-sampleapp-lib', ' ', d)}"

#------------- Configuration ---------------#
XR_AVS_PLATFORM  ?= "rpi3"
XR_AVS_DATA_PATH ?= "/usr/share/WPEFramework"
XR_AVS_LOG_LEVEL ?= "DEBUG9"
XR_AVS_SMART_SCREEN_SUPPORT ?= "ON"

#---------------------------------------------------#
PACKAGECONFIG[avs]     = " \
 -DAVS_PLATFORM=${XR_AVS_PLATFORM} \
 -DAVS_DATA_PATH=${XR_AVS_DATA_PATH} \
 -DAVS_LOG_LEVEL=${XR_AVS_LOG_LEVEL} \
 -DAVS_ENABLE_SMART_SCREEN_SUPPORT=${XR_AVS_SMART_SCREEN_SUPPORT} \
 -DAVSDSDK_INCLUDE_DIRS=${STAGING_INCDIR}/AVS \
 -DTHUNDER_INCLUDE_DIRS=${STAGING_INCDIR}/WPEFramework,"
