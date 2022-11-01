# Use xravs for ControlMgr voice support
BBMASK .= "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', '|.meta-rdk-voice/recipes-wpe/wpeframework/|.meta-rdk-voice/recipes-avs/pryonlite/|.meta-rdk-voice/recipes-extended/webkitbrowser-plugin/', '|.meta-rdk-voice/recipes-xravs/', d)}"

BBMASK .= "${@bb.utils.contains('DISTRO_FEATURES', 'alexa-sdk-3.0', '|.meta-rdk-voice/recipes-avs/alexa/wpe-alexa-lib.bb|.meta-rdk-voice/recipes-avs/alexa-smart-screen/alexa-ss-sdk_git.bb', '|.meta-rdk-voice/recipes-avs/alexa/wpe-alexa-lib_3.0.bb|.meta-rdk-voice/recipes-avs/alexa/alexa-sampleapp-lib.bb|.meta-rdk-voice/recipes-avs/alexa-smart-screen/alexa-web-sdk_git.bb', d)}"
