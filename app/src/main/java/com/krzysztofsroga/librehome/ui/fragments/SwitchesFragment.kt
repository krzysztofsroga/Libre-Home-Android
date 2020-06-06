package com.krzysztofsroga.librehome.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.LhDevice
import com.krzysztofsroga.librehome.ui.adapters.ComponentListAdapter
import com.krzysztofsroga.librehome.utils.getCurrentOrientationLayoutManager
import com.krzysztofsroga.librehome.viewmodels.SshViewModel
import com.krzysztofsroga.librehome.viewmodels.SwitchesViewModel
import kotlinx.android.synthetic.main.switches_fragment.*


class SwitchesFragment : Fragment() {
    private val switchesViewModel: SwitchesViewModel by activityViewModels()

    private val sshViewModel: SshViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.switches_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun promptRestartRaspberry() {
            AlertDialog.Builder(requireActivity()).apply {
                setMessage(getString(R.string.restart_confirmation))
                setPositiveButton(getString(R.string.restart)) { _, _ ->
                    sshViewModel.restartRaspberry()
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                create().show()
            }
        }

        //ToDo how to add the SSH buttons to favs?
        button_check_ssh_connection.setOnClickListener {
            sshViewModel.checkConnection()
        }

        button_ssh_restart.setOnClickListener {
            promptRestartRaspberry()
        }

        //ToDo Bug: when out was present one time, then it shown every time when activating the fragment. Is there a obeserveOnce?
        sshViewModel.out.observe(viewLifecycleOwner, Observer { out ->
            Toast.makeText(context, out, Toast.LENGTH_LONG).show()
        })

        switches_list.apply {
            layoutManager = getCurrentOrientationLayoutManager()
            setHasFixedSize(true)
            adapter = ComponentListAdapter(listOf(), {
                switchesViewModel.sendComponentState(it)
            }, {
                Toast.makeText(context, context.getString(R.string.add_fav, it.name), Toast.LENGTH_SHORT).show()
                switchesViewModel.addFavorite(it)
            }).apply { setHasStableIds(true) }
        }

        switchesViewModel.switches.observe(viewLifecycleOwner, Observer { switches ->
            (switches_list.adapter as ComponentListAdapter).updateData(switches.filterIsInstance<LhDevice>())
        })

    }
}
