package com.krzysztofsroga.librehome.ui

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
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext


class MusicFragment : Fragment() {

    private lateinit var mediaRecorder: MediaRecorder

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
                delay(33)
                val amplitude = mediaRecorder.maxAmplitude
                withContext(Dispatchers.Main) {
                    something3.text = amplitude.toString()
                    var amp = amplitude.toDouble() * 10 - 400
                    if (amp < 0) amp = 0.0
                    val logAmplitude = Math.log(amp).toFloat() / 10
                    val color = Color.HSVToColor(floatArrayOf(0f, 1f, logAmplitude))
                    something4.setBackgroundColor(color)
                    val color2 = Color.HSVToColor(floatArrayOf(270f, logAmplitude, 0.4f))
                    something2.setBackgroundColor(color2)
                }
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