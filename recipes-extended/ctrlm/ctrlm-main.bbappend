FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
        ${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', 'file://0001-remove-jenkins-version-check.patch;apply=no', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 't4hworkaround', 'file://0002-COMCAST-794-RDK-Voice-stack-T4H-session-support.patch', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', 'file://0003-Add-xr-speech-avs-support.patch;apply=no', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', 'file://0004-Async_server_msg_support.patch', '', d)} \
    "


EXTRA_OECONF_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', ' --enable-async_srvr_msg_support', ' ', d)}"
EXTRA_OECONF_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', ' --enable-xrsr_sdt_avs', ' ', d)}"
DEPENDS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', ' xr-speech-avs', ' ', d)}"

EXTRA_OECONF_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', ' --enable-ble', ' ', d)}"
DEPENDS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', ' libevdev ctrlm-xraudio-hal', ' ', d)}"
LDFLAGS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', ' -levdev -lanl', ' ', d)}"

RDEPENDS_${PN}_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', ' asbluetoothrcu', ' ', d)}"
CXXFLAGS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', ' -DCTRLM_NETWORK_BLE -DDEEPSLEEP_CLOSE_DB', '  -DCTRLM_HOST_DECRYPTION_NOT_SUPPORTED', d)}"

EXTRA_OECONF_append = "${@bb.utils.contains('VOICE_NEXTGEN_MAC', 'true', ' --enable-voice-nextgen-mac', '', d)}"

addtask do_apply_patch after do_unpack before do_configure

do_apply_patch() {
    cd ${S}
    if [ ! -e patch_applied ]; then
        if [ "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', 'true', 'false', d)}" = "true" ]; then
                bbnote "Patching 0001-remove-jenkins-version-check.patch"
                patch -p1 < ${WORKDIR}/0001-remove-jenkins-version-check.patch

                bbnote " "
                bbnote "Patching 0003-Add-xr-speech-avs-support.patch"
                patch -p1 < ${WORKDIR}/0003-Add-xr-speech-avs-support.patch
        fi
        touch patch_applied
    fi
}

do_configure_append() {
    if [ "${@bb.utils.contains('DISTRO_FEATURES', 'enable-rdkv-bt-voice', 'true', 'false', d)}" = "true" ]; then
        echo '{
        "voice" : {
        "url_src_ptt"                :  "avs://example.com"
        }
        }' > ${CTRLM_CONFIG_OEM_ADD}
    fi
}

