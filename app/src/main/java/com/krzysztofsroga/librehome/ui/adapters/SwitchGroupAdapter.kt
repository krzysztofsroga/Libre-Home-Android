package com.krzysztofsroga.librehome.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.LhComponent
import com.krzysztofsroga.librehome.models.LightSwitch
import com.krzysztofsroga.librehome.models.SwitchGroup
import com.krzysztofsroga.librehome.utils.isEven
import kotlinx.android.synthetic.main.switch_group_entry.view.*
import java.io.File

class SwitchGroupAdapter(
    private val switchGroupList: List<SwitchGroup>,
    private val switches: List<LhComponent>,
    private val onItemClick: (SwitchGroup) -> Unit = { _ -> },
    private val onAddGroupClick: () -> Unit = {}
) : RecyclerView.Adapter<SwitchGroupAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return if (viewType == 1) {
            GroupViewHolder.AddGroup(LayoutInflater.from(parent.context).inflate(R.layout.add_group_entry, parent, false))
        } else {
            GroupViewHolder.Group(LayoutInflater.from(parent.context).inflate(R.layout.switch_group_entry, parent, false))
        }
    }

    override fun getItemCount() = switchGroupList.size + 1

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        if (position.isEven()) {
            holder.layout.removeView(holder.photo)
            holder.layout.addView(holder.photo)
        }
        when (holder) {
            is GroupViewHolder.Group -> {
                val switchGroup = switchGroupList[position]
                holder.card.setOnClickListener {
                    onItemClick(switchGroup)
                }
                holder.description.text = switchGroup.description
                holder.name.text = switchGroup.name
                Glide.with(holder.card.context).load(File(switchGroup.imagePath)).into(holder.photo)
                holder.switchList.text = switchGroup.switchesIndices.map { id -> switches.find { it.id == id }?.name ?: id }.joinToString(", ")
            }
            is GroupViewHolder.AddGroup -> {
                Glide.with(holder.card.context).load(R.drawable.plus_in_circle_inv).into(holder.photo)
                holder.card.setOnClickListener {
                    onAddGroupClick()
                }
            }
        }
    }

    sealed class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: LinearLayout = view.group_entry_linear_layout as LinearLayout
        val card: CardView = view as CardView
        val photo: ImageView = view.group_entry_photo

        class AddGroup(view: View) : GroupViewHolder(view)
        class Group(view: View) : GroupViewHolder(view) {
            val name: TextView = view.group_entry_name
            val description: TextView = view.group_entry_description
            val switchList: TextView = view.group_entry_switch_list
        }
    }

    override fun getItemViewType(position: Int) = if (position == switchGroupList.size) 1 else 0

}
