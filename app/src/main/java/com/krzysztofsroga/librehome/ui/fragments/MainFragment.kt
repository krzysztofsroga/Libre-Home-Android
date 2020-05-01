package com.krzysztofsroga.librehome.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.adapters.ComponentListAdapter
import com.krzysztofsroga.librehome.utils.getCurrentOrientationLayoutManager
import com.krzysztofsroga.librehome.viewmodels.SshViewModel
import com.krzysztofsroga.librehome.viewmodels.SwitchesViewModel
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {

    private val sshViewModel: SshViewModel by activityViewModels()

    private val switchesViewModel: SwitchesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_check_ssh_connection.setOnClickListener {
            sshViewModel.checkConnection()
        }

        button_ssh_restart.setOnClickListener {
            promptRestartRaspberry()
        }

        switches_favorite_list.apply {
            layoutManager = getCurrentOrientationLayoutManager()
            adapter = ComponentListAdapter(listOf(), {
                switchesViewModel.sendComponentState(it)
            }, {
                switchesViewModel.removeFavorite(it)
            }).apply { setHasStableIds(true) }
        }

        scene_list.apply {
            layoutManager = getCurrentOrientationLayoutManager()
            adapter = ComponentListAdapter(listOf(), {
                switchesViewModel.sendComponentState(it)
            }, {}).apply { setHasStableIds(true) }
        }

        switchesViewModel.favoriteSwitches.observe(viewLifecycleOwner, Observer { favs ->
            (switches_favorite_list.adapter as ComponentListAdapter).updateData(favs)
        })

        switchesViewModel.groupScenes.observe(viewLifecycleOwner, Observer { groupScenes ->
            (scene_list.adapter as ComponentListAdapter).updateData(groupScenes)
        })

        sshViewModel.out.observe(viewLifecycleOwner, Observer { out ->
            ssh_out.text = out
        })
    }

    private fun promptRestartRaspberry() {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(getString(R.string.restart_confirmation))
            setPositiveButton(getString(R.string.restart)) { _, _ ->
                sshViewModel.restartRaspberry()
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            create().show()
        }
    }

}
