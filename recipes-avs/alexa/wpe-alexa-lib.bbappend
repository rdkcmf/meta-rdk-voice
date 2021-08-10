do_install_append() {
    install -d ${D}${includedir}
    install -m 644 ${S}/SampleApp/include/SampleApp/SampleApplication.h ${D}${includedir}/AVS/SampleApp/
}

