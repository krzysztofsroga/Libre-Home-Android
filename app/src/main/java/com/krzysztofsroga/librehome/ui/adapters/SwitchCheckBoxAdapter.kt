package com.krzysztofsroga.librehome.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.LightSwitch
import kotlinx.android.synthetic.main.switch_check_entry.view.*
import kotlinx.android.synthetic.main.switch_entry.view.*
import kotlinx.android.synthetic.main.switch_entry.view.lightIcon

//TODO pass livedata?
class SwitchCheckBoxAdapter(private var lightSwitchList: List<LightSwitch>, private var selected: MutableSet<Int>) : RecyclerView.Adapter<SwitchCheckBoxAdapter.SwitchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.switch_check_entry, parent, false)
        return SwitchViewHolder(itemView)
    }

    override fun getItemCount(): Int = lightSwitchList.size

    override fun onBindViewHolder(holder: SwitchViewHolder, position: Int) {
        val switch = lightSwitchList[position]
        holder.loadSwitch(switch, switch.id in selected) { isChecked ->
            when (isChecked) {
                true -> selected.add(switch.id!!) //TODO id nullability
                false -> selected.remove(switch.id!!)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (lightSwitchList[position]) {
            is LightSwitch.SimpleSwitch -> 1
            is LightSwitch.DimmableSwitch -> 2
            is LightSwitch.SelectorSwitch -> 3
            is LightSwitch.PushButtonSwitch -> 4
            is LightSwitch.PercentageSwitch -> 5
            is LightSwitch.UnsupportedSwitch -> 6
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
            icon.setImageResource(lightSwitch.icon)

            switch.setOnClickListener {
                onCheckedChange(switch.isChecked)
            }
        }
    }
}