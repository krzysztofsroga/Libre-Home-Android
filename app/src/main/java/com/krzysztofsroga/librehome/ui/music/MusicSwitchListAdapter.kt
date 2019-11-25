package com.krzysztofsroga.librehome.ui.music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.switches.LightSwitch
import kotlinx.android.synthetic.main.music_led_entry.view.*

class MusicSwitchListAdapter(
    private val lightSwitchList: List<LightSwitch.DimmableSwitch>,
    private val callback: (LightSwitch.DimmableSwitch, Boolean) -> Unit
) :
    RecyclerView.Adapter<MusicSwitchListAdapter.MusicSwitchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicSwitchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.music_led_entry, parent, false)
        return MusicSwitchViewHolder(itemView)
    }

    override fun getItemCount(): Int = lightSwitchList.size

    override fun onBindViewHolder(holder: MusicSwitchViewHolder, position: Int) {
        val lightSwitch = lightSwitchList[position]
        holder.switch.text = "${lightSwitch.name}($position)"
        holder.switch.isChecked = lightSwitch.enabled

        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            callback(lightSwitch, isChecked)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position //TODO return 0 for simple, 1 for dimmable
    }


    class MusicSwitchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val switch: Switch = view.musicSwitchName
        val seekBar: SeekBar = view.musicSwitchSeekBar
        val icon: ImageView = view.musicLightIcon
    }
}