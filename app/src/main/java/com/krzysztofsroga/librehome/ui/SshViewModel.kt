package com.krzysztofsroga.librehome.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

//https://stackoverflow.com/questions/14323661/simple-ssh-connect-with-jsch
class SshViewModel(application: Application) : AndroidViewModel(application) {

    private val _out: MutableLiveData<String> = MutableLiveData()

    val out: LiveData<String>
        get() = _out

    private val prefs
        get() = PreferenceManager.getDefaultSharedPreferences(getApplication())

    fun restartRaspberry() {
        Log.d("SSH", prefs.all.toString()) //TODO remove
        val host = prefs.getString("host", "domoticz") //"192.168.2.191"
        val sshPassword = prefs.getString("ssh_password", "")
        val port = 22
        val username = "pi"

        viewModelScope.launch {
            _out.value = withContext(Dispatchers.IO) {
                return@withContext try {
                    val session = JSch().getSession(username, host, port)
                    session.setPassword(sshPassword)

                    // Avoid asking for key confirmation
                    session.setConfig("StrictHostKeyChecking", "no")
                    _out.postValue("Connecting...")
                    session.connect()
                    _out.postValue("Connected!")

                    val channelssh = session.openChannel("exec") as ChannelExec
                    val baos = ByteArrayOutputStream()
                    channelssh.outputStream = baos

                    // Execute command
                    channelssh.setCommand("uname -a")//TODO restart instead of getting kernel data
                    _out.postValue("Sending command...")
                    channelssh.connect()
                    _out.postValue("Awaiting response...")
                    Thread.sleep(1000)//TODO check if there's another way
                    channelssh.disconnect()
                    baos.toString()
                } catch (e: Exception) {
                    "Connection failed"
                }


            }


        }

    }
}
