package com.krzysztofsroga.librehome.utils

import android.content.res.Configuration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

fun Int.isEven() = (this and 1) == 0
fun Int.odd() = (this and 1) == 1

fun RecyclerView.getCurrentOrientationLayoutManager(): RecyclerView.LayoutManager {
    return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    } else {
        LinearLayoutManager(context)
    }
}