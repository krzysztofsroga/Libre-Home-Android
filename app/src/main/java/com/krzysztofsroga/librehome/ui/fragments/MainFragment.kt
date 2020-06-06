package com.krzysztofsroga.librehome.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.adapters.ComponentListAdapter
import com.krzysztofsroga.librehome.utils.getCurrentOrientationLayoutManager
import com.krzysztofsroga.librehome.viewmodels.SwitchesViewModel
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {

    private val switchesViewModel: SwitchesViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //ToDo if no favs are available display a msg stating how to add devices to favs.
        switches_favorite_list.apply {
            layoutManager = getCurrentOrientationLayoutManager()
            adapter = ComponentListAdapter(listOf(), {
                switchesViewModel.sendComponentState(it)
            }, {
                switchesViewModel.removeFavorite(it)
            }, switchesViewModel.showAdditionalInfo).apply { setHasStableIds(true) }
        }

        switchesViewModel.favoriteSwitches.observe(viewLifecycleOwner, Observer { favs ->
            (switches_favorite_list.adapter as ComponentListAdapter).updateData(favs)
        })

    }

}
