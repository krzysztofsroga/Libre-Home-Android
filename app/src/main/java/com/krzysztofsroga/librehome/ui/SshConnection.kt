package com.krzysztofsroga.librehome.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.krzysztofsroga.librehome.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class SshConnection(prefs: SharedPreferences) {
    private val host = prefs.getString(AppConfig.PrefKeys.HOST, AppConfig.defaultDomoticzHostname)
    private val sshPassword = prefs.getString(AppConfig.PrefKeys.SSH_PASSWORD, "")
    private val port = AppConfig.sshPort
    private val username = AppConfig.rPiUsername

    suspend fun restartRpi(_out: MutableLiveData<String>): String {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val session = JSch().getSession(username, host, port)
                session.setPassword(sshPassword)

                // Avoid asking for key confirmation
                session.setConfig("StrictHostKeyChecking", "no")
                _out.postValue("Connecting...")
                session.connect()
                _out.postValue("Connected!")

                val channel = session.openChannel("exec") as ChannelExec
                val output = ByteArrayOutputStream()
                channel.outputStream = output

                // Execute command
                channel.setCommand("uname -a") //TODO sudo reboot
                _out.postValue("Sending command...")
                channel.connect()
                _out.postValue("Awaiting response...")
                Thread.sleep(1000)
                channel.disconnect()
                output.toString()
            } catch (e: Exception) {
                Log.d("SSH", e.toString())
                "Connection failed"
            }
        }
    }
}