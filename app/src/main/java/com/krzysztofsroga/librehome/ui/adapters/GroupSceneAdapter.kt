package com.krzysztofsroga.librehome.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.LhGroupScene
import kotlinx.android.synthetic.main.switch_entry.view.*

class GroupSceneAdapter(private var groupSceneList: List<LhGroupScene>, private val callback: (LhGroupScene) -> Unit, private val longCallback: (LhGroupScene) -> Unit) :
    RecyclerView.Adapter<GroupSceneAdapter.GroupSceneViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupSceneViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.switch_entry, parent, false)
        return GroupSceneViewHolder(itemView)
    }

    override fun getItemCount(): Int = groupSceneList.size

    override fun onBindViewHolder(holder: GroupSceneViewHolder, position: Int) {
        holder.loadSwitch(groupSceneList[position], callback, longCallback)
    }

    override fun getItemViewType(position: Int): Int {
        return when (groupSceneList[position]) {
            is LhGroupScene.LhGroup -> 1
            is LhGroupScene.LhScene -> 2
            is LhGroupScene.LhUnsupportedGroupScene -> 3
        }
    }

    override fun getItemId(position: Int): Long {
        return groupSceneList[position].id!!.toLong() //TODO make id non nullable
    }

    fun updateData(newGroupSceneList: List<LhGroupScene>) {
        groupSceneList = newGroupSceneList
        notifyDataSetChanged()
    }

    class GroupSceneViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val switch: Switch = view.switchName
        //        private val seekBar: SeekBar = view.switchSeekBar
//        private val spinner: Spinner = view.switchSpinner
        private val icon: ImageView = view.lightIcon
        private val unsupportedLayout = view.unsupported_layout
        private val unsupportedName = view.unsupported_name

        fun loadSwitch(groupScene: LhGroupScene, callback: (LhGroupScene) -> Unit, longCallback: (LhGroupScene) -> Unit) {
            switch.text = groupScene.name
            if (groupScene is LhGroupScene.LhGroup)
                switch.isChecked = groupScene.enabled
            icon.setImageResource(groupScene.icon)
            if (groupScene is LhGroupScene.LhUnsupportedGroupScene) {
                unsupportedLayout.visibility = View.VISIBLE
                unsupportedName.text = groupScene.typeName ?: "null"
            }

            switch.setOnClickListener {
                if (groupScene is LhGroupScene.LhGroup)
                    groupScene.enabled = switch.isChecked
                callback(groupScene)
            }
            switch.setOnLongClickListener {
                longCallback(groupScene)
                true
            }

        }
    }
}