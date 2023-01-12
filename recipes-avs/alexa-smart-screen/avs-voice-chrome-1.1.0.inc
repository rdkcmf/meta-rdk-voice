#Adds Voice chrome support with Alexa Smart Screen GUI

#This URL can be fetched from setup-environment or by re-writing the URL from a bbappend file in device specific layer

SRC_URI += "file://${RDKROOT}/downloads/AlexaSmartScreenVoiceChrome-1.1.0.tar;name=voicechrome;destsuffix=voicechrome"
SRC_URI[voicechrome.md5sum] = "5add618d41af70b578c786e6b3a40bf2"
SRC_URI[voicechrome.sha256sum] = "6954196b501dd9aa4ed0e29999a03c43789b8a108bc7cac5c2b25354617b0d83"


addtask do_apply_patch after do_unpack before do_configure
do_apply_patch() {
    cd ${S}
    if [ ! -e patch_applied ]; then
        bbnote "Patching alexa-smart-screen-voice-chrome-linear.patch"
        patch -p1 -d${WORKDIR}/alexa-web-components < ${WORKDIR}/alexa-smart-screen-voice-chrome-linear/alexa-smart-screen-voice-chrome-linear.patch
        touch patch_applied
    fi
}

do_configure_append() {
    cp -r -av --no-preserve=ownership ${WORKDIR}/alexa-smart-screen-voice-chrome-linear ${WORKDIR}/alexa-web-components/packages
    #rm -rf ${S}/modules/GUI/js/node_modules ${S}/modules/GUI/js/package-lock.json
}

