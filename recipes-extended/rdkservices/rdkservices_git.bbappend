FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'ctrlm', 'file://0001-Hack-to-put-CtrlMgr-to-start-scan-for-BT-RCU.patch', '', d)}"
