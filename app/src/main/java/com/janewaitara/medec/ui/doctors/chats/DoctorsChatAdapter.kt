package com.janewaitara.medec.ui.doctors.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.janewaitara.medec.R
import com.janewaitara.medec.model.PatientsDetails
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.chat_list.view.*

class DoctorsChatAdapter(private val chatPatientClickListener: ChatPatientClickListener) : RecyclerView.Adapter<DoctorsChatAdapter.DoctorsChatsViewHolder>(){

    private var patientList = emptyList<PatientsDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_list, parent, false)
        return DoctorsChatsViewHolder(view)
    }

    override fun getItemCount() = patientList.size

    override fun onBindViewHolder(holder: DoctorsChatsViewHolder, position: Int) {
      holder.bind(patientList[position])

        holder.itemView.setOnClickListener {
            chatPatientClickListener.chatPatientClickListener(patientList[position].patId)
        }
    }

    interface ChatPatientClickListener {
        fun chatPatientClickListener(patientId: String)
    }

    class DoctorsChatsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var chatDocLastMsg = itemView.findViewById(R.id.user_last_message) as TextView
        var chatDocOnlineStatus = itemView.findViewById(R.id.doctor_online_status) as CircleImageView
        var chatDocOfflineStatus = itemView.findViewById(R.id.doctor_offline_status) as CircleImageView


        fun bind(patientDetails: PatientsDetails){
            itemView.apply {
                Glide.with(chat_user_image.context)
                    .load(patientDetails.patientImageUrl)
                    .placeholder(R.drawable.nurse)
                    .into(chat_user_image)

                chat_user_name.text = patientDetails.patientName
                chat_user_location.text = patientDetails.patientLocation

            }
        }
    }

}