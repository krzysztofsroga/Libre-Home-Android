package com.krzysztofsroga.librehome.ui.Switches

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.switches_fragment.*


class SwitchesFragment : Fragment() {
    val switches: OnlineSwitches = OnlineSwitches() //TODO move to viewmodel

    companion object {
        fun newInstance() = SwitchesFragment()
    }

    private lateinit var viewModel: SwitchesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.switches_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SwitchesViewModel::class.java)


        switches.initialize()
        initializeList()


    }

    fun initializeList() {
        Log.d("initialization", "initializing list")
        switches.getAllSwitches { downloadedSwitches ->
            Log.d("initialization", "callback list")

            activity!!.runOnUiThread {
                Log.d("initialization", "ui list")
                switches_list.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = SwitchListAdapter(downloadedSwitches).apply { setHasStableIds(true) }
                }

            }
        }
    }

}
