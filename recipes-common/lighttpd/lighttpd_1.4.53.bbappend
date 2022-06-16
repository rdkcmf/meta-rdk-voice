do_install_append () {
	sed -i '$ a alias.url += ( "/alexa-smart-screen/" => "/home/root/Alexa_SDK/alexa-smart-screen/" )' ${D}${sysconfdir}/lighttpd.d/offline_apps.conf
        sed -i '$ a alias.url += ( "/avs-sdk-oobe-screens/" => "/home/root/Alexa_SDK/avs-sdk-oobe-screens/" )' ${D}${sysconfdir}/lighttpd.d/offline_apps.conf
}
