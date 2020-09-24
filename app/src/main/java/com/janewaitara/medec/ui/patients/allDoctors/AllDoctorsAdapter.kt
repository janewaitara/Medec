package com.janewaitara.medec.ui.patients.allDoctors


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.janewaitara.medec.R
import com.janewaitara.medec.model.DoctorsDetails
import kotlinx.android.synthetic.main.doctors_list.view.*
import kotlinx.android.synthetic.main.doctors_near_by.view.doctor_name
import kotlinx.android.synthetic.main.doctors_near_by.view.doctor_title

class AllDoctorsAdapter(private val doctorDetailsClickListener: DoctorDetailsClickListener ): RecyclerView.Adapter<AllDoctorsAdapter.AllDoctorsDetailsViewHolder> (){

    private var doctorsList = emptyList<DoctorsDetails>()

    interface DoctorDetailsClickListener{
        fun doctorDetailsClickListener(doctorId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDoctorsDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.doctors_list, parent, false)
        return AllDoctorsDetailsViewHolder(view)
    }

    override fun getItemCount() = doctorsList.size

    override fun onBindViewHolder(holder: AllDoctorsDetailsViewHolder, position: Int) {
        holder.bind(doctorsList[position])
        holder.itemView.setOnClickListener {
            doctorDetailsClickListener.doctorDetailsClickListener(doctorsList[position].docId)
        }
    }
    internal fun setDoctorsList(doctorsList: List<DoctorsDetails>){
        this.doctorsList = doctorsList
        notifyDataSetChanged()
    }

    class AllDoctorsDetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(doctorDetails: DoctorsDetails){
            itemView.apply {
                Glide.with(doctor_image.context)
                    .load(doctorDetails.docImageUrl)
                    .placeholder(R.drawable.nurse)
                    .into(doctor_image)

                doctor_name.text = doctorDetails.docName
                doctor_title.text = doctorDetails.doctorsTitle
                doctor_location.text = doctorDetails.docLocation
            }
        }
    }
}