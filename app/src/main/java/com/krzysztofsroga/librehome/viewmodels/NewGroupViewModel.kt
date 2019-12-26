package com.krzysztofsroga.librehome.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class NewGroupViewModel :ViewModel() {
    val tmpImagePath : MutableLiveData<File?> = MutableLiveData(null)
    val selected: MutableSet<Int> = mutableSetOf()
}