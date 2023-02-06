package com.acm431.complaintmanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.acm431.complaintmanagement.view.complaintviews.AddComplaintFragment
import com.acm431.complaintmanagement.view.complaintviews.AdminComplaints
import com.acm431.complaintmanagement.view.complaintviews.ProfileFragment
import kotlinx.android.synthetic.main.activity_complaint.*

class ComplaintActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint)

        if(intent.hasExtra("toProfile")){
            println("profile test")
            replaceFragment(ProfileFragment())
            bottomNavigationView.getMenu().getItem(0).setChecked(true)
        }
        if(intent.hasExtra("toAddComplaint")){
            println("add comp")
            replaceFragment(AddComplaintFragment())
            bottomNavigationView.getMenu().getItem(1).setChecked(true)
        }

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.add_complaint -> replaceFragment(AddComplaintFragment())
                R.id.notifications -> replaceFragment(AdminComplaints())
                R.id.profile -> replaceFragment(ProfileFragment())
                else ->{
                }
            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()


    }

    override fun onBackPressed() {
        super.onBackPressed()


    }
}