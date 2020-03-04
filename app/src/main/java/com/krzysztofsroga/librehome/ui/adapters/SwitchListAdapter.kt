package com.krzysztofsroga.librehome.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.LhComponent
import com.krzysztofsroga.librehome.models.LhDevice
import com.krzysztofsroga.librehome.models.LightSwitch
import kotlinx.android.synthetic.main.switch_entry.view.*
import kotlin.math.min

//TODO pass livedata?
class SwitchListAdapter(private var lightSwitchList: List<LhComponent>, private val callback: (LhComponent) -> Unit, private val longCallback: (LhComponent) -> Unit) :
    RecyclerView.Adapter<SwitchListAdapter.SwitchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.switch_entry, parent, false)
        return SwitchViewHolder(itemView)
    }

    override fun getItemCount(): Int = lightSwitchList.size

    override fun onBindViewHolder(holder: SwitchViewHolder, position: Int) {
        holder.loadSwitch(lightSwitchList[position], callback, longCallback)
    }

    override fun getItemViewType(position: Int): Int=lightSwitchList[position]::class.simpleName.hashCode()

    override fun getItemId(position: Int): Long {
        return lightSwitchList[position].id.toLong()
    }

    fun updateData(newLightSwitchList: List<LhComponent>) {
        lightSwitchList = newLightSwitchList
        notifyDataSetChanged()
    }

    class SwitchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val switch: Switch = view.switchName
        private val seekBar: SeekBar = view.switchSeekBar
        private val spinner: Spinner = view.switchSpinner
        private val icon: ImageView = view.lightIcon
        private val unsupportedLayout = view.unsupported_layout
        private val unsupportedName = view.unsupported_name
        private val button = view.push_button

        fun loadSwitch(lightSwitch: LhComponent, callback: (LhComponent) -> Unit, longCallback: (LhComponent) -> Unit) {
            if(lightSwitch is LhDevice.LhBlindsPercentage) {
                switch.isEnabled = false //TODO fully support Percentage Switches
            }
            icon.setImageResource(lightSwitch.icon)
            seekBar.visibility = if (lightSwitch is LhDevice.LhDimmableSwitch) {
                switch.setOnCheckedChangeListener { _, isChecked ->
                    seekBar.isEnabled = isChecked
                }
                seekBar.isEnabled = lightSwitch.enabled
                seekBar.progress = lightSwitch.dim
                View.VISIBLE
            } else View.GONE
            spinner.visibility = if (lightSwitch is LhDevice.LhSelectorSwitch) {
                spinner.adapter = ArrayAdapter(spinner.context, R.layout.support_simple_spinner_dropdown_item, lightSwitch.levels)
                spinner.onItemSelectedListener = null
                spinner.setSelection(min(lightSwitch.selectedId, lightSwitch.levels.lastIndex), false)
                switch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked && lightSwitch.selectedId == 0) switch.isChecked = false
                }
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    var c = 0
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (c++ >= 1) {
                            lightSwitch.selectedId = position
                            switch.isChecked = (position != 0)
                            lightSwitch.enabled = switch.isChecked
                            callback(lightSwitch)
                        }
                    }
                }
                View.VISIBLE
            } else View.GONE
            if(lightSwitch is LhDevice.LhUnsupported) {
                unsupportedLayout.visibility = View.VISIBLE
                unsupportedName.text = lightSwitch.typeName ?: "null"
            }
            if(lightSwitch is LhDevice.LhPushButton) {
                switch.visibility = View.GONE
                button.visibility = View.VISIBLE
                button.text = lightSwitch.name
                button.setOnClickListener {
                    callback(lightSwitch)
                }
                button.setOnLongClickListener {
                    longCallback(lightSwitch)
                    true
                }
            } else if (lightSwitch is LhComponent.Switchable){
                switch.text = lightSwitch.name
                switch.isChecked = lightSwitch.enabled
                switch.setOnClickListener {
                    lightSwitch.enabled = switch.isChecked
                    callback(lightSwitch)
                }
                switch.setOnLongClickListener {
                    longCallback(lightSwitch)
                    true
                }
            }
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    callback(lightSwitch)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    (lightSwitch as LhComponent.Dimmable).dim = progress
                }

            })
        }
    }
}