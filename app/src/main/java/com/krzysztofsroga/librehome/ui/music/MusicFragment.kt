package com.krzysztofsroga.librehome.ui.music

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.music_fragment.*
import android.media.MediaRecorder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.krzysztofsroga.librehome.ui.switches.LightSwitch
import kotlinx.coroutines.*
import kotlin.math.pow


class MusicFragment : Fragment() {

    private val switches = MusicSwitches()
    private lateinit var mediaRecorder: MediaRecorder

    private val managedLights = mutableSetOf<LightSwitch.DimmableSwitch>()

    companion object {
        fun newInstance() = MusicFragment()
    }

    private lateinit var viewModel: MusicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.music_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MusicViewModel::class.java)

        something1.setTextColor(Color.CYAN)
        something2.setBackgroundColor(Color.YELLOW)
        // TODO: Use the ViewModel

        initializeRecorder()
        switches.initialize()
        initializeList()

    }

    fun initializeList() {
        Log.d("initialization", "initializing list")
        switches.getLedSwitches { downloadedSwitches ->
            Log.d("initialization", "callback list")

            activity!!.runOnUiThread {
                Log.d("initialization", "ui list")
                music_switches_list.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = MusicSwitchListAdapter(downloadedSwitches) { switch, enabled ->
                        if (enabled) {
                            managedLights += switch
                        } else {
                            managedLights.remove(switch)
                        }
                    }.apply { setHasStableIds(true) }

                }

            }
        }
    }

    fun initializeRecorder() {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.RECORD_AUDIO),
                0);

        }

        else {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile("/dev/null")
                prepare()
                start()
            }
        }




// Obtain maximum amplitude since last call of getMaxAmplitude()

        CoroutineScope(Dispatchers.Default).launch {
            while(true) {
                delay(16)
//                delay(125)
                val amplitude = mediaRecorder.maxAmplitude
                var amp = amplitude.toDouble() * 10 - 1000
                if (amp <= 1) amp = 1.0
                val logAmplitude = Math.log(amp).toFloat() / 10
                val powAmplitude = logAmplitude.pow(2)

                withContext(Dispatchers.Main) {
                    something3.text = amplitude.toString()
                    val color = Color.HSVToColor(floatArrayOf(80f, 0f, powAmplitude))
                    something_frame.setBackgroundColor(color)
                    val color2 = Color.HSVToColor(floatArrayOf(270f, logAmplitude, 0.4f))
                    something2.setBackgroundColor(color2)
                }

//                managedLights.forEach {
//                    switches.sendSwitchState(it.apply { dim = (powAmplitude * 100).toInt()
//                    enabled = (dim > 0)})
//                }
            }
        }


// Don't forget to release

    }

    override fun onDestroy() {
        mediaRecorder.reset()
        mediaRecorder.release()
        super.onDestroy()
    }


}


/*
public void onRecordBtnClicked() {
  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
      != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO },
        10);
  } else {
    recordAudio();
  }
}

private void recordAudio() {
  //Record Audio.
}

@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
    @NonNull int[] grantResults) {
  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  if (requestCode == 10) {
    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      recordAudio();
    }else{
      //User denied Permission.
    }
  }
}
 */