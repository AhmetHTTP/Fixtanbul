package com.acm431.complaintmanagement.view.complaintviews.tabfragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acm431.complaintmanagement.BaseFragment
import com.acm431.complaintmanagement.R
import com.acm431.complaintmanagement.adapter.NotificationsAdapter
import com.acm431.complaintmanagement.database.Database
import com.acm431.complaintmanagement.model.Complaint
import com.acm431.complaintmanagement.viewmodel.AdminViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_admin_active.*
import kotlinx.android.synthetic.main.fragment_admin_solved.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.notifications_row.*

class AdminActiveFragment : BaseFragment() {

    private lateinit var recyclerA : NotificationsAdapter
    private lateinit var viewModel: AdminViewModel

    private val auth = FirebaseAuth.getInstance()
    private var isAdmin = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

        if(auth.currentUser!!.uid == "7k7afLkIiZUnTuW8NG5LfHfpIpU2"){
            isAdmin = true
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerA = NotificationsAdapter(this,isAdmin,requireContext())
        rv_active_complaints_notifications.layoutManager = LinearLayoutManager(requireContext())
        rv_active_complaints_notifications.setHasFixedSize(true)
        rv_active_complaints_notifications.adapter = recyclerA
        observeLiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_active, container, false)
    }

    override fun onResume() {
        super.onResume()

    }

    fun editStatus(complaintId: String){
        Database().editComplaintStatus(this,complaintId)
    }

    fun solvedSuccess(){
        println("success")
        //solveda yönlendir.
    }

    private fun observeLiveData() {

        viewModel.complaintList.observe(this.viewLifecycleOwner, Observer { myList ->
            myList?.let {
                val tempArray = ArrayList<Complaint>()
                for(i in myList){
                    if(i.solved == 0){
                        tempArray.add(i)
                    }
                }
                tv_no_problems_msg_notifications_active.visibility = View.GONE
                rv_active_complaints_notifications.visibility = View.VISIBLE
                recyclerA.complaints = tempArray
            }

            if (myList == null) {
                tv_no_problems_msg_notifications_active.visibility = View.VISIBLE
                rv_active_complaints_notifications.visibility = View.GONE
            }
        })
    }
}