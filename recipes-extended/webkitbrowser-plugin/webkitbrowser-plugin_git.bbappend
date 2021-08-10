FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://SmartScreenApp.json"
do_install_append() {
    install -m 0664 ${WORKDIR}/SmartScreenApp.json ${D}/etc/WPEFramework/plugins/
}
