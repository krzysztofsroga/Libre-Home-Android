package com.krzysztofsroga.librehome.ui.switches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.switch_entry.view.*

class SwitchListAdapter(private val lightSwitchList: List<LightSwitch>, private val callback: (LightSwitch) -> Unit, private val longCallback: (LightSwitch) -> Unit) :
    RecyclerView.Adapter<SwitchListAdapter.SwitchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.switch_entry, parent, false)
        return SwitchViewHolder(itemView)
    }

    override fun getItemCount(): Int = lightSwitchList.size

    override fun onBindViewHolder(holder: SwitchViewHolder, position: Int) {
        holder.initialize(lightSwitchList[position], callback, longCallback)
    }

    override fun getItemViewType(position: Int): Int {
        return position //TODO return 0 for simple, 1 for dimmable
    }


    class SwitchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val switch: Switch = view.switchName
        private val seekBar: SeekBar = view.switchSeekBar
        private val icon: ImageView = view.lightIcon

        fun initialize(lightSwitch: LightSwitch, callback: (LightSwitch) -> Unit, longCallback: (LightSwitch) -> Unit) {
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