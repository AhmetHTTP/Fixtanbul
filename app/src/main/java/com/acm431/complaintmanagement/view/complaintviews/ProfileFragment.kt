package com.acm431.complaintmanagement.view.complaintviews

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acm431.complaintmanagement.ComplaintActivity
import com.acm431.complaintmanagement.MainActivity
import com.acm431.complaintmanagement.R
import com.acm431.complaintmanagement.adapter.ProfileComplaintsAdapter
import com.acm431.complaintmanagement.database.GlobalValues
import com.acm431.complaintmanagement.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_add_complaint.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var recyclerA : ProfileComplaintsAdapter
    private val auth = FirebaseAuth.getInstance()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        iv_log_out.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rv_profile_complaints.layoutManager = LinearLayoutManager(requireContext())
        recyclerA = ProfileComplaintsAdapter(requireContext())
        rv_profile_complaints.adapter = recyclerA
        rv_profile_complaints.setHasFixedSize(true)
        GlobalValues.userName.observe(viewLifecycleOwner, Observer {
            tv_name_surname_profile.text = it
        })
        btn_report_problem.setOnClickListener {

            val intent = Intent(requireContext(),ComplaintActivity::class.java)
            intent.putExtra("toAddComplaint",1)
            startActivity(intent)

        }
        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.list.observe(this.viewLifecycleOwner, Observer { myList ->
            myList?.let {
                tv_no_problems_msg_profile.visibility = View.GONE
                fl_rv_complaint_holder_profile.visibility = View.VISIBLE
                recyclerA.complaints = myList
            }

            if (myList == null) {
                tv_no_problems_msg_profile.visibility = View.VISIBLE
                fl_rv_complaint_holder_profile.visibility = View.GONE
            }
        })
    }
}