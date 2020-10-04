package com.janewaitara.medec.ui.patients.chatMessaging

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.janewaitara.medec.R
import com.janewaitara.medec.common.extensions.onClick
import com.janewaitara.medec.model.MessageType
import com.janewaitara.medec.model.TextMessage
import com.janewaitara.medec.model.result.ChannelIdSuccessResult
import com.janewaitara.medec.model.result.ResultResponseError
import com.janewaitara.medec.model.result.ResultResponseViewState
import com.janewaitara.medec.model.result.TextMessagesListSuccessResult
import kotlinx.android.synthetic.main.fragment_chat_messaging.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ChatMessagingFragment : Fragment() {

    private val chatMessagingViewModel: ChatMessagingViewModel by viewModel()
    lateinit var currentUserId: String
    private lateinit var messageReceiverId: String
    private lateinit var messageReceiverName: String
    private lateinit var messageReceiverProfilePictureUrl: String
    /*private var shouldInitRecyclerView = true*/

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
                send_message.setImageResource(R.drawable.ic_send)
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


    }

    private fun subscribeToData() {
        chatMessagingViewModel.getOrCreateChatChannel(currentUserId,messageReceiverId)

        chatMessagingViewModel.getOrCreateChatChannelLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
            resultResponseViewState?.let {resultViewState ->
                 onResultViewStateChanged(resultViewState)
            }
        })
    }


    private fun onResultViewStateChanged(resultViewState: ResultResponseViewState) {
        when(resultViewState){
            is ChannelIdSuccessResult -> addChatMessageListenerAndSendMessage(resultViewState.data)
            is TextMessagesListSuccessResult -> setUpRecyclerView(resultViewState.data)
            is ResultResponseError -> showErrorMessage(resultViewState.error)
        }

    }

    private fun setUpRecyclerView(messagesList: List<TextMessage>) {

        Log.e("Messages Recycler", messagesList.toString())

        val recyclerAdapter =  MessageAdapter()
        recycler_chats.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter =  recyclerAdapter
        }

        recyclerAdapter.setTextMessages(messagesList)

        recycler_chats.scrollToPosition(recycler_chats.adapter!!.itemCount - 1)

    }


    private fun addChatMessageListenerAndSendMessage(channelId:  String) {
        addChatMessageListener(channelId)
        sendMessage(channelId)
    }

    private fun sendMessage(channelId: String) {
        send_message.onClick {
            /*sendMessageToUser(currentUserId, messageReceiverId, message)*/
            val typedMessage = chat_message.text.toString()
            val messageToSend = TextMessage(
                typedMessage,
                Calendar.getInstance().time,
                currentUserId,
                messageReceiverId,
                MessageType.TEXT
            )
            chat_message.setText("")
            chatMessagingViewModel.sendMessage(messageToSend, channelId)
        }
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
                onResultViewStateChanged(resultResponseViewState)
                Toast.makeText(activity, "Messages Changed", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun sendMessageToUser(messageSenderId: String, messageReceiverId: String, message: String) {
        TODO("Not yet implemented")
    }

    private fun showErrorMessage(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }
}