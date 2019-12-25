package com.krzysztofsroga.librehome.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs

import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.viewmodels.SelectedGroupViewModel
import com.krzysztofsroga.librehome.viewmodels.SwitchGroupViewModel
import kotlinx.android.synthetic.main.selected_group_fragment.*

class SelectedGroupFragment : Fragment() {

    private val args: SelectedGroupFragmentArgs by navArgs()
    private val viewModel: SelectedGroupViewModel by viewModels()
    private val groupsViewModel: SwitchGroupViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.selected_group_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_group_delete.text = "Delete ${args.myArg}" //TODO res
        button_group_delete.setOnClickListener {
//            findNavController().navigate(R.id.action_SelectedGroupFragment_to_GroupsFragment)
            groupsViewModel.switchGroups.observe(viewLifecycleOwner, Observer {
                Log.d("u", it.toString())
                val toDelete = it.find { it.name == args.myArg }
                Log.d("u2", toDelete.toString())
                if (toDelete != null) {
                    groupsViewModel.deleteGroup(toDelete)
                    requireActivity().onBackPressed()
                }
            })

//            groupsViewModel.deleteGroup(groupsViewModel.switchGroups.value!!.find { it.name == args.myArg }!!)
        }
    }


}
