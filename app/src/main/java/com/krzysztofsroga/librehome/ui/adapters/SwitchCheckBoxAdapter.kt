package com.krzysztofsroga.librehome.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.LightSwitch
import kotlinx.android.synthetic.main.switch_check_entry.view.*
import kotlinx.android.synthetic.main.switch_entry.view.lightIcon

//TODO pass livedata?
class SwitchCheckBoxAdapter(private var lightSwitchList: List<LightSwitch>) : RecyclerView.Adapter<SwitchCheckBoxAdapter.SwitchViewHolder>() {

    private val _selected: MutableSet<LightSwitch> = mutableSetOf()

    val selected: Set<LightSwitch>
        get() = _selected

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.switch_check_entry, parent, false)
        return SwitchViewHolder(itemView)
    }

    override fun getItemCount(): Int = lightSwitchList.size

    override fun onBindViewHolder(holder: SwitchViewHolder, position: Int) {
        val switch = lightSwitchList[position]
        holder.loadSwitch(switch, switch in _selected) { isChecked ->
            when (isChecked) {
                true -> _selected += switch
                false -> _selected.remove(switch)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (lightSwitchList[position]) {
            is LightSwitch.SimpleSwitch -> 1
            is LightSwitch.DimmableSwitch -> 2
        }
    }

    override fun getItemId(position: Int): Long {
        return lightSwitchList[position].id!!.toLong() //TODO make id non nullable
    }

    fun updateData(newLightSwitchList: List<LightSwitch>) {
        lightSwitchList = newLightSwitchList
        notifyDataSetChanged()
    }

    class SwitchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val switch: CheckBox = view.switchCheckBox
        private val icon: ImageView = view.lightIcon

        fun loadSwitch(lightSwitch: LightSwitch, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
            switch.text = lightSwitch.name
            switch.isChecked = checked

            if (lightSwitch is LightSwitch.DimmableSwitch) {
                icon.setImageResource(R.drawable.light_dim)
            }

            switch.setOnClickListener {
                onCheckedChange(switch.isChecked)
            }
        }
    }
}