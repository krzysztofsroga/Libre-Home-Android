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
import com.krzysztofsroga.librehome.models.LhSensor
import com.krzysztofsroga.librehome.ui.adapters.ComponentListAdapter
import com.krzysztofsroga.librehome.utils.getCurrentOrientationLayoutManager
import com.krzysztofsroga.librehome.viewmodels.SwitchesViewModel
import kotlinx.android.synthetic.main.sensors_fragment.*


class SensorsFragment : Fragment() {
    private val switchesViewModel: SwitchesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sensors_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sensors_list.apply {
            layoutManager = getCurrentOrientationLayoutManager()
            setHasFixedSize(true)
            adapter = ComponentListAdapter(listOf(), {
                switchesViewModel.sendComponentState(it)
            }, {
                Toast.makeText(context, context.getString(R.string.add_fav, it.name), Toast.LENGTH_SHORT).show()
                switchesViewModel.addFavorite(it)
            }, switchesViewModel.showAdditionalInfo).apply { setHasStableIds(true) }
        }

        switchesViewModel.switches.observe(viewLifecycleOwner, Observer { switches ->
            (sensors_list.adapter as ComponentListAdapter).updateData(switches.filterIsInstance<LhSensor>())
        })
    }
}
