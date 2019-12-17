package com.krzysztofsroga.librehome.ui.switches

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.krzysztofsroga.librehome.ui.MainActivityFragmentFactory
import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.switches_fragment.*


class SwitchesFragment : Fragment() {

    companion object:
        MainActivityFragmentFactory<SwitchesFragment> {
        override fun newInstance() = SwitchesFragment()
        override val name: String
            get() = "All Switches"
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
        viewModel = ViewModelProvider(requireActivity())[SwitchesViewModel::class.java] //getting viewmodel in activity scope so that all fragments in pager have access to it

        initializeList()

    }

    private fun initializeList() {
        Log.d("initialization", "initializing list")

        viewModel.switches.observe(viewLifecycleOwner, Observer { switches ->
            switches_list.apply {
                layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2)
                } else {
                    LinearLayoutManager(context)
                }
                setHasFixedSize(true)
                adapter = SwitchListAdapter(switches) {
                    viewModel.sendSwitchState(it)
                }.apply { setHasStableIds(true) }
            }
        })

    }

}
