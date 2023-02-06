package com.acm431.complaintmanagement.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.acm431.complaintmanagement.view.complaintviews.tabfragments.AdminActiveFragment
import com.acm431.complaintmanagement.view.complaintviews.tabfragments.AdminSolvedFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle : Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                AdminActiveFragment()
            }
            1->{
                AdminSolvedFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}