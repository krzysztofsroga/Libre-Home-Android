package com.krzysztofsroga.librehome.ui.switches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.switches_fragment.*


class SwitchesFragment : Fragment() {

    companion object {
        fun newInstance() = SwitchesFragment()
    }

    private lateinit var viewModel: SwitchesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO databinding
        return inflater.inflate(R.layout.switches_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SwitchesViewModel::class.java]

        initializeList()

    }

    private fun initializeList() {
        Log.d("initialization", "initializing list")
        viewModel.switches.observe({ lifecycle }) { t: List<LightSwitch>? ->
            t?.let {
                switches_list.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = SwitchListAdapter(it) {
                        viewModel.sendSwitchState(it)
                    }.apply { setHasStableIds(true) }


                }
            }
        }

        viewModel.switches.observe(viewLifecycleOwner, Observer{

        })

    }

}

//activity!!.runOnUiThread {
//    Log.d("initialization", "ui list")
//    switches_list.apply {
//        setHasFixedSize(true)
//        layoutManager = LinearLayoutManager(context)
//        adapter = SwitchListAdapter(downloadedSwitches) {
//            switches.sendSwitchState(it)
//        }.apply { setHasStableIds(true) }
//
//    }
//
//}