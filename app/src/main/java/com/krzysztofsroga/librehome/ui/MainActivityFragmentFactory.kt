package com.krzysztofsroga.librehome.ui

import androidx.fragment.app.Fragment

interface MainActivityFragmentFactory<T : Fragment> {
    fun newInstance(): T
    val name: String
}
