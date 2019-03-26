package com.krzysztofsroga.librehome.ui.Switches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.switch_entry.view.*

class SwitchListAdapter(private val switchModelList: List<SwitchModel>) :
    RecyclerView.Adapter<SwitchListAdapter.SwitchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.switch_entry, parent, false)
        return SwitchViewHolder(itemView)
    }

    override fun getItemCount(): Int = switchModelList.size

    override fun onBindViewHolder(holder: SwitchViewHolder, position: Int) {
        holder.name.text = switchModelList[position].name
        holder.name.isChecked = switchModelList[position].state
    }


    class SwitchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: android.widget.Switch = view.switchName //TODO rename my SwitchModel class so that it doesn't collide with library names
    }
}