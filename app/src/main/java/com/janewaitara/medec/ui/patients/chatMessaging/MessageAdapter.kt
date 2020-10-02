package com.janewaitara.medec.ui.patients.chatMessaging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.janewaitara.medec.R
import com.janewaitara.medec.model.TextMessage

class MessageAdapter(): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){

    private var textMessagesList = emptyList<TextMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int  = textMessagesList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    internal fun setTextMessages(textMessagesList: List<TextMessage>){
        this.textMessagesList = textMessagesList
        notifyDataSetChanged()
    }
    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

}