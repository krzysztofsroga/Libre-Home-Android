package com.krzysztofsroga.librehome.connection

import android.content.SharedPreferences
import android.util.Log
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.krzysztofsroga.librehome.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

//https://stackoverflow.com/questions/14323661/simple-ssh-connect-with-jsch
@UseExperimental(ExperimentalCoroutinesApi::class)
class SshConnection(prefs: SharedPreferences) {
    private val host = prefs.getString(AppConfig.PrefKeys.HOST, InternetConfiguration.defaultDomoticzHostname)
    private val sshPassword = prefs.getString(AppConfig.PrefKeys.SSH_PASSWORD, "")
    private val port = InternetConfiguration.sshPort
    private val username = InternetConfiguration.rPiUsername

    private fun initializeSession(): Session {
        return JSch().getSession(username, host, port).apply {
            setPassword(sshPassword)
            setConfig("StrictHostKeyChecking", "no")
        }
    }

    suspend fun checkConnection(): Flow<String> = channelFlow {
        withContext(Dispatchers.IO) {
            try {
                val session = initializeSession()
                send("Connecting...")
                session.connect()
                session.disconnect()
                send("Connected!")
            } catch (e: Exception) {
                Log.d("SSH", e.toString())
                send("Connection failed")
            }
        }
    }

    suspend fun restartRpi(): Flow<String> = channelFlow {
        withContext(Dispatchers.IO) {
            try {
                val session = initializeSession()
                send("Connecting...")
                session.connect()
                send("Connected!")

                val channel = session.openChannel("exec") as ChannelExec
                val output = ByteArrayOutputStream()
                channel.outputStream = output

                // Execute command
                channel.setCommand("sudo shutdown -r now") //TODO sudo reboot
                send("Sending command...")
                channel.connect()
                send("Awaiting response...")
                Thread.sleep(1000)
                channel.disconnect()
                session.disconnect()
                output.toString().let {
                    if (it.isBlank())
                        send("Reboot successful")
                    else
                        send(it)
                }

            } catch (e: Exception) {
                Log.d("SSH", e.toString())
                send("Connection failed")
            }
        }
    }
}