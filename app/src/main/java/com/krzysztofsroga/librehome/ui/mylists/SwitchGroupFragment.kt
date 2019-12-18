package com.krzysztofsroga.librehome.ui.mylists

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.SwitchGroup
import com.krzysztofsroga.librehome.ui.MainActivityFragmentFactory
import com.krzysztofsroga.librehome.viewmodels.SwitchGroupViewModel
import kotlinx.android.synthetic.main.switch_group_fragment.*

class SwitchGroupFragment : Fragment() {

    companion object :
        MainActivityFragmentFactory<SwitchGroupFragment> {
        override fun newInstance() = SwitchGroupFragment()
        override val name: String
            get() = "Your switches groups:"
    }

    private val switchGroupViewModel: SwitchGroupViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.switch_group_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        switch_group_list.apply {
            setHasFixedSize(true)
            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 2)
            } else {
                LinearLayoutManager(context)
            }
        }

        switchGroupViewModel.switchGroups.observe(viewLifecycleOwner, Observer { groups ->
            switch_group_list.adapter = SwitchGroupAdapter(groups,
                onItemClick = { switchGroup ->
                    showSwitchGroup(switchGroup)
                },
                onAddGroupClick = {
                    startActivity(Intent(activity, NewGroupActivity::class.java))
                })
        })


    }

    private fun showSwitchGroup(group: SwitchGroup) {
        Toast.makeText(context, "You selected ${group.name} which is ${group.description}!", Toast.LENGTH_SHORT).show()
//        val intent = Intent(context, GroupActivity::class.java).apply { TODO uncomment and fix
//            putExtra(EXTRA_GROUP_ID, group.id)
//        }
//        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
    }

}
