SUMMARY = "Smart Screen SDK"
DESCRIPTION = "Alexa Smart Screen SDK" 
LICENSE = "Apache-2.0 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=8af6ce427017dadf03f47608d86f3716" 

FILESEXTRAPATHS_prepend:= "${THISDIR}/files:"
PROVIDES = "alexa-ss-sdk"
RPROVIDES_${PN} = "alexa-ss-sdk"

FILES_${PN} = "${libdir}"
FILES_${PN}-dev = "${includedir}"

OECMAKE_GENERATOR = "Unix Makefiles"

SRC_URI = "git://github.com/alexa/alexa-smart-screen-sdk.git;branch=master;protocol=https;name=alexa-ss-sdk"
SRC_URI += "https://nodejs.org/dist/v12.16.3/node-v12.16.3-linux-x64.tar.xz;name=nodejs;subdir=nodejs"
SRC_URI[nodejs.md5sum] = "1cc2ea88646695f42a2016a5cb29bf2a"

SRC_URI += "file://0001-Remove_additional_linking.patch;striplevel=1"
SRC_URI += "file://0002-Allow_disabling_GUIClient_and_set_install_path.patch;striplevel=1"
SRC_URI += "file://0003-Add_install_for_version_file.patch;striplevel=1"
SRC_URI += "file://0004-Build-SampleApp-as-library.patch;striplevel=2"
SRC_URI += "file://0005-node-module-fix.patch"
SRC_URI += "file://0007-modify-ui-attributes.patch"


include ${@bb.utils.contains('DISTRO_FEATURES', 'voice-chrome', "avs-voice-chrome.inc", "avs-voice-chrome-stub.inc", d)}

#SDK 2.0.1
SRCREV_alexa-ss-sdk ="ddead91198e59b1f3fac8e362799e9a4349b25bf"

LDFLAGS += "-pthread"

S = "${WORKDIR}/git"
SB = "${WORKDIR}/build"

do_compile_prepend() {
    export PATH=${WORKDIR}/nodejs/node-v12.16.3-linux-x64/bin:${PATH}
}

do_install_prepend() {
    export PATH=${WORKDIR}/nodejs/node-v12.16.3-linux-x64/bin:${PATH}
}

INSANE_SKIP_${PN} = "dev-so"
TARGET_CC_ARCH += "${LDFLAGS}"

AVS_DIR ?= "/home/root/Alexa_SDK"

RDEPENDS_${PN} += "bash perl libwebsockets"
DEPENDS = "websocketpp asio apl-core"
DEPENDS += "curl nghttp2 sqlite3 gstreamer1.0-plugins-base cjson"

DEPENDS += "virtual/alexa-device-sdk"

inherit pkgconfig cmake systemd

EXTRA_OECMAKE = " \
-DASDK_LIBRARY_DIRS= ${STAGING_LIB_DIR} \
-DWEBSOCKETPP_INCLUDE_DIR=${STAGING_INCDIR} \
-DDISABLE_WEBSOCKET_SSL=ON \
-DGSTREAMER_MEDIA_PLAYER=ON \
-DCMAKE_BUILD_TYPE=DEBUG \
-DBUILD_TESTING=0 \
-DPORTAUDIO=ON \
-DPORTAUDIO_LIB_PATH=${STAGING_LIBDIR}/libportaudio.so \
-DPORTAUDIO_INCLUDE_DIR=${STAGING_INCDIR} \
-DAPL_CORE=ON \
-DAPLCORE_INCLUDE_DIR=${STAGING_INCDIR} \
-DAPLCORE_LIB_DIR=${STAGING_LIBDIR} \
-DAPLCORE_RAPIDJSON_INCLUDE_DIR=${STAGING_INCDIR} \
-DYOGA_INCLUDE_DIR=${STAGING_INCDIR} \
-DYOGA_LIB_DIR=${STAGING_LIBDIR} \
-DJS_GUICLIENT_ENABLE=ON \
-DJS_GUICLIENT_INSTALL_PATH=${D}/${AVS_DIR}/alexa-smart-screen \
"

do_install_append() {
#Stop the local copy of rapidjson being installed to sysroot
    rm -rf ${D}${includedir}/rapidjson
}

BBCLASSEXTEND = "native"

FILES_${PN} += "${AVS_DIR}/*   /sounds /database ${bindir} ${libdir}  ${includedir}" 
FILES_${PN} += "${sysconfdir}/* "
ASNEEDED=""

