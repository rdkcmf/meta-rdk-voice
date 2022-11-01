#Adds Voice chrome support with Alexa Smart Screen GUI

#This URL can be fetched from setup-environment or by re-writing the URL from a bbappend file in device specific layer

# Reach out to Alexa to get the OEM/Operator specific release and override from OEM layer.
SRC_URI += "file://${RDKROOT}/downloads/AlexaSmartScreenVoiceChrome-1.0.0.tar;name=voicechrome;destsuffix=voicechrome"
SRC_URI[voicechrome.md5sum] = "9a5dc6a8b191d36e31a5680ba4204df6"
SRC_URI[voicechrome.sha256sum] = "6ba540a03d848a22106a7e98bc69344f66e5a4770ae1dc5641e27f9e41525a43"


SRC_URI += "file://0001-Voice-Chrome-support-1.0.0.patch;patchdir=${WORKDIR}/alexa-web-components"

do_configure_append() {
    cp -r -av --no-preserve=ownership ${WORKDIR}/alexa-smart-screen-voice-chrome-linear ${WORKDIR}/alexa-web-components/packages
    #rm -rf ${S}/modules/GUI/js/node_modules ${S}/modules/GUI/js/package-lock.json
}

do_install_append() {
    cp -av --no-preserve=ownership ${WORKDIR}/alexa-web-components/packages/alexa-smart-screen-voice-chrome-linear/avs-voicechrome/lib/* ${D}${AVS_DIR}/alexa-smart-screen
    cp -av --no-preserve=ownership ${WORKDIR}/alexa-web-components/packages/alexa-smart-screen-voice-chrome-linear/avs-voicechrome/lib/* ${D}${AVS_DIR}/avs-sdk-oobe-screens
}
