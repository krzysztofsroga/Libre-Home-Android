package com.krzysztofsroga.librehome.ui.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.adapters.LogEntryAdapter
import com.krzysztofsroga.librehome.utils.Logger
import com.krzysztofsroga.librehome.utils.getCurrentOrientationLayoutManager
import com.krzysztofsroga.librehome.utils.showToast
import kotlinx.android.synthetic.main.logs_actvity_activity.*


class LogsActvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logs_actvity_activity)

        val logFileNames = Logger.getLogFileNames()
        if (logFileNames.isEmpty()) {
            logs_list.visibility = View.GONE
            no_logs_yet_text.visibility = View.VISIBLE
        } else {
            logs_list.apply {
                layoutManager = getCurrentOrientationLayoutManager()
                setHasFixedSize(true)
                adapter = LogEntryAdapter(logFileNames) { fileName ->
                    log_title.text = fileName
                    log_content.setText(Logger.getLog(fileName))
                    log_content.visibility = View.VISIBLE
                    button_send_log.isEnabled = true
                    button_copy_log.isEnabled = true
                }
            }
        }

        button_send_log.setOnClickListener {
            val subject = "LibreHome Log: ${log_title.text}"
            val body = log_content.text.toString()
            val email = "support@sroga.dev"
            val chooserTitle = getString(R.string.send_email)

            ShareCompat.IntentBuilder.from(this)
                .setType("message/rfc822")
                .addEmailTo(email)
                .setSubject(subject)
                .setText(body)
                .setChooserTitle(chooserTitle)
                .startChooser();
        }

        button_copy_log.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("LibreHome Log", log_content.text.toString())
            clipboard.setPrimaryClip(clip)
            showToast(getString(R.string.log_copy))
        }
    }
}
