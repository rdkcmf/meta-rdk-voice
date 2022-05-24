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

SRC_URI += "file://0002-Allow_disabling_GUIClient_and_set_install_path-2.6.patch;striplevel=1"
SRC_URI += "file://0003-Add_install_for_version_file-2.6.patch;striplevel=1"
SRC_URI += "file://0004-Build-SampleApp-as-library-2.6.patch;striplevel=1"
SRC_URI += "file://0007-modify-ui-attributes-2.6.patch"

include ${@bb.utils.contains('DISTRO_FEATURES', 'voice-chrome', "avs-voice-chrome.inc", "avs-voice-chrome-stub.inc", d)}

#SDK 2.6
SRCREV_alexa-ss-sdk ="03354f93c5494d847cd13cea93b3caccfe6603f4"

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
INSANE_SKIP_${PN} += "dev-deps"
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
-DAPLCORE_BUILD_INCLUDE_DIR=${STAGING_INCDIR} \
-DAPLCORE_LIB_DIR=${STAGING_LIBDIR} \
-DAPLCORE_RAPIDJSON_INCLUDE_DIR=${STAGING_INCDIR}/AVS \
-DYOGA_INCLUDE_DIR=${STAGING_INCDIR} \
-DYOGA_LIB_DIR=${STAGING_LIBDIR} \
-DJS_GUICLIENT_ENABLE=ON \
-DJS_GUICLIENT_INSTALL_PATH=${D}/${AVS_DIR}/alexa-smart-screen \
"

do_install_append() {
#Fix - few libraries not not copied by default build script
    find ${WORKDIR}/build -iname 'libAPLClient.so' -exec cp {} ${D}${libdir} \;
#Stop the local copy of rapidjson being installed to sysroot
    rm -rf ${D}${includedir}/rapidjson
    cp -av --no-preserve=ownership ${S}/modules/Alexa/APLClientLibrary/APLClient/include/* ${D}${includedir}
    install -d ${D}${includedir}/SmartScreen
    cd ${SB}
#copy headers
    for dirList in `find ${S} -name include`
    do
        cp -af $dirList/* ${D}${includedir}/SmartScreen/
    done
#Copy SampleApplication header file from SampleApp to SmartScreen Dir
    cp -f ${D}${includedir}/SampleApp/SampleApplication.h ${D}${includedir}/SmartScreen/SampleApp/
}


BBCLASSEXTEND = "native"
FILES_${PN} += "${AVS_DIR}/alexa-smart-screen /sounds /database ${bindir} ${libdir}  ${includedir}"
FILES_${PN} += "${sysconfdir}/* "
ASNEEDED=""

