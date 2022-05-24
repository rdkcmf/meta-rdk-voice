do_install_append () {
	sed -i '$ a alias.url += ( "/alexa-smart-screen/" => "/home/root/Alexa_SDK/alexa-smart-screen/" )' ${D}${sysconfdir}/lighttpd.d/offline_apps.conf
    sed -i '$ a alias.url += ( "/custom-sssdk-oobe/" => "/home/root/Alexa_SDK/custom-sssdk-oobe/" )' ${D}${sysconfdir}/lighttpd.d/offline_apps.conf
}
