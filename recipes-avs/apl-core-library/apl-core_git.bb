SUMMARY = "APL Core Library"
DESCRIPTION = "Recipe to build APL Core" 
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM="file://LICENSE.txt;md5=d92e60ee98664c54f68aa515a6169708"

FILESEXTRAPATHS_prepend:= "${THISDIR}/files:"
PROVIDES = "apl-core"
RPROVIDES_${PN} = "apl-core"

FILES_${PN} = "${libdir}"
FILES_${PN}-dev = "${includedir}"

OECMAKE_GENERATOR = "Unix Makefiles"

BASE_URI ?= "git://github.com/alexa/apl-core-library.git;branch=master;protocol=https;name=apl-core-lib"
SRC_URI   = "${BASE_URI}"

SRCREV_apl-core-lib ="${@bb.utils.contains('DISTRO_FEATURES', 'alexa-sdk-3.0', '5c43a0be0c2557955d2400707dd03ba544787ffd', 'd80bb154bec280b88640c9c917283277ce0394e2', d)}"

LDFLAGS += "-pthread"

INSANE_SKIP_${PN} = "dev-so"
TARGET_CC_ARCH += "${LDFLAGS}"

S = "${WORKDIR}/git"

inherit pkgconfig cmake

RDEPENDS_${PN} += "bash perl libwebsockets"
DEPENDS = "curl nghttp2 sqlite3 gstreamer1.0-plugins-base cjson"

EXTRA_OECMAKE = "-DCMAKE_POSITION_INDEPENDENT_CODE=ON -DENABLE_PIC=ON \
"

do_install_append() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -d ${D}${includedir}/yoga
    install -d ${D}${libdir}/pkgconfig

    cp -av --no-preserve=ownership ${WORKDIR}/build/aplcore/libapl.a ${D}${libdir}
    cp -av --no-preserve=ownership ${WORKDIR}/build/lib/libyogacore.a ${D}${libdir}

    cp -av --no-preserve=ownership ${WORKDIR}/build/yoga-prefix/src/yoga/yoga/*.h ${D}${includedir}/yoga/
    cp -av --no-preserve=ownership ${S}/aplcore/include/* ${D}${includedir}

    install -d ${D}${libdir}/pkgconfig
    find ${WORKDIR}/build -iname 'apl.pc' -exec cp {} ${D}${libdir}/pkgconfig \;
    rm -rf ${D}${includedir}/rapidjson
}

BBCLASSEXTEND = "native"

FILES_${PN} += "${libdir}/pkgconfig/apl.pc "
ASNEEDED=""

