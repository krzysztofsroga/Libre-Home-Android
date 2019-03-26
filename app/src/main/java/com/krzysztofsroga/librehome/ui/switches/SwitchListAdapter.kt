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

class SwitchListAdapter(private val lightSwitchList: List<LightSwitch>, private val callback: (LightSwitch) -> Unit) :
    RecyclerView.Adapter<SwitchListAdapter.SwitchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.switch_entry, parent, false)
        return SwitchViewHolder(itemView)
    }

    override fun getItemCount(): Int = lightSwitchList.size

    override fun onBindViewHolder(holder: SwitchViewHolder, position: Int) {
        val lightSwitch = lightSwitchList[position]
        holder.switch.text = lightSwitch.name
        holder.switch.isChecked = lightSwitch.enabled
        holder.seekBar.visibility = if (lightSwitch is LightSwitch.DimmableSwitch) {
            holder.switch.setOnCheckedChangeListener { _, isChecked ->
                holder.seekBar.isEnabled = isChecked
            }
            holder.seekBar.isEnabled = lightSwitch.enabled
            holder.seekBar.progress = lightSwitch.dim
            holder.icon.setImageResource(R.drawable.light_dim)
            View.VISIBLE
        } else View.GONE
        holder.switch.setOnClickListener {
            lightSwitch.enabled = holder.switch.isChecked //TODO setOnCheckedChangeListener
            callback(lightSwitch)
        }
        holder.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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


    class SwitchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val switch: Switch = view.switchName
        val seekBar: SeekBar = view.switchSeekBar
        val icon: ImageView = view.lightIcon
    }
}