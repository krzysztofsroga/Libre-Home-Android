package com.krzysztofsroga.librehome.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.LhComponent
import kotlinx.android.synthetic.main.switch_check_entry.view.*
import kotlinx.android.synthetic.main.switch_entry.view.lightIcon

//TODO pass livedata?
class SwitchCheckBoxAdapter(private var lightSwitchList: List<LhComponent>, private var selected: MutableSet<Int>) : RecyclerView.Adapter<SwitchCheckBoxAdapter.SwitchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.switch_check_entry, parent, false)
        return SwitchViewHolder(itemView)
    }

    override fun getItemCount(): Int = lightSwitchList.size

    override fun onBindViewHolder(holder: SwitchViewHolder, position: Int) {
        val component = lightSwitchList[position]
        holder.loadSwitch(component, component.id in selected) { isChecked ->
            when (isChecked) {
                true -> selected.add(component.id)
                false -> selected.remove(component.id)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = lightSwitchList[position]::class.simpleName.hashCode()

    override fun getItemId(position: Int): Long {
        return lightSwitchList[position].id.toLong()
    }

    fun updateData(newLightSwitchList: List<LhComponent>) {
        lightSwitchList = newLightSwitchList
        notifyDataSetChanged()
    }

    class SwitchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val switch: CheckBox = view.switchCheckBox
        private val icon: ImageView = view.lightIcon

        fun loadSwitch(lightSwitch: LhComponent, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
            switch.text = lightSwitch.name
            switch.isChecked = checked
            icon.setImageResource(lightSwitch.icon)

            switch.setOnClickListener {
                onCheckedChange(switch.isChecked)
            }
        }
    }
}