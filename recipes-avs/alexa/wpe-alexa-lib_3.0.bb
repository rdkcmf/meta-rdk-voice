SUMMARY = "AVS SDK"
DESCRIPTION = "Amazon Voice SDK Integration for WPE Framework AVS Plugin" 
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d92e60ee98664c54f68aa515a6169708" 

FILESEXTRAPATHS_prepend:= "${THISDIR}/files:"
PROVIDES = "virtual/alexa-device-sdk"
RPROVIDES_${PN} = "virtual/alexa-device-sdk"

OECMAKE_GENERATOR = "Unix Makefiles"

SRC_URI = "git://github.com/alexa/avs-device-sdk.git;branch=master;protocol=https;name=alexa-device-sdk;destsuffix=git"
SRC_URI  += "file://0001-gst-adjust-rank.patch"
#SRC_URI += "file://0003-bluetooth-src-symbol-fix-1.23.patch"
SRC_URI += "file://0002-sample-app-fix-3.0.patch"
SRC_URI += "file://0003-Build-the-SampleApplication-as-library-3.0.patch"
SRC_URI += "file://0004-sdk-header-installation-fix.patch"

#3.0
SRCREV_alexa-device-sdk = "b32be0a1139be1aa22f1df5f59ba394d3b6a9697"
SRCREV ="${AUTOREV}"


LDFLAGS += "-pthread"
INSANE_SKIP_${PN} = "dev-so"
INSANE_SKIP_${PN} += "dev-deps"
TARGET_CC_ARCH += "${LDFLAGS}"

S = "${WORKDIR}/git"
SB = "${WORKDIR}/build"

inherit pkgconfig cmake systemd

EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=DEBUG \
                 -DGSTREAMER_MEDIA_PLAYER=ON \
                 -DPORTAUDIO=ON \
                 -DRAPIDJSON_MEM_OPTIMIZATION=OFF \
                 -DPORTAUDIO_LIB_PATH=${STAGING_LIBDIR}/libportaudio.so \
                 -DPORTAUDIO_INCLUDE_DIR=${STAGING_INCDIR} \
                 -DPKCS11=OFF \
                 -DINSTALL_COMMON_SAMPLE_LIBS=ON \
                 -DENABLE_ALL_VIDEO_CONTROLLERS=OFF \
                 -DVIDEO_CONTROLLERS_ALEXA_LAUNCHER=ON \
                 -DVIDEO_CONTROLLERS_ALEXA_KEYPAD_CONTROLLER=ON \
                 -DVIDEO_CONTROLLERS_ALEXA_PLAYBACK_CONTROLLER=ON \
                 -DVIDEO_CONTROLLERS_ALEXA_PLAYBACK_STATE_REPORTER=ON \
                 -DVIDEO_CONTROLLERS_ALEXA_REMOTE_VIDEO_PLAYER=ON \
                 -DASDK_LIB_INSTALL_DIR=${D}${libdir} \
                 -DASDK_INCLUDE_INSTALL_DIR=${D}${includedir}/AVS \
"

RDEPENDS_${PN} += "bash perl"
DEPENDS = "curl nghttp2 sqlite3 gstreamer1.0-plugins-base cjson"
DEPENDS += " portaudio-v19"


#Add KWD provider library conditionally.
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'alexa_ffv', 'virtual/alexa-kwd-detector', ' ', d)}"

do_install() {
    install -d ${D}${includedir}/AVS
    install -d ${D}${libdir}    
    install -d ${D}${libdir}/pkgconfig
    cd ${SB}
    make install
#remove local rapidjson headers
#    rm -rf ${D}${includedir}/rapidjson/*
}


BBCLASSEXTEND = "native"
FILES_${PN} += "${bindir} ${libdir}  ${includedir}" 
FILES_${PN}-dev = "${includedir}"
ASNEEDED=""

CXXFLAGS_append_dunfell = " -Wno-redundant-move -Wno-deprecated-copy "
