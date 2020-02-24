package com.krzysztofsroga.librehome.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.LightSwitch
import kotlinx.android.synthetic.main.switch_entry.view.*
import kotlin.math.max
import kotlin.math.min

//TODO pass livedata?
class SwitchListAdapter(private var lightSwitchList: List<LightSwitch>, private val callback: (LightSwitch) -> Unit, private val longCallback: (LightSwitch) -> Unit) :
    RecyclerView.Adapter<SwitchListAdapter.SwitchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.switch_entry, parent, false)
        return SwitchViewHolder(itemView)
    }

    override fun getItemCount(): Int = lightSwitchList.size

    override fun onBindViewHolder(holder: SwitchViewHolder, position: Int) {
        holder.loadSwitch(lightSwitchList[position], callback, longCallback)
    }

    override fun getItemViewType(position: Int): Int {
        return when (lightSwitchList[position]) {
            is LightSwitch.SimpleSwitch -> 1
            is LightSwitch.DimmableSwitch -> 2
            is LightSwitch.SelectorSwitch -> 3
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
        private val switch: Switch = view.switchName
        private val seekBar: SeekBar = view.switchSeekBar
        private val spinner: Spinner = view.switchSpinner
        private val icon: ImageView = view.lightIcon

        fun loadSwitch(lightSwitch: LightSwitch, callback: (LightSwitch) -> Unit, longCallback: (LightSwitch) -> Unit) {
            switch.text = lightSwitch.name
            switch.isChecked = lightSwitch.enabled
            seekBar.visibility = if (lightSwitch is LightSwitch.DimmableSwitch) {
                switch.setOnCheckedChangeListener { _, isChecked ->
                    seekBar.isEnabled = isChecked
                }
                seekBar.isEnabled = lightSwitch.enabled
                seekBar.progress = lightSwitch.dim
                icon.setImageResource(R.drawable.light_dim)
                View.VISIBLE
            } else View.GONE
            spinner.visibility = if (lightSwitch is LightSwitch.SelectorSwitch) {
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
                icon.setImageResource(R.drawable.light_dim)
                View.VISIBLE
            } else View.GONE
            switch.setOnClickListener {
                lightSwitch.enabled = switch.isChecked //TODO setOnCheckedChangeListener
                callback(lightSwitch)
            }
            switch.setOnLongClickListener {
                longCallback(lightSwitch)
                true
            }
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    callback(lightSwitch)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    (lightSwitch as LightSwitch.DimmableSwitch).dim = progress
                }

            })
        }
    }
}