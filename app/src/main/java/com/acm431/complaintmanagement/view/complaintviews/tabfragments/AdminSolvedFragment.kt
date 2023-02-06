package com.acm431.complaintmanagement.view.complaintviews.tabfragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acm431.complaintmanagement.R
import com.acm431.complaintmanagement.adapter.NotificationsAdapter
import com.acm431.complaintmanagement.adapter.ProfileComplaintsAdapter
import com.acm431.complaintmanagement.model.Complaint
import com.acm431.complaintmanagement.viewmodel.AdminViewModel
import com.acm431.complaintmanagement.viewmodel.AuthViewModel
import com.acm431.complaintmanagement.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_admin_active.*
import kotlinx.android.synthetic.main.fragment_admin_solved.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.notifications_row.*
import java.time.LocalDateTime

class AdminSolvedFragment : Fragment(R.layout.fragment_admin_solved) {

    private lateinit var recyclerA : NotificationsAdapter
    private lateinit var viewModel : AdminViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerA = NotificationsAdapter(this,false,requireContext())
        rv_solved_complaints_notifications.layoutManager = LinearLayoutManager(requireContext())
        rv_solved_complaints_notifications.setHasFixedSize(true)
        rv_solved_complaints_notifications.adapter = recyclerA


        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.complaintList.observe(this.viewLifecycleOwner, Observer { myList ->

            val tempArray = ArrayList<Complaint>()
            for(i in myList){
                if(i.solved == 1){
                    tempArray.add(i)
                }
            }
            myList?.let {
                tv_no_problems_msg_notifications_solved.visibility = View.GONE
                rv_solved_complaints_notifications.visibility = View.VISIBLE
                recyclerA.complaints= tempArray
            }

            if (myList == null) {
                tv_no_problems_msg_notifications_active.visibility = View.VISIBLE
                rv_solved_complaints_notifications.visibility = View.GONE
            }
        })
    }
}