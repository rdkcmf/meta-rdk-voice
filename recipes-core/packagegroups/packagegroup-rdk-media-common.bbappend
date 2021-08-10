RDEPENDS_packagegroup-rdk-media-common += "\
    virtual/alexa-device-sdk \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alexa_ffv', 'openblas', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alexa_smart_screen', 'alexa-ss-sdk', '', d)} \
    "
