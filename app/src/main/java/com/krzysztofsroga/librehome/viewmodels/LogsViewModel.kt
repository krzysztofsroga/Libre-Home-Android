package com.krzysztofsroga.librehome.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogsViewModel : ViewModel() {
    val logName: MutableLiveData<String> = MutableLiveData()
}
