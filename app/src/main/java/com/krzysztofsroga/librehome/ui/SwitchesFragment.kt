package com.krzysztofsroga.librehome.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.adapters.SwitchListAdapter
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
            adapter = SwitchListAdapter(listOf(), {
                switchesViewModel.sendSwitchState(it)
            }, {
                Toast.makeText(context, "Switch '${it.name}' is added to favorites!", Toast.LENGTH_SHORT).show()
                switchesViewModel.addFavorite(it)
            }).apply { setHasStableIds(true) }
        }

        switchesViewModel.switches.observe(viewLifecycleOwner, Observer { switches ->
            (switches_list.adapter as SwitchListAdapter).updateData(switches)
        })

    }

}
