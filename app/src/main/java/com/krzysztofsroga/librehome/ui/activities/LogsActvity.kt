package com.krzysztofsroga.librehome.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.fragments.LogContentFragment
import com.krzysztofsroga.librehome.ui.fragments.LogsListFragment

class LogsActvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logs_actvity_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.list_container, LogsListFragment.newInstance())
                .replace(R.id.content_container, LogContentFragment.newInstance())
                .commitNow()
        }
    }
}
