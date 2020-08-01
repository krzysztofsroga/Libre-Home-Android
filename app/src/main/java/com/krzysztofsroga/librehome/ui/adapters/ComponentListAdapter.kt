package com.krzysztofsroga.librehome.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.LhComponent
import com.krzysztofsroga.librehome.models.LhDevice
import kotlinx.android.synthetic.main.switch_entry.view.*
import kotlin.math.min

//TODO pass livedata?
class ComponentListAdapter(private var componentList: List<LhComponent>, private val callback: (LhComponent) -> Unit, private val longCallback: (LhComponent) -> Unit, private val showAddInfo: Boolean = false) :
    RecyclerView.Adapter<ComponentListAdapter.ComponentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.switch_entry, parent, false)
        return ComponentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        holder.loadSwitch(componentList[position], callback, longCallback, showAddInfo)
    }

    override fun getItemCount(): Int = componentList.size

    override fun getItemViewType(position: Int): Int = componentList[position]::class.simpleName.hashCode()

    override fun getItemId(position: Int): Long = componentList[position].id.toLong()

    fun updateData(newList: List<LhComponent>) {
        componentList = newList
        notifyDataSetChanged()
    }

    class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val switch: Switch = view.switchName
        private val seekBar: SeekBar = view.switchSeekBar
        private val spinner: Spinner = view.switchSpinner
        private val icon: ImageView = view.lightIcon
        private val simpleName: TextView = view.simple_name
        private val unsupportedLayout = view.unsupported_layout
        private val unsupportedName = view.unsupported_name
        private val button = view.push_button
        private val stateText = view.state_text
        private val switchLayout = view.switch_layout
        private val additionalData = view.additional_data
        private val additionalInfo = view.additional_info


        fun loadSwitch(component: LhComponent, callback: (LhComponent) -> Unit, longCallback: (LhComponent) -> Unit, showAddInfo: Boolean) {
            icon.setImageResource(component.icon)

            if (component is LhComponent.Dimmable) {
                seekBar.visibility = View.VISIBLE
                switch.setOnCheckedChangeListener { _, isChecked ->
                    seekBar.isEnabled = isChecked
                }
                seekBar.isEnabled = component !is LhComponent.Switchable || component.enabled //TODO move logic to isSwitchable
                seekBar.progress = component.dim
                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        callback(component)
                    }

                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        component.dim = progress
                    }
                })
            }

            if (component is LhDevice.LhSelectorSwitch) { //TODO check for interface, not specific type
                spinner.visibility = View.VISIBLE
                spinner.adapter = ArrayAdapter(spinner.context, R.layout.support_simple_spinner_dropdown_item, component.levels)
                spinner.onItemSelectedListener = null
                spinner.setSelection(min(component.selectedId, component.levels.lastIndex), false)
                switch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked && component.selectedId == 0) switch.isChecked = false
                }
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    var c = 1 //TODO 0 works for me, 1 works on other devices(c can be deleted as well)...
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (c++ >= 1) {
                            component.selectedId = position
                            switch.isChecked = (position != 0)
                            component.enabled = switch.isChecked
                            callback(component)
                        }
                    }
                }
            }

            if (component is LhComponent.Unsupported) {
                simpleName.visibility = View.VISIBLE
                simpleName.text = component.name //TODO To be able to figure out which device is not supported.
                unsupportedLayout.visibility = View.VISIBLE
                unsupportedName.text = component.typeName ?: "null"
            }

            if (component is LhComponent.SimpleName) { // TODO generate SimpleName automatically?
                simpleName.visibility = View.VISIBLE
                simpleName.text = component.name
            }

            if (component is LhComponent.Switchable) {
                switch.visibility = View.VISIBLE
                switch.text = component.name
                switch.isChecked = component.enabled
                switch.setOnClickListener {
                    component.enabled = switch.isChecked //TODO maybe move this logic to callback?
                    callback(component)
                }
                switch.setOnLongClickListener {
                    longCallback(component)
                    true
                }
            }


            if (component is LhComponent.SensorDataUpdate) {
                stateText.visibility = View.VISIBLE
                stateText.text = component.state
                if (showAddInfo) {
                        additionalInfo.visibility = View.VISIBLE
                        additionalInfo.text = switch.context.getString(R.string.last_update_time)
                        additionalData.visibility = View.VISIBLE
                        additionalData.text = component.update
                    }
            }

            if (component is LhComponent.SensorData) {
                stateText.visibility = View.VISIBLE
                stateText.text = component.state
            }

            if (component is LhComponent.HasButton) {
                button.visibility = View.VISIBLE
                button.text = component.name
                button.setOnClickListener {
                    callback(component)
                }
                switch.setOnLongClickListener {
                    longCallback(component)
                    true
                }
            }

            switchLayout.setOnLongClickListener {
                longCallback(component)
                true
            }
        }
    }
}