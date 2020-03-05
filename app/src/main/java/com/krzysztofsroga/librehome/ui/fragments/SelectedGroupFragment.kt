package com.krzysztofsroga.librehome.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.krzysztofsroga.librehome.AppConfig

import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.activities.NewGroupActivity
import com.krzysztofsroga.librehome.ui.adapters.SwitchListAdapter
import com.krzysztofsroga.librehome.utils.getCurrentOrientationLayoutManager
import com.krzysztofsroga.librehome.viewmodels.SwitchGroupViewModel
import com.krzysztofsroga.librehome.viewmodels.SwitchesViewModel
import kotlinx.android.synthetic.main.selected_group_fragment.*
import kotlinx.android.synthetic.main.selected_group_fragment.switches_list
import java.io.File

class SelectedGroupFragment : Fragment() {

    private val args: SelectedGroupFragmentArgs by navArgs()
    private val groupsViewModel: SwitchGroupViewModel by viewModels()
    private val switchesViewModel: SwitchesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.selected_group_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_group_delete.setOnClickListener {
            groupsViewModel.switchGroups.observe(viewLifecycleOwner, Observer {
                val toDelete = it.find { it.id == args.selectedGroupId } //TODO findGroupById
                if (toDelete != null) {
                    groupsViewModel.deleteGroup(toDelete)
                    File(toDelete.imagePath).delete()
                    requireActivity().onBackPressed()
                }
            })
        }

        button_group_edit.setOnClickListener {
            startActivity(Intent(activity, NewGroupActivity::class.java).apply { putExtra(AppConfig.ExtrasKeys.GROUP_ID, args.selectedGroupId) })
        }

        switches_list.apply {
            layoutManager = getCurrentOrientationLayoutManager()
            setHasFixedSize(true)
            adapter = SwitchListAdapter(listOf(), {
                switchesViewModel.sendSwitchState(it)
            }, {
                Toast.makeText(context, "Switch '${it.name}' is added to favorites!", Toast.LENGTH_SHORT).show() //TODO string template
                switchesViewModel.addFavorite(it)
            }).apply { setHasStableIds(true) }
        }

        switchesViewModel.switches.observe(viewLifecycleOwner, Observer { switches ->
            groupsViewModel.switchGroups.observe(viewLifecycleOwner, Observer { groups ->
                //TODO think about whether nested observers is a good pattern. Maybe transformations as it is solved with favorites?
                val group = groups.find { it.id == args.selectedGroupId }!! //TODO findGroupById
                (switches_list.adapter as SwitchListAdapter).updateData(switches.filter { it.id in group.switchesIndices })
            })
        })
    }

}
