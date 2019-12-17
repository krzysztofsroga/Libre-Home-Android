package com.krzysztofsroga.librehome.connection

object InternetConfiguration {
    const val sshPort = 22
    const val rPiUsername = "pi"
    const val defaultDomoticzHostname = "domoticz"  //"192.168.2.191"
    const val fullPath = "http://$defaultDomoticzHostname:8080" //TODO public IP address, some configuration
}