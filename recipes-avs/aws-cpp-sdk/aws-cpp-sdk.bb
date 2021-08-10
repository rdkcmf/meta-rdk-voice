SUMMARY = "AWS CPP SDK component"
SECTION = "devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
    file://LICENSE.txt;md5=e0a34bbbe8370ca4767991e22fc3fda8 \
"

PV = "1.6.53+git${SRCPV}"

SRCREV = "59337ec3dd6a998bddb41e4bad39e783cad34b6b"
SRC_URI = "git://github.com/aws/aws-sdk-cpp.git;protocol=https"
S = "${WORKDIR}/git"

inherit cmake

DEPENDS += "curl openssl zlib"

BUILD_COMPONENTS="core;sqs"

EXTRA_OECMAKE += "-DBUILD_DEPS=ON -DBUILD_SHARED_LIBS=ON -DBUILD_ONLY='${BUILD_COMPONENTS}'"
EXTRA_OECMAKE += "-DWERROR=0 -DCURL_HAS_H2_EXITCODE=0 -DCURL_HAS_H2_EXITCODE__TRYRUN_OUTPUT='' -DCURL_HAS_TLS_PROXY_EXITCODE=0 -DCURL_HAS_TLS_PROXY_EXITCODE__TRYRUN_OUTPUT=''"

do_install11_append() {
    for libFile in `find ${D}${libdir} -name libaws*`
    do
	ln -sf `basename ${libFile}` ${libFile}.0.0
	ln -sf `basename ${libFile}` ${libFile}.0
    done
}
FILES_${PN} += "${libdir}"
FILES_${PN}-dev = "${includedir}"

CXXFLAGS_append_dunfell = " -Wno-redundant-move -Wno-deprecated-copy "

 
