package com.krzysztofsroga.librehome.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.music.MusicFragment
import com.krzysztofsroga.librehome.ui.switches.SwitchesFragment
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
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        button_show_switches.setOnClickListener {
            replaceFragment(R.id.center_fragment, SwitchesFragment.newInstance())
        }
        button_music.setOnClickListener {
            replaceFragment(R.id.center_fragment, MusicFragment.newInstance())
        }
    }


}

fun Fragment.replaceFragment(frame: Int, newFragment: Fragment) {
    activity!!.supportFragmentManager.beginTransaction().apply {
        replace(
            frame,
            newFragment
        )
        addToBackStack(null)
    }.commit()
}
