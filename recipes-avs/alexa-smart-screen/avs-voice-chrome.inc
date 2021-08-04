#Adds Voice chrome support with Alexa Smart Screen GUI

#This URL can be fetched from setup-environment or by re-writing the URL from a bbappend file in device specific layer

SRC_URI += "file://${RDKROOT}/downloads/SmartScreenSDKVoiceChrome-2.4.tar.gz;name=voicechrome;subdir=voicechrome"
SRC_URI[voicechrome.md5sum] = "185d182773f368a49515c75031f188d7"
SRC_URI[voicechrome.sha256sum] = "adbdf29899e1757e8ed262b709d0db9c86b92d2d8b6e9b8e1d17a5c0e9e5882c"


SRC_URI += "file://0001-voice-chrome-addition-2.6.patch"

do_compile_prepend() {
    cp -av --no-preserve=ownership ${WORKDIR}/voicechrome/SmartScreenSDKVoiceChrome-2.4/src/VoiceChrome.tsx ${S}/modules/GUI/js/src/components/VoiceChrome.tsx
    rm -rf ${S}/modules/GUI/js/node_modules ${S}/modules/GUI/js/package-lock.json
}

do_install_append() {
    cp -av --no-preserve=ownership ${WORKDIR}/voicechrome/SmartScreenSDKVoiceChrome-2.4/avs-voicechrome/assets/* ${D}/${AVS_DIR}/alexa-smart-screen
}
