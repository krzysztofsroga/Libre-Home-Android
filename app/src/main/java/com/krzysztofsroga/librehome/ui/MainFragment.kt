package com.krzysztofsroga.librehome.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.Switches.SwitchesFragment
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        button_show_switches.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().apply {
                replace(
                    R.id.center_fragment,
                    SwitchesFragment.newInstance()
                )
                addToBackStack(null)
            }.commit()
        }
    }


}
