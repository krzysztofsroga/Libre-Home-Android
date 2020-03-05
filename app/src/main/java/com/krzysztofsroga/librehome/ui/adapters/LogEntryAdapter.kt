package com.krzysztofsroga.librehome.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.entry_log_name.view.*

class LogEntryAdapter(private var logList: Array<String>, private val callback: (String) -> Unit) : RecyclerView.Adapter<LogEntryAdapter.LogEntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogEntryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.entry_log_name, parent, false)
        return LogEntryViewHolder(itemView)
    }

    override fun getItemCount(): Int = logList.size

    override fun onBindViewHolder(holder: LogEntryViewHolder, position: Int) {
        val name = logList[position]
        holder.name.text = name
        holder.layout.setOnClickListener { callback(name) }
    }

    class LogEntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: View = view
        val name: TextView = view.log_file_name
    }
}