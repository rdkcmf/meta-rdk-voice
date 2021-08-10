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

#1.15
SRCREV_alexa-device-sdk = "2e9428e9dfc9fa7c3b4bfd5106323f8c2f1643c9"
SRCREV ="${AUTOREV}"

#To support AVS plugin
SRC_URI += "file://0001-prevent_crashes_on_rpi.patch;striplevel=1"
SRC_URI += "file://0002-Synchronise_rapidjson_version_with_smart_screen.patch;striplevel=1"
SRC_URI += "file://0003-Build-the-SampleApplication-as-library.patch;striplevel=2"

LDFLAGS += "-pthread"
INSANE_SKIP_${PN} = "dev-so"
TARGET_CC_ARCH += "${LDFLAGS}"

S = "${WORKDIR}/git"
SB = "${WORKDIR}/build"

inherit pkgconfig cmake systemd

EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=RELEASE \
                 -DGSTREAMER_MEDIA_PLAYER=ON \
                 -DPORTAUDIO=ON \
                 -DPORTAUDIO_LIB_PATH=${STAGING_LIBDIR}/libportaudio.so \
                 -DPORTAUDIO_INCLUDE_DIR=${STAGING_INCDIR} \
"

RDEPENDS_${PN} += "bash perl libwebsockets"
DEPENDS = "curl nghttp2 sqlite3 gstreamer1.0-plugins-base cjson"
DEPENDS += " rapidjson portaudio-v19"

#Add KWD provider library conditionally.
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'alexa_ffv', 'virtual/alexa-kwd-detector', ' ', d)}"

do_install() {
    install -d ${D}${includedir}/AVS

    cd ${SB}
#copying the .so to the /usr/lib
    install -d ${D}${libdir}
    install -d ${D}${libdir}/pkgconfig
    find -iname '*.so' -exec cp {} ${D}${libdir} \;
    find -iname '*.pc' -exec cp {} ${D}${libdir}/pkgconfig \;

#copy headers
    for dirList in `find ${S} -name include`
    do
       cp -af $dirList/* ${D}${includedir}/AVS/
    done
#remove local rapidjson headers
#    rm -rf ${D}${includedir}/rapidjson/*
}

BBCLASSEXTEND = "native"
FILES_${PN} += "${bindir} ${libdir}  ${includedir}" 
FILES_${PN}-dev = "${includedir}"
ASNEEDED=""

CXXFLAGS_append_dunfell = " -Wno-redundant-move -Wno-deprecated-copy "
