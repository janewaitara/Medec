package com.janewaitara.medec.ui.patients.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.janewaitara.medec.R
import com.janewaitara.medec.model.DoctorsDetails
import kotlinx.android.synthetic.main.doctors_near_by.view.*

class DoctorsRecyclerAdapter(private val doctorDetailsClickListener: DoctorDetailsClickListener ): RecyclerView.Adapter<DoctorsRecyclerAdapter.DoctorsDetailsViewHolder> (){

    private var doctorsList = emptyList<DoctorsDetails>()

    interface DoctorDetailsClickListener{
        fun doctorDetailsClickListener(doctorId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.doctors_near_by, parent, false)
        return DoctorsDetailsViewHolder(view)
    }

    override fun getItemCount() = doctorsList.size

    override fun onBindViewHolder(holder: DoctorsDetailsViewHolder, position: Int) {
       holder.bind(doctorsList[position])
        holder.itemView.setOnClickListener {
            doctorDetailsClickListener.doctorDetailsClickListener(doctorsList[position].docId)
        }
    }
    internal fun setDoctorsList(doctorsList: List<DoctorsDetails>){
        this.doctorsList = doctorsList
        notifyDataSetChanged()
    }

    class DoctorsDetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(doctorDetails: DoctorsDetails){
            itemView.apply {
                Glide.with(imageDoctor.context)
                    .load(doctorDetails.docImageUrl)
                    .into(imageDoctor)

                doctor_name.text = doctorDetails.docName
                doctor_title.text = doctorDetails.doctorsTitle
            }

        }
    }
}