SUMMARY = "Initscript for enabling USB gadget Ethernet"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

PR = "r3"

SRC_URI = "file://usb-gether \
           file://usb-gserial \
           file://usb-gcdc \
           file://gether-mod \
           file://gserial-mod \
           file://gcdc-mod \
           file://COPYING.GPL"
S = "${WORKDIR}"

do_install() {
    install -d ${D}${sysconfdir}

    # Only install usb-g* scripts if 'sysvinit' is in DISTRO_FEATURES
    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        install -d ${D}${sysconfdir}/init.d
        install usb-gether ${D}${sysconfdir}/init.d
        install usb-gserial ${D}${sysconfdir}/init.d
        install usb-cdc ${D}${sysconfdir}/init.d
    fi

    # Install systemd related configuration file
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${sysconfdir}/modules-load.d
        install -m 0644 ${WORKDIR}/gether-mod ${D}${sysconfdir}/modules-load.d
        install -m 0644 ${WORKDIR}/gserial-mod ${D}${sysconfdir}/modules-load.d
        install -m 0644 ${WORKDIR}/gcdc-mod ${D}${sysconfdir}/modules-load.d
    fi
}

# As the recipe doesn't inherit systemd.bbclass, we need to set this variable
# manually to avoid unnecessary postinst/preinst generated.
python __anonymous() {
    if not bb.utils.contains('DISTRO_FEATURES', 'sysvinit', True, False, d):
        d.setVar("INHIBIT_UPDATERCD_BBCLASS", "1")
}

inherit update-rc.d allarch

INITSCRIPT_PACKAGES = "${PN}-gether ${PN}-gserial ${PN}-gcdc"

INITSCRIPT_NAME_${PN}-gether = "usb-gether"
INITSCRIPT_NAME_${PN}-gserial = "usb-gserial"
INITSCRIPT_NAME_${PN}-gcdc = "usb-cdc"
INITSCRIPT_PARAMS_${PN}-gcdc = "start 99 5 2 . stop 20 0 1 6 ."
