# Use xravs for ControlMgr voice support
BBMASK .= "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', '|.meta-rdk-voice/recipes-wpe/wpeframework/|.meta-rdk-voice/recipes-avs/pryonlite/|.meta-rdk-voice/recipes-extended/webkitbrowser-plugin/', '|.meta-rdk-voice/recipes-xravs/', d)}"
