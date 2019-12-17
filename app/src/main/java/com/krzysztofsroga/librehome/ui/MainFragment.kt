package com.krzysztofsroga.librehome.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.switches.SwitchListAdapter
import com.krzysztofsroga.librehome.viewmodels.SwitchesViewModel
import com.krzysztofsroga.librehome.viewmodels.SshViewModel
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {
    companion object :
        MainActivityFragmentFactory<MainFragment> {
        override fun newInstance() = MainFragment()
        override val name: String
            get() = "Main screen"
    }

    private lateinit var sshViewModel: SshViewModel
    private lateinit var switchesViewModel: SwitchesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sshViewModel = ViewModelProvider(requireActivity())[SshViewModel::class.java]
        switchesViewModel = ViewModelProvider(requireActivity())[SwitchesViewModel::class.java]

        button_check_ssh_connection.setOnClickListener {
            sshViewModel.checkConnection()
        }

        button_ssh_restart.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setMessage(getString(R.string.restart_confirmation))
                setPositiveButton(getString(R.string.restart)) { _, _ ->
                    sshViewModel.restartRaspberry()
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                create().show()
            }
        }

        sshViewModel.out.observe(viewLifecycleOwner, Observer { out ->
            ssh_out.text = out
        })

        switchesViewModel.favoriteSwitches.observe(viewLifecycleOwner, Observer { favs ->
            switches_favorite_list.apply {
                layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2)
                } else {
                    LinearLayoutManager(context)
                }
                setHasFixedSize(true)
                adapter = SwitchListAdapter(favs, {
                    switchesViewModel.sendSwitchState(it)
                }, {
                    switchesViewModel.removeFavorite(it)
                }).apply { setHasStableIds(true) }
            }
        })
    }


}
