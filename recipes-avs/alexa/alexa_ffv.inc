SRC_URI  += "git://github.com/Kitt-AI/snowboy.git;branch=master;protocol=https;destsuffix=snowboy;name=snowboy"
SRC_URI  += "file://0002-alexa-ffv.patch"

EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=RELEASE \
                 -DGSTREAMER_MEDIA_PLAYER=ON \
                 -DCMAKE_INSTALL_PREFIX=${D}${AVS_DIR}/avs-sdk-client \
                 -DKITTAI_KEY_WORD_DETECTOR=ON \
                 -DKITTAI_KEY_WORD_DETECTOR_LIB_PATH=${WORKDIR}/snowboy/lib/rpi/libsnowboy-detect.a \
                 -DKITTAI_KEY_WORD_DETECTOR_INCLUDE_DIR=${WORKDIR}/snowboy/include \
                 -DPORTAUDIO=ON \
                 -DPORTAUDIO_LIB_PATH=${STAGING_LIBDIR}/libportaudio.so \
                 -DPORTAUDIO_INCLUDE_DIR=${STAGING_INCDIR} \
"

DEPENDS += " portaudio-v19 openblas"

do_install_append() {
    install -d -m 0755 ${D}/home/root/resources
    cp -av --no-preserve=ownership ${WORKDIR}/snowboy/resources* ${D}/home/root
    cp -av --no-preserve=ownership ${WORKDIR}/snowboy/resources/alexa/alexa-avs-sample-app/alexa.umdl ${D}/home/root/resources
}

FILES_${PN} += " /home/root/resources ${WORKDIR}/wake"

