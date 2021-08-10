FILESEXTRAPATHS_prepend := "${THISDIR}/wpeframework-plugins:"
include include/avs.inc

DEPENDS += "skillmapper"
RDEPENDS_${PN} += "skillmapper"
SRC_URI  += "file://0003-skillmapper-integration.patch"
SRC_URI  += "file://0004-template-card-support.patch"
SRC_URI  += "file://0005-avsplugin-crash-fix.patch"
SRC_URI  += "file://0006-smart-screen-notification-and-audio-player.patch"
SRC_URI  += "file://0007-smartscreen-crash-fix.patch"

PACKAGECONFIG_append = " avs"

do_configure_prepend() {
    ln -s ${S}/WPEPluginAVS/Impl/AVSDevice/ThunderInputManager.cpp ${S}/WPEPluginAVS/Impl/
    ln -s ${S}/WPEPluginAVS/Impl/AVSDevice/ThunderInputManager.h ${S}/WPEPluginAVS/Impl/
}

do_install_append() {
 install -d ${D}/root
 install -d ${D}/root/AVS
 install -d ${D}/root/AVS/db
}
FILES_${PN} += " root/AVS/db"

ASNEEDED=""
