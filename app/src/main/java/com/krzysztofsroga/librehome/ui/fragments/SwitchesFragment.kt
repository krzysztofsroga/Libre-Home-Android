package com.krzysztofsroga.librehome.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.adapters.ComponentListAdapter
import com.krzysztofsroga.librehome.utils.getCurrentOrientationLayoutManager
import com.krzysztofsroga.librehome.viewmodels.SwitchesViewModel
import kotlinx.android.synthetic.main.switches_fragment.*


class SwitchesFragment : Fragment() {
    private val switchesViewModel: SwitchesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.switches_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        switches_list.apply {
            layoutManager = getCurrentOrientationLayoutManager()
            setHasFixedSize(true)
            adapter = ComponentListAdapter(listOf(), {
                switchesViewModel.sendComponentState(it)
            }, {
                Toast.makeText(context, "Switch '${it.name}' is added to favorites!", Toast.LENGTH_SHORT).show()
                switchesViewModel.addFavorite(it)
            }).apply { setHasStableIds(true) }
        }

        switchesViewModel.switches.observe(viewLifecycleOwner, Observer { switches ->
            (switches_list.adapter as ComponentListAdapter).updateData(switches)
        })

    }

}
