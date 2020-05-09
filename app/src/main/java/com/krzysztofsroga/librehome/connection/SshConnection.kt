package com.krzysztofsroga.librehome.connection

import android.content.SharedPreferences
import android.util.Log
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.krzysztofsroga.librehome.AppConfig
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.utils.EventMessage
import com.krzysztofsroga.librehome.utils.EventMessage.RawMessage
import com.krzysztofsroga.librehome.utils.EventMessage.ResourceMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

//https://stackoverflow.com/questions/14323661/simple-ssh-connect-with-jsch
@OptIn(ExperimentalCoroutinesApi::class)
class SshConnection(prefs: SharedPreferences) {
    private val host = prefs.getString(AppConfig.PrefKeys.HOST, InternetConfiguration.defaultDomoticzHostname)
    private val sshPassword = prefs.getString(AppConfig.PrefKeys.SSH_PASSWORD, "")
    private val port = InternetConfiguration.sshPort
    private val username = prefs.getString(AppConfig.PrefKeys.SSH_USERNAME,InternetConfiguration.rPiUsername)

    private fun initializeSession(): Session {
        return JSch().getSession(username, host, port).apply {
            setPassword(sshPassword)
            setConfig("StrictHostKeyChecking", "no")
        }
    }

    suspend fun checkConnection(): Flow<EventMessage> = channelFlow {
        withContext(Dispatchers.IO) {
            try {
                val session = initializeSession()
                send(ResourceMessage(R.string.connecting))
                session.connect()
                session.disconnect()
                send(ResourceMessage(R.string.connected))
            } catch (e: Exception) {
                Log.d("SSH", e.toString())
                send(ResourceMessage(R.string.connection_failed))
            }
        }
    }

    suspend fun restartRpi(): Flow<EventMessage> = channelFlow {
        withContext(Dispatchers.IO) {
            try {
                val session = initializeSession()
                send(ResourceMessage(R.string.connecting))
                session.connect()
                send(ResourceMessage(R.string.connected))

                val channel = session.openChannel("exec") as ChannelExec
                val output = ByteArrayOutputStream()
                channel.outputStream = output

                // Execute command
                channel.setCommand("sudo shutdown -r now")
                send(ResourceMessage(R.string.sending_command))
                channel.connect()
                send(ResourceMessage(R.string.awaiting_response))
                Thread.sleep(1000)
                channel.disconnect()
                session.disconnect()
                output.toString().let {
                    if (it.isBlank())
                        send(ResourceMessage(R.string.reboot_success))
                    else
                        send(RawMessage(it))
                }

            } catch (e: Exception) {
                Log.d("SSH", e.toString())
                send(ResourceMessage(R.string.connection_failed))
            }
        }
    }
}