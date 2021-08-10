#Adds Voice chrome support with Alexa Smart Screen GUI

#This URL can be fetched from setup-environment or by re-writing the URL from a bbappend file in device specific layer
SRC_URI += "file://${RDKROOT}/downloads/SmartScreenSDKVoiceChrome-2.1.tar.gz;name=voicechrome;subdir=voicechrome"
SRC_URI[voicechrome.md5sum] = "bb667872561870e56ac7c9349abf3af8"
SRC_URI[voicechrome.sha256sum] = "59c24002346575396d04e304b6639540ba19e5cb8e0cb2619b41bda08fe40b16"

SRC_URI += "file://0006-voice-chrome-support.patch"

do_compile_prepend() {
    cp -av --no-preserve=ownership ${WORKDIR}/voicechrome/archive/src/VoiceChrome.tsx ${S}/modules/GUI/js/src/components/VoiceChrome.tsx
    rm -rf ${S}/modules/GUI/js/node_modules ${S}/modules/GUI/js/package-lock.json
}

do_install_append() {
    cp -av --no-preserve=ownership ${WORKDIR}/voicechrome/archive/avs-voicechrome/assets/* ${D}/${AVS_DIR}/alexa-smart-screen
}
