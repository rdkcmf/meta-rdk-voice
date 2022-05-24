FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', 'file://0001-SDT-Session-Begin-End.patch;apply=no', '', d)}"

DEPENDS += "ctrlm-xraudio-hal"

ENABLE_SDT_SUPPORT  = " ${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', '1', '0', d)} "
EXTRA_OECONF_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', '--enable-xrsr_sdt', '', d)} "
XRAUDIO_RESOURCE_MGMT = " ${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', '0', '1', d)} "

addtask do_apply_patch after do_unpack before do_configure

do_apply_patch() {
    cd ${S}
    if [ ! -e patch_applied ]; then
        if [ "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', 'true', 'false', d)}" = "true" ]; then
                patch -p1 < ${WORKDIR}/0001-SDT-Session-Begin-End.patch
        fi
        touch patch_applied
    fi
}
