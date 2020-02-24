package com.krzysztofsroga.librehome.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.utils.Logger
import com.krzysztofsroga.librehome.viewmodels.LogsViewModel
import kotlinx.android.synthetic.main.fragment_log_content.*


class LogContentFragment : Fragment() {

    companion object {
        fun newInstance() = LogContentFragment()
    }

    val viewModel: LogsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_content, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.logName.observe(viewLifecycleOwner, Observer {fileName ->
            log_file_name.text == fileName
            log_content.setText(Logger.getLog(fileName))
        })
    }

}
