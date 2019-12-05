package com.krzysztofsroga.librehome.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.krzysztofsroga.librehome.MainActivityFragmentFactory
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.databinding.MainFragmentBinding
import com.krzysztofsroga.librehome.ui.switches.LightSwitch


class MainFragment : Fragment() {
    companion object : MainActivityFragmentFactory<MainFragment> {
        override fun newInstance() = MainFragment()
        override val name: String
            get() = "Main screen"
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<MainFragmentBinding>(inflater, R.layout.main_fragment, container, false)
        binding.bestswitch = LightSwitch.SimpleSwitch("BESTSWITCH", enabled = true)
        return binding.root //inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java) //TODO get switchesviewmodel?

/*TODO switch fragments different way        button_show_switches.setOnClickListener {
            replaceFragment(R.id.center_fragment, SwitchesFragment.newInstance())
        }
        button_music.setOnClickListener {
            replaceFragment(R.id.center_fragment, MusicFragment.newInstance())
        }*/
    }


}
