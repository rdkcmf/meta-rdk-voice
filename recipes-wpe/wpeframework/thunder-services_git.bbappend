FILESEXTRAPATHS_prepend := "${THISDIR}/wpeframework-plugins:"
include include/avs.inc

DEPENDS += "skillmapper"
RDEPENDS_${PN} += "skillmapper"

SRC_URI += "file://0001-alexa-device-sdk-upgrade-1.23.patch"
SRC_URI += "file://0002-smartscreen-upgrade-and-crash-fix-2.6.patch"
SRC_URI += "file://0003-smart-screen-notification-and-audio-player-2.6.patch"
SRC_URI += "file://0004-ss-notification-fix-2.6.patch"
SRC_URI += "file://0008-Custom-skill-invocation-fix.patch"
SRC_URI += "file://AlexaClientSDKConfig-1.23.json"
SRC_URI += "file://SmartScreenSDKConfig-2.6.json"

PACKAGECONFIG_append = " avs"

do_configure_prepend() {
    ln -sf ${S}/WPEPluginAVS/Impl/AVSDevice/ThunderInputManager.cpp ${S}/WPEPluginAVS/Impl/
    ln -sf ${S}/WPEPluginAVS/Impl/AVSDevice/ThunderInputManager.h ${S}/WPEPluginAVS/Impl/
}

do_install_append() {
 install -d ${D}/root
 install -d ${D}/root/AVS
 install -d ${D}/root/AVS/db
 cp -afv --no-preserve=ownership ${WORKDIR}/AlexaClientSDKConfig-1.23.json ${D}/usr/share/WPEFramework/AVS/AlexaClientSDKConfig.json
 cp -afv --no-preserve=ownership ${WORKDIR}/SmartScreenSDKConfig-2.6.json ${D}/usr/share/WPEFramework/AVS/SmartScreenSDKConfig.json
}
FILES_${PN} += " root/AVS/db"

ASNEEDED=""
