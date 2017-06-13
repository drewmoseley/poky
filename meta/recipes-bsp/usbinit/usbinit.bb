SUMMARY = "Initscript for enabling USB gadget Ethernet"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

PR = "r3"

SRC_URI = "file://usb-gether \
           file://usb-gserial \
           file://usb-gcdc \
           file://COPYING.GPL"
S = "${WORKDIR}"

do_install() {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/init.d
    install usb-gether ${D}${sysconfdir}/init.d
    install usb-gserial ${D}${sysconfdir}/init.d
    install usb-cdc ${D}${sysconfdir}/init.d
}

inherit update-rc.d allarch

INITSCRIPT_PACKAGES = "${PN}-gether ${PN}-gserial ${PN}-gcdc"

INITSCRIPT_NAME_${PN}-gether = "usb-gether"
INITSCRIPT_NAME_${PN}-gserial = "usb-gserial"
INITSCRIPT_NAME_${PN}-gcdc = "usb-cdc"
INITSCRIPT_PARAMS_${PN}-gcdc = "start 99 5 2 . stop 20 0 1 6 ."
