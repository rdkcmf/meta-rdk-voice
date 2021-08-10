FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
include include/avs.inc

DEPENDS += "skillmapper"
RDEPENDS_${PN} += "skillmapper"
SRC_URI  += "file://0003-skillmapper-integration.patch"
SRC_URI  += "file://0004-template-card-support.patch"

PACKAGECONFIG_append = " avs"


do_install_append() {
 install -d ${D}/root
 install -d ${D}/root/AVS
 install -d ${D}/root/AVS/db
}
FILES_${PN} += " root/AVS/db"

ASNEEDED=""
