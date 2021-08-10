DEPENDS_class-target += "nghttp2"

PACKAGECONFIG_append = " nghttp2 "
PACKAGECONFIG[nghttp2] = "--with-nghttp2,--without-nghttp2"

