SUMMARY = "Amazon Pryon Lite Wake word Library"
LICENSE = "Apache-2.0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
FILESEXTRAPATHS_prepend := "${THISDIR}/:"

PROVIDES = "virtual/alexa-kwd-detector"
RPROVIDES_${PN} = "virtual/alexa-kwd-detector"

S = "${WORKDIR}"
LIC_FILES_CHKSUM = "file://${S}/LICENSE.txt;md5=1cdb860fab4148492f8b4ebb36002fb5"

#This URL can be fetched from setup-environment or by re-writting the URL from a bbappend file in device specific layer
SRC_URI = "file://${RDKROOT}/downloads/pryon_lite_2.3.0-metrological-2019.12.23.2344.zip"
SRC_URI[md5sum] = "382b57342d6c20b2fd7cc4da950937a7"
SRC_URI[sha256sum] = "5ced5e358016398ab649464eb3674fef581174357ab130637f7177f778d9295f"

do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {

    install -d -m 0755 ${D}/usr/share/WPEFramework/AVS/models
    install -d -m 0755 ${D}${libdir}
    install -d -m 0755 ${D}${includedir}

    cp -av --no-preserve=ownership ${S}/models/* ${D}/usr/share/WPEFramework/AVS/models/
    cp -av --no-preserve=ownership ${S}/bcm72604/localeToModels.json ${D}/usr/share/WPEFramework/AVS/models/
    cp -av --no-preserve=ownership ${S}/bcm72604/libpryon_lite.so* ${D}${libdir}
    cp -av --no-preserve=ownership ${S}/bcm72604/libpryon_lite.a ${D}${libdir}
    cp -av --no-preserve=ownership ${S}/bcm72604/*.h ${D}${includedir}
}
FILES_${PN} += "/usr/share/WPEFramework/* ${libdir} "
FILES_${PN}-dev = "${includedir}"
INSANE_SKIP_${PN} += " already-stripped"
