SUMMARY = "RDK Skill mappper library for alexa"
LICENSE = "Apache-2.0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
FILESEXTRAPATHS_prepend := "${THISDIR}/:"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b1e01b26bacfc2232046c90a330332b3"

SRC_URI  = "${CMF_GIT_ROOT}/rdk/components/generic/avs/alexa_skill_mapper;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=skillmapper"

SRCREV_skillmapper ="${AUTOREV}"
#SRCREV_FORMAT = "skillmapper"
inherit cmake
OECMAKE_GENERATOR = "Unix Makefiles"

DEPENDS = "wpeframework rapidjson"
DEPENDS += "virtual/alexa-device-sdk"
DEPENDS += "aws-cpp-sdk"

S = "${WORKDIR}/git"

SMARTSCREEN_SUPPORT ?= "ON"
FRONTPANEL_SUPPORT ?= "OFF"
RDKSERVICE_SUPPORT ?= "ON"
XR_SPEECH_AVS_SUPPORT ?= "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', 'ON', '', d)}"

EXTRA_OECMAKE += " \
    -DSKILLMAPPER_SMARTSCREEN_SUPPORT=${SMARTSCREEN_SUPPORT} \
    -DSKILLMAPPER_FRONTPANEL_SUPPORT=${FRONTPANEL_SUPPORT} \
    -DSKILLMAPPER_RDKSERVICE_SUPPORT=${RDKSERVICE_SUPPORT} \
    -DBUILD_REFERENCE=${SRCREV} \
    -DSKILLMAPPER_XR_SPEECH_AVS_SUPPORT=${XR_SPEECH_AVS_SUPPORT} \
"
AVS_DIR ?= "/home/root/Alexa_SDK"
do_install_append () {
    install -d -m 0755 ${D}/home/root/.aws
    install -d -m 0755 ${D}${AVS_DIR}/Integration
    cp -av --no-preserve=ownership ${S}/AlexaCurl.json ${D}${AVS_DIR}/Integration
    cp -av --no-preserve=ownership ${S}/AlexaRdkServiceSkillMap.json ${D}${AVS_DIR}/Integration
    cp -av --no-preserve=ownership ${S}/SmartScreenControl.json ${D}${AVS_DIR}/Integration
    cp -av --no-preserve=ownership ${S}/VSKConfig.json ${D}${AVS_DIR}/Integration
    cp -av --no-preserve=ownership ${S}/credentials ${D}/home/root/.aws
}
FILES_${PN} += "${libdir}/*.so /home/root/.aws/* ${AVS_DIR}"
FILES_${PN}-dev = "${includedir} ${libdir}/cmake/*"

