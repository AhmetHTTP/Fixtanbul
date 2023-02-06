package com.acm431.complaintmanagement.view.complaintviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acm431.complaintmanagement.R
import com.acm431.complaintmanagement.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_admin_complaints.*


class AdminComplaints : Fragment(R.layout.fragment_admin_complaints) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter(childFragmentManager,lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout,viewPager){tab,position->
            when(position){
                0->{
                    tab.text = getString(R.string.active_complaints)
                }
                1->{
                    tab.text = getString(R.string.solved_complaints)
                }
            }
        }.attach()
    }


}