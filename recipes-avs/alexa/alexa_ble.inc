SRC_URI  += "file://0003-wav-file-injection.patch"
SRC_URI  += "file://device.wav"

EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=RELEASE \
                 -DGSTREAMER_MEDIA_PLAYER=ON \
                 -DFILEAUDIO=ON \
"

DEPENDS += " libsndfile1 wpeframework"

do_install_append() {
    cp -av --no-preserve=ownership ${WORKDIR}/device.wav ${D}${AVS_DIR}/Integration
}

FILES_${PN} += " ${AVS_DIR}/Integration/device.wav"
