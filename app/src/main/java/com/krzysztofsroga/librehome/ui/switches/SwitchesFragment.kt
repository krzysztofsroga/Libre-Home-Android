package com.krzysztofsroga.librehome.ui.switches

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.viewmodels.SwitchesViewModel
import kotlinx.android.synthetic.main.switches_fragment.*


class SwitchesFragment : Fragment() {
    private val viewModel: SwitchesViewModel by activityViewModels() //getting viewmodel in activity scope so that all fragments in pager have access to it

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.switches_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        switches_list.apply {
            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 2)
            } else {
                LinearLayoutManager(context)
            }
            setHasFixedSize(true)

        }

        viewModel.switches.observe(viewLifecycleOwner, Observer { switches ->
            switches_list.adapter = SwitchListAdapter(switches, {
                viewModel.sendSwitchState(it)
            }, {
                Toast.makeText(context, "Switch '${it.name}' is added to favorites!", Toast.LENGTH_SHORT).show()
                viewModel.addFavorite(it)
            }).apply {
                setHasStableIds(true)

            }

        })

    }

}
