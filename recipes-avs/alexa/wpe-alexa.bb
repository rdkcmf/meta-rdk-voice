SUMMARY = "AVS SDK"
DESCRIPTION = "Amazon Voice Service Integration In To RDK Framework" 
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://VoiceToApps/LICENSE.txt;md5=3424fb8f52974b4894e41f79768039c3" 

FILESEXTRAPATHS_prepend:= "${THISDIR}/files:"
PROVIDES = "virtual/alexa-device-sdk"
RPROVIDES_${PN} = "virtual/alexa-device-sdk"

FILES_${PN}-sampleapp = "${bindir}/SampleApp"
FILES_${PN} = "${libdir}"
FILES_${PN}-dev = "${includedir}"

OECMAKE_GENERATOR = "Unix Makefiles"

BASE_URI ?= "git://github.com/alexa/avs-device-sdk.git;branch=master;protocol=https;name=wpe-alexa;destsuffix=git"
SRC_URI   = "${BASE_URI}"

SRCREV_wpe-alexa = "f82767c783194992e1770f47090f0176d2c90e27"
SRCREV ="${AUTOREV}"

SRCREV_FORMAT ="${AUTOREV}"

SRC_URI  += "${CMF_GIT_ROOT}/components/generic/avs/alexa_skill_mapper;protocol=${CMF_GIT_PROTOCOL};branch=rdk-next;name=skillmapper;destsuffix=git/VoiceToApps"
SRC_URI  += "file://0001-gst-adjust-rank.patch"
SRC_URI  += "file://AlexaClientSDKConfig.json"
SRC_URI  += "file://alexa.service"

LDFLAGS += "-pthread"

INSANE_SKIP_${PN} = "dev-so"
TARGET_CC_ARCH += "${LDFLAGS}"

S = "${WORKDIR}/git"
SB = "${WORKDIR}/build"

AVS_DIR ?= "/home/root/Alexa_SDK"

inherit pkgconfig cmake systemd

RDEPENDS_${PN} += "bash perl libwebsockets"
DEPENDS = "curl nghttp2 sqlite3 gstreamer1.0-plugins-base cjson"

#Selection of either FFV or Push-to-talk profiles based on alexa_ffv distro feature
require ${@bb.utils.contains('DISTRO_FEATURES', 'alexa_ffv', 'alexa_ffv.inc', 'alexa_ble.inc', d)}

do_compile() {
    cd ${SB}
    oe_runmake ${PARALLEL_MAKE} SampleApp
}

do_install() {
    install -d -m 0755 ${D}${AVS_DIR}/Integration
    install -d -m 0755 ${D}/sounds
    install -d -m 0755 ${D}/database
    install -d -m 0755 ${D}/home/root/resources
    install -d ${D}${systemd_unitdir}/system

    install -m 0644 ${WORKDIR}/alexa.service ${D}${systemd_unitdir}/system/
    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
    ln -sf ${systemd_unitdir}/system/alexa.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/alexa.service

    cp -av --no-preserve=ownership ${WORKDIR}/AlexaClientSDKConfig.json ${D}${AVS_DIR}/Integration
    cp -av --no-preserve=ownership ${WORKDIR}/git/VoiceToApps/AlexaCurl.json ${D}${AVS_DIR}/Integration

    cd ${SB}
    #copying the .so to the /usr/lib
    install -d ${D}${libdir}
    find -iname '*.so' -exec cp {} ${D}${libdir} \;

    #copying thr executable in to the /usr/bin
    install -d ${D}${bindir}
    cp -r -L SampleApp/src/SampleApp ${D}${bindir}

    #copying the script file in to /usr/bin
    cp -r -L ${S}/tools/Install/genConfig.sh ${D}${bindir}
}

BBCLASSEXTEND = "native"

SYSTEMD_SERVICE_${PN} += "alexa.service"
FILES_${PN} += "${AVS_DIR}   /sounds /database ${bindir} ${libdir}  ${includedir}" 
FILES_${PN} += "${sysconfdir}/* ${systemd_unitdir}/system/alexa.service"
