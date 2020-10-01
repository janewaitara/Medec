package com.janewaitara.medec.ui.patients.chatMessaging

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.janewaitara.medec.R
import com.janewaitara.medec.common.extensions.onClick
import kotlinx.android.synthetic.main.fragment_chat_messaging.*

class ChatMessagingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_messaging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var message = chat_message.text.toString()
        if (message.isEmpty()){
            send_message.setImageResource(R.drawable.ic_keyboard_voice)
        }else{
            send_message.setImageResource(R.drawable.ic_send)
        }

        send_message.onClick {

        }
    }
}