package com.janewaitara.medec.ui.patients.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.janewaitara.medec.R
import com.janewaitara.medec.model.DoctorsDetails
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.doctors_chat_list.view.*


class ChatsAdapter(private val chatDoctorClickListener: ChatDoctorClickListener): RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    private var doctorsList = emptyList<DoctorsDetails>()

    interface ChatDoctorClickListener{
        fun chatDoctorClickListener(doctorId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.doctors_chat_list, parent, false)
        return ChatsViewHolder(view)
    }

    override fun getItemCount(): Int = doctorsList.size

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(doctorsList[position])

        holder.itemView.setOnClickListener {
            chatDoctorClickListener.chatDoctorClickListener(doctorsList[position].docId)
        }
    }

    class ChatsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
      //var chatDocImage = itemView.findViewById(R.id.chat_doctor_image) as CircleImageView
      //var chatDocName = itemView.findViewById(R.id.chat_doctor_name) as TextView
      var chatDocLastMsg = itemView.findViewById(R.id.doctor_last_message) as TextView
     // var chatDocLocation = itemView.findViewById(R.id.chat_doctor_location) as TextView
      var chatDocOnlineStatus = itemView.findViewById(R.id.doctor_online_status) as CircleImageView
      var chatDocOfflineStatus = itemView.findViewById(R.id.doctor_offline_status) as CircleImageView


        fun bind(doctorDetails: DoctorsDetails){
            itemView.apply {
                Glide.with(chat_doctor_image.context)
                    .load(doctorDetails.docImageUrl)
                    .placeholder(R.drawable.nurse)
                    .into(chat_doctor_image)

                chat_doctor_name.text = doctorDetails.docName
                chat_doctor_location.text = doctorDetails.docLocation

            }
        }
    }


}