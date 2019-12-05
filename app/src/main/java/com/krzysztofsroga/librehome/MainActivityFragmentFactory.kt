package com.krzysztofsroga.librehome

import androidx.fragment.app.Fragment

interface MainActivityFragmentFactory<T: Fragment> {
    fun newInstance(): T
    val name: String
}
