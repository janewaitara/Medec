package com.janewaitara.medec.ui.patients.chatMessaging

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.janewaitara.medec.R
import com.janewaitara.medec.model.TextMessage
import kotlinx.android.synthetic.main.text_message.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat

class MessageAdapter(): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){

    private var textMessagesList = emptyList<TextMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int  = textMessagesList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(textMessagesList[position])
        setTimeToText(textMessagesList[position],holder)
        setMessageRootGravity(textMessagesList[position], holder)
    }

    internal fun setTextMessages(textMessagesList: List<TextMessage>){
        this.textMessagesList = textMessagesList
        notifyDataSetChanged()
    }
    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(textMessage: TextMessage){
            itemView.apply {
                text_message_view.text = textMessage.text


            }
        }
    }
    /**Putting date into a human readable format*/
    private fun setTimeToText(textMessage: TextMessage, viewHolder: MessageViewHolder){
        val dateFormat = SimpleDateFormat
            .getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT )
        viewHolder.itemView.text_message_time.text = dateFormat.format(textMessage.time)
    }

    private fun setMessageRootGravity(textMessage: TextMessage, viewHolder: MessageViewHolder) {
        if (textMessage.messageSenderId == FirebaseAuth.getInstance().currentUser?.uid) {
            viewHolder.itemView.message_root.apply {
                backgroundResource = R.drawable.sender_message_background
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParams
            }
        } else {
            viewHolder.itemView.message_root.apply {
                backgroundResource = R.drawable.recipient_message_background
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }
        }
    }



}