package com.krzysztofsroga.librehome.utils

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Bitmap
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.krzysztofsroga.librehome.database.SwitchesRoomDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.math.min

fun Int.isEven() = (this and 1) == 0
fun Int.odd() = (this and 1) == 1

fun RecyclerView.getCurrentOrientationLayoutManager(): RecyclerView.LayoutManager {
    return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    } else {
        LinearLayoutManager(context)
    }
}

class Event<out T>(private var _value: T?) {
    val value: T?
        get() = _value.also { _value = null }
}

fun Bitmap.scale(xRes: Int, yRes: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, xRes, yRes, true)
}

fun Bitmap.cropSquare(): Bitmap {
    val minDim = min(height, width)
    return Bitmap.createBitmap(this, (width - minDim) / 2, (height - minDim) / 2, minDim, minDim)
}

fun Bitmap.saveAsJpeg(file: File) {
    file.delete()
    file.createNewFile()
    FileOutputStream(file).use { ostream ->
        compress(Bitmap.CompressFormat.JPEG, 90, ostream)
    }
}

val Exception.stackTraceString: String
    get() {
        val sw = StringWriter()
        printStackTrace(PrintWriter(sw))
        return sw.toString()
    }

val AndroidViewModel.prefs: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(getApplication())

val AndroidViewModel.switchesDb: SwitchesRoomDatabase
    get() = SwitchesRoomDatabase.getDatabase(getApplication())

fun Activity.showToast(value: String) {
    Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(value: String) {
    requireActivity().showToast(value)
}