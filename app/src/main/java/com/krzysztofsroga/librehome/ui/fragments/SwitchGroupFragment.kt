package com.krzysztofsroga.librehome.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.SwitchGroup
import com.krzysztofsroga.librehome.ui.activities.NewGroupActivity
import com.krzysztofsroga.librehome.ui.adapters.SwitchGroupAdapter
import com.krzysztofsroga.librehome.utils.getCurrentOrientationLayoutManager
import com.krzysztofsroga.librehome.viewmodels.SwitchGroupViewModel
import com.krzysztofsroga.librehome.viewmodels.SwitchesViewModel
import kotlinx.android.synthetic.main.switch_group_fragment.*

class SwitchGroupFragment : Fragment() {

    private val switchGroupViewModel: SwitchGroupViewModel by activityViewModels()
    private val switchesViewModel: SwitchesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.switch_group_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        switch_group_list.apply {
            layoutManager = getCurrentOrientationLayoutManager()
            setHasFixedSize(true)
        }

        switchGroupViewModel.switchGroups.observe(viewLifecycleOwner, Observer { groups ->
            switchesViewModel.switches.observe(viewLifecycleOwner, Observer { switches ->
                //TODO think about whether nested observers is a good pattern
                switch_group_list.adapter = SwitchGroupAdapter(groups, switches,
                    onItemClick = { switchGroup ->
                        showSwitchGroup(switchGroup)
                    },
                    onAddGroupClick = {
                        startActivity(Intent(activity, NewGroupActivity::class.java))
                    })
            })
        })


    }

    private fun showSwitchGroup(group: SwitchGroup) {
        val action = SwitchGroupFragmentDirections.actionGroupsToSelectedGroup(group.id)
        findNavController().navigate(action)
    }
}
