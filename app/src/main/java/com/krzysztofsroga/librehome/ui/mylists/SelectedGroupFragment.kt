package com.krzysztofsroga.librehome.ui.mylists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.viewmodels.SelectedGroupViewModel
import kotlinx.android.synthetic.main.selected_group_fragment.*

class SelectedGroupFragment : Fragment() {

    private val args: SelectedGroupFragmentArgs by navArgs()
    private val viewModel: SelectedGroupViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.selected_group_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        name_button.text = args.myArg
        name_button.setOnClickListener {
            findNavController().navigate(R.id.action_SelectedGroupFragment_to_GroupsFragment)
        }
    }

}
