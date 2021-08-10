do_install_append () {
	sed -i '$ a alias.url += ( "/alexa-smart-screen/" => "/home/root/Alexa_SDK/alexa-smart-screen/" )' ${D}${sysconfdir}/lighttpd.d/offline_apps.conf
}
