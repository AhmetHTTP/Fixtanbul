package com.acm431.complaintmanagement.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acm431.complaintmanagement.LoadGlide
import com.acm431.complaintmanagement.R
import com.acm431.complaintmanagement.model.Complaint
import kotlinx.android.synthetic.main.notifications_row.view.*
import kotlinx.android.synthetic.main.profile_complaints_row.view.*

class ProfileComplaintsAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ProfileComplaintsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val diffUtil = object : DiffUtil.ItemCallback<Complaint>() {
        override fun areItemsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
            return oldItem == newItem
        }
    }

    private val listDifferRecycler = AsyncListDiffer(this, diffUtil)

    var complaints: List<Complaint>
        get() = listDifferRecycler.currentList
        set(value) = listDifferRecycler.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProfileComplaintsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.profile_complaints_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = complaints[position]
        holder.itemView.tv_address_date_profile_complaint_row.background.alpha = 80
        holder.itemView.tv_complaint_name_profile_row.text = model.content
        holder.itemView.tv_urgency_profile_row.text = model.urgency
        holder.itemView.tv_row_complaint_status.text = model.status
        holder.itemView.tv_address_date_profile_complaint_row.text =
            model.location + " - " + model.date
        LoadGlide(holder.itemView.context).loadComplaintImage(
            model.imagePath,
            holder.itemView.iv_complaint_image_small_profile_row
        )
        holder.itemView.tv_address_date_profile_complaint_row.setOnClickListener {
            openGoogleMap(model.latitude, model.longitude, context)
        }
    }

    fun openGoogleMap(latitude: Double?, longitude: Double?, context: Context) {

        // you need action, data and package name

        val uri = Uri.parse("geo:$latitude,$longitude?z=15")
        //create an intent
        val mapIntent = Intent()

        //add action & data
        mapIntent.action = Intent.ACTION_VIEW
        mapIntent.data = uri

        //package name for google maps app
        mapIntent.setPackage("com.google.android.apps.maps")
        try {
            context.startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {

            //if map app isn't resolved/installed catch error
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return complaints.size
    }
}