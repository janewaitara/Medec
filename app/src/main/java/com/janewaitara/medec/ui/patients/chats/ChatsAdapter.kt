package com.janewaitara.medec.ui.patients.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.janewaitara.medec.R
import com.janewaitara.medec.model.ChannelId
import com.janewaitara.medec.model.ChatUserDetails
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.chat_list.view.*
import kotlinx.android.synthetic.main.chat_list_sample.view.*


class ChatsAdapter(private val chatDoctorClickListener: ChatDoctorClickListener): RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    private var doctorsList = emptyList<ChatUserDetails>()

    interface ChatDoctorClickListener{
        fun chatDoctorClickListener(doctorId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_list, parent, false)
        return ChatsViewHolder(view)
    }

    override fun getItemCount(): Int = doctorsList.size

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(doctorsList[position])

        /*holder.bind(recipientsIdList[position])*/
        holder.itemView.setOnClickListener {
            chatDoctorClickListener.chatDoctorClickListener(doctorsList[position].recipientId)
        }
    }

    internal fun setChatDoctorList(chatDoctorList: List<ChatUserDetails>){
        this.doctorsList =chatDoctorList
        notifyDataSetChanged()
    }

    class ChatsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
      //var chatDocImage = itemView.findViewById(R.id.chat_doctor_image) as CircleImageView
      //var chatDocName = itemView.findViewById(R.id.chat_doctor_name) as TextView
      var chatDocLastMsg = itemView.findViewById(R.id.user_last_message) as TextView
     // var chatDocLocation = itemView.findViewById(R.id.chat_doctor_location) as TextView
      var chatDocOnlineStatus = itemView.findViewById(R.id.doctor_online_status) as CircleImageView
      var chatDocOfflineStatus = itemView.findViewById(R.id.doctor_offline_status) as CircleImageView


        fun bind(chatDoctorDetails:ChatUserDetails){
            itemView.apply {
                Glide.with(chat_user_image.context)
                    .load(chatDoctorDetails.recipientImageUrl)
                    .placeholder(R.drawable.nurse)
                    .into(chat_user_image)

                chat_user_name.text = chatDoctorDetails.recipientName
                chat_user_location.text = chatDoctorDetails.recipientLocation
                chatDocLastMsg.text = chatDoctorDetails.lastMessage
            }
        }

    }


}