package com.krzysztofsroga.librehome.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.adapters.LogEntryAdapter
import com.krzysztofsroga.librehome.utils.Logger
import com.krzysztofsroga.librehome.utils.getCurrentOrientationLayoutManager
import com.krzysztofsroga.librehome.viewmodels.LogsViewModel
import kotlinx.android.synthetic.main.logs_list_fragment.*

class LogsListFragment : Fragment() {

    companion object {
        fun newInstance() = LogsListFragment()
    }

    private val viewModel: LogsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.logs_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        logs_list.apply {
            layoutManager = getCurrentOrientationLayoutManager()
            setHasFixedSize(true)
            adapter = LogEntryAdapter(Logger.getLogFileNames(), viewModel.logName::setValue)
        }

    }

}
