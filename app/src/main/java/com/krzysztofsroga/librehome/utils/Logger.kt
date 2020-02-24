package com.krzysztofsroga.librehome.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Logger {
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private const val logFolder = "Logs"

    lateinit var filesDir: File
    private val logDir get() = filesDir.resolve(logFolder)
    private val fileName get() = "Log_${dateFormat.format(Date())}.txt"
    private val file: File get() = logDir.resolve(fileName)

    fun e(tag: String, text: String) {
        Log.e(tag, text)
        val value = "\n" +
                "${dateTimeFormat.format(Date())}\n" +
                "Error: $tag\n" +
                "$text\n"
        GlobalScope.launch(Dispatchers.IO) {
            logDir.mkdirs()
            if (!file.exists()) file.createNewFile()
            file.appendText(value)
        }
    }

    fun getTodayLog(): String = file.readText()

    fun getLogFileNames(): Array<String> = logDir.list() ?: arrayOf()

    fun getLog(name: String): String = logDir.resolve(name).readText()
}