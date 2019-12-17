package com.krzysztofsroga.librehome.ui.mylists

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.ui.MainActivityFragmentFactory
import kotlinx.android.synthetic.main.switch_group_fragment.*

class SwitchGroupFragment : Fragment() {

    companion object :
        MainActivityFragmentFactory<SwitchGroupFragment> {
        override fun newInstance() = SwitchGroupFragment()
        override val name: String
            get() = "Your switches groups:"
    }

    private lateinit var viewModel: SwitchGroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.switch_group_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SwitchGroupViewModel::class.java)

        switch_group_list.apply {
            setHasFixedSize(true)
            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 2)
            } else {
                LinearLayoutManager(context)
            }
            adapter = SwitchGroupAdapter(viewModel.switchGroupList,
                onItemClick = { switchGroup, i ->
                    showSwitchGroup(switchGroup, i)
                },
                onAddGroupClick = {
                    showSwitchGroup(TODO(), -1)
                })
        }
    }

    private fun showSwitchGroup(group: SwitchGroup, groupIdx: Int) {
        Toast.makeText(context, "You selected ${group.name} which is ${group.description}!", Toast.LENGTH_SHORT).show()
//        val intent = Intent(context, DogActivity::class.java).apply { TODO uncomment and fix
//            putExtra(EXTRA_DOG_IDX, dogIdx)
//        }
//        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
    }

}
