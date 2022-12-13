SUMMARY = "xr-speech-avs library for control manager"
LICENSE = "Apache-2.0"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"
include include/xr_speech_avs.inc

S = "${WORKDIR}/git/xr-speech-avs"

DEPENDS += "wpeframework-interfaces asio rdkx-logger xr-speech-router sqlite3 libevdev"

XLOG_MODULE_NAME="VSDK"

DEPENDS += "skillmapper"
RDEPENDS_${PN} += "skillmapper"

LDFLAGS_append = " -lVoiceToApps -lsqlite3"

PACKAGECONFIG_append = " avs"

inherit pkgconfig cmake rdkx-logger


do_install_append() {
 install -d ${D}/root
 install -d ${D}/root/AVS
 install -d ${D}/root/AVS/db
 install -d ${D}${includedir}
 install -m 644 ${S}/AVS.h ${D}${includedir}
 install -m 644 ${S}/avs_sdt/avs_sdt.h ${D}${includedir}
 # Install the reference app supported configuration.
 install -d -m 0755 ${D}/etc/WPEFramework/plugins
 install -m 0644 ${S}/SmartScreen.json ${D}/etc/WPEFramework/plugins/
}
FILES_${PN} += " root/AVS/db"
FILES_${PN} += " ${libdir}/*.so"
FILES_${PN} += " usr/share/WPEFramework/AVS"
FILES_${PN} += " etc/WPEFramework/plugins"
FILES_${PN}-dev = " ${includedir}"

ASNEEDED=""

INSANE_SKIP_${PN} += "libdir staticdev dev-so"
INSANE_SKIP_${PN}-dbg += "libdir"

