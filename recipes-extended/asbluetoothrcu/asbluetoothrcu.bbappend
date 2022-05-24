FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " ${@bb.utils.contains('DISTRO_FEATURES', 't4hworkaround', 'file://0001-COMCAST-794-T4H-RCU-Support.patch;patchdir=${WORKDIR}/git', '', d)}"

