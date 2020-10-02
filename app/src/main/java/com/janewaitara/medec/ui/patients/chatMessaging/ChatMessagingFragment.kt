package com.janewaitara.medec.ui.patients.chatMessaging

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.janewaitara.medec.R
import com.janewaitara.medec.common.extensions.onClick
import com.janewaitara.medec.model.result.ChannelIdSuccessResult
import com.janewaitara.medec.model.result.ResultResponseViewState
import kotlinx.android.synthetic.main.fragment_chat_messaging.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatMessagingFragment : Fragment() {

    private val chatMessagingViewModel: ChatMessagingViewModel by viewModel()
    lateinit var currentUserId: String
    private lateinit var messageReceiverId: String
    private lateinit var messageReceiverName: String
    private lateinit var messageReceiverProfilePictureUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_messaging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            messageReceiverId = ChatMessagingFragmentArgs.fromBundle(it).messageRecipientId
            messageReceiverName = ChatMessagingFragmentArgs.fromBundle(it).messageRecipientName
            messageReceiverProfilePictureUrl =
                ChatMessagingFragmentArgs.fromBundle(it).messageRecipientProfilePictureUrl
        }
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        setUpRecipient()
        subscribeToData()

    }

    private fun setUpRecipient() {

        Glide.with(chat_message_doctor_image.context)
            .load(messageReceiverProfilePictureUrl)
            .placeholder(R.drawable.nurse)
            .into(chat_message_doctor_image)

        chat_message_doctor_name.text = messageReceiverName
        val message = chat_message.text.toString()

        chat_message.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                send_message.setImageResource(R.drawable.ic_keyboard_voice)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                send_message.setImageResource(R.drawable.ic_send)
            }

        })
      /*  if (message.isEmpty()){
            send_message.setImageResource(R.drawable.ic_keyboard_voice)
        }else{
            send_message.setImageResource(R.drawable.ic_send)
        }*/

        send_message.onClick {
            sendMessageToUser(currentUserId, messageReceiverId, message)
        }
    }

    private fun subscribeToData() {
        chatMessagingViewModel.getOrCreateChatChannel(currentUserId,messageReceiverId)

        chatMessagingViewModel.getOrCreateChatChannelLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
            resultResponseViewState?.let {resultViewState ->
                 onResultViewStateChanged(resultViewState)
            }
        })
    }

    private fun addChatMessageListener(channelId:  String) {
        chatMessagingViewModel.addChatMessageListener(channelId)

        /**
         * messagesListenerRegistration for when we want to stop listening to the changes in messages*/
        chatMessagingViewModel.messagesListenerRegistrationLiveData().observe(viewLifecycleOwner,
            Observer { messagesListenerRegistration ->

        })

        chatMessagingViewModel.getTextMessagesListLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
            resultResponseViewState?.let { resultResponseViewState ->
                Toast.makeText(activity, "Messages Changed", Toast.LENGTH_LONG).show()
            }
        })

    }


    private fun onResultViewStateChanged(resultViewState: ResultResponseViewState) {
        when(resultViewState){
            is ChannelIdSuccessResult -> addChatMessageListener(resultViewState.data)
        }
    }

    private fun sendMessageToUser(messageSenderId: String, messageReceiverId: String, message: String) {
        TODO("Not yet implemented")
    }
}