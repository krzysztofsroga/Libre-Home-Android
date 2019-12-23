package com.krzysztofsroga.librehome.ui.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.fragment_home_second.*

class HomeSecondFragment : Fragment() {

    private val args: HomeSecondFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textview_home_second.text = getString(R.string.hello_home_second, args.myArg)

        button_home_second.setOnClickListener {
            findNavController().navigate(R.id.action_HomeSecondFragment_to_HomeFragment)
        }
    }
}
