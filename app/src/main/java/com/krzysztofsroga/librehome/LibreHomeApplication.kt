package com.krzysztofsroga.librehome

import android.app.Application
import com.krzysztofsroga.librehome.utils.Logger
import com.yariksoffice.lingver.Lingver

class LibreHomeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.filesDir = filesDir
        Lingver.init(this) // TODO enable Lingver (and language change in settings) only in debug builds!
    }
}