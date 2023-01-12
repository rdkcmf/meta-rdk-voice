SUMMARY = "Smart Screen SDK"
DESCRIPTION = "Alexa Smart Screen SDK" 
LICENSE = "Apache-2.0 & MIT & AmazonSoftwareLicense-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

FILESEXTRAPATHS_prepend:= "${THISDIR}/files:"
FILESEXTRAPATHS_prepend:= "${THISDIR}/alexa-web-components:"
PROVIDES = "alexa-web-sdk"
RPROVIDES_${PN} = "alexa-web-sdk"

FILES_${PN} = "${libdir}"
FILES_${PN}-dev = "${includedir}"

SRC_URI += "https://nodejs.org/dist/v14.20.1/node-v14.20.1-linux-x64.tar.xz;name=nodejs;subdir=nodejs"
SRC_URI[nodejs.md5sum] = "6616e5d1380813f96a8ece5d5606eca4"

SRC_URI += "file://0001-APLClient-library-fix.patch"

#OOBE-Screen
SRC_URI  += "git://github.com/alexa/avs-sdk-oobe-screens-demo.git;branch=main;protocol=https;name=alexa-sdk-oobe-screens;destsuffix=avs-sdk-oobe-screens"
SRC_URI += "file://0008-Add-interactive-authentication-screen-support.patch;patchdir=${WORKDIR}/avs-sdk-oobe-screens"

#alexa-client-library
SRC_URI  += "git://github.com/alexa/apl-client-library.git;branch=main;protocol=https;name=apl-client-lib"
SRCREV_apl-client-lib ="${AUTOREV}"

#web-components
SRC_URI  += "git://github.com/alexa/alexa-smart-screen-web-components.git;branch=main;protocol=https;name=alexa-web-components;destsuffix=alexa-web-components"
SRCREV_alexa-web-components ="${AUTOREV}"
SRC_URI += "file://0002-modify-ui-attributes-3.0.patch;patchdir=${WORKDIR}/alexa-web-components"
SRC_URI += "file://0003-Add-support-for-GUI-external-JS-functions-3.0.patch;patchdir=${WORKDIR}/alexa-web-components"

include ${@bb.utils.contains('DISTRO_FEATURES', 'voice-chrome', "avs-voice-chrome-1.1.0.inc", "", d)}

#OOBE-Screen
SRCREV_alexa-sdk-oobe-screens ="${AUTOREV}"

SRCREV_FORMAT ="${AUTOREV}"

LDFLAGS += "-pthread"

S = "${WORKDIR}/git"
SB = "${WORKDIR}/build"

do_compile_prepend() {
    export PATH=${WORKDIR}/nodejs/node-v14.20.1-linux-x64/bin:${PATH}
    npm install --global yarn
    cd ${WORKDIR}/alexa-web-components/packages/alexa-smart-screen-apl
    yarn add file: ${S}/apl-client-js -D
    cd ${WORKDIR}/alexa-web-components
    yarn install
    yarn build
}

do_install_prepend() {
    export PATH=${WORKDIR}/nodejs/node-v14.20.1-linux-x64/bin:${PATH}
}

INSANE_SKIP_${PN} = "dev-so"
INSANE_SKIP_${PN} += "dev-deps"
TARGET_CC_ARCH += "${LDFLAGS}"

AVS_DIR ?= "/home/root/Alexa_SDK"

RDEPENDS_${PN} += "bash perl libwebsockets"

DEPENDS += " websocketpp asio apl-core rapidjson"

#inherit pkgconfig
inherit pkgconfig cmake systemd

EXTRA_OECMAKE = " \
-DASDK_LIBRARY_DIRS= ${STAGING_LIB_DIR} \
-DWEBSOCKETPP_INCLUDE_DIR=${STAGING_INCDIR} \
-DCMAKE_BUILD_TYPE=DEBUG \
-DBUILD_TESTING=OFF \
-DAPL_CORE=ON \
-DAPLCORE_INCLUDE_DIR=${STAGING_INCDIR} \
-DAPLCORE_EXPORTS_INCLUDE_DIR=${STAGING_INCDIR} \
-DAPLCORE_EXPORTS_LIB_DIR=${STAGING_LIBDIR} \
-DAPLCORE_RAPIDJSON_INCLUDE_DIR=${STAGING_INCDIR}/AVS \
-DYOGA_INCLUDE_DIR=${STAGING_INCDIR} \
-DYOGA_LIB_DIR=${STAGING_LIBDIR} \
-DSTANDALONE=OFF \
-DCMAKE_INSTALL_PREFIX=${D} \
"

do_install_prepend() {

 install -d ${D}${libdir}
 install -d ${D}${includedir}
 install -d ${D}${AVS_DIR}/alexa-smart-screen
 install -d ${D}${AVS_DIR}/avs-sdk-oobe-screens

}

do_install_append() {

 #install -d ${D}${libdir} 
 #install -d ${D}${includedir}
 #install -d ${D}${AVS_DIR}/alexa-smart-screen
 cp -av --no-preserve=ownership ${S}/APLClient/include/* ${D}${includedir}

 cp -av --no-preserve=ownership ${WORKDIR}/alexa-web-components/samples/alexa-smart-screen-sample-app/dist/* ${D}${AVS_DIR}/alexa-smart-screen/

#Fix - few libraries not not copied by default build script
    #cd ${SB}
    find ${WORKDIR}/build -iname 'libAPLClient.so' -exec cp {} ${D}${libdir} \;

#Authentication scheme
    cp -av --no-preserve=ownership ${WORKDIR}/avs-sdk-oobe-screens/* ${D}${AVS_DIR}/avs-sdk-oobe-screens/
    cp -av --no-preserve=ownership ${D}${AVS_DIR}/alexa-smart-screen/main.bundle.js ${D}${AVS_DIR}/avs-sdk-oobe-screens/js/
    mv ${D}${AVS_DIR}/avs-sdk-oobe-screens/ss-sdk.html ${D}${AVS_DIR}/avs-sdk-oobe-screens/index.html
}


BBCLASSEXTEND = "native"
FILES_${PN} += "${AVS_DIR}/ ${includedir} ${libdir}"
FILES_${PN} += "${sysconfdir}/* "
ASNEEDED=""

