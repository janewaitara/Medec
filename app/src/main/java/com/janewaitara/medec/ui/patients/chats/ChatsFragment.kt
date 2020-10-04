package com.janewaitara.medec.ui.patients.chats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.janewaitara.medec.R
import com.janewaitara.medec.model.ChannelId
import com.janewaitara.medec.model.ChatUserDetails
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.TextMessage
import com.janewaitara.medec.model.result.*
import kotlinx.android.synthetic.main.fragment_chats.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatsFragment : Fragment() , ChatsAdapter.ChatDoctorClickListener{

    private val chatsViewModel: ChatsViewModel by viewModel()
    lateinit var currentUserId: String
    lateinit var chatsLastTextMessageDetails: TextMessage
   /* lateinit var chatUserDetails : ChatUserDetails*/
    var doctorChatList =  mutableListOf<ChatUserDetails>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        subscribeToData()
    }

    private fun subscribeToData() {
        chatsViewModel.getEngagedChatsRecipientsIdList(currentUserId)

        chatsViewModel.getEngagedChatsRecipientsIdLiveData().observe(viewLifecycleOwner,
            Observer { resultResponseViewState ->
                resultResponseViewState?.let { resultViewState ->
                    onResultViewStateChanged(resultViewState)
                }
            })
    }

    private fun onResultViewStateChanged(resultViewState: ResultResponseViewState) {
        when(resultViewState){
            is RecipientsIdSuccessResult -> getChannelLastMessage(resultViewState.channelIdList)
            is LastTextMessageSuccessResult ->  setUpTheChatsInterface(resultViewState.textMessage)
            is DoctorDetailsSuccessResult -> setUpTheChatsList(resultViewState.data)
            is ResultResponseError -> showErrorMessage(resultViewState.error)
            is EmptySuccessMessage -> {}
        }
    }
    private fun getChannelLastMessage(channelIdList: List<ChannelId>) {

        Log.d("Text Message 0",channelIdList.toString())

        for (channelId in channelIdList){
            Log.d("Text Message 01",channelId.channelId)
            chatsViewModel.getChannelLastMessage(channelId.channelId)

            Log.d("Text Message 0001",channelId.channelId)

            chatsViewModel.getChannelLastMessageLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
                resultResponseViewState?.let { resultViewState ->
                    onResultViewStateChanged(resultViewState)
                    Log.d("Text Message 00001",resultViewState.toString())
                }
            })
        }
       /* channelIdList.forEach { channelId ->
            Log.d("Text Message 01",channelId.channelId)
            chatsViewModel.getChannelLastMessage(channelId.channelId)

            Log.d("Text Message 0001",channelId.channelId)

            chatsViewModel.getChannelLastMessageLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
                resultResponseViewState?.let { resultViewState ->
                    onResultViewStateChanged(resultViewState)
                    Log.d("Text Message 00001",resultViewState.toString())
                }
            })
        }*/
    }

    private fun setUpTheChatsInterface(lastTextMessageDetails: TextMessage) {

        chatsLastTextMessageDetails  = lastTextMessageDetails

        Log.d("Text Message 2", lastTextMessageDetails.messageReceiverId)

        chatsViewModel.getDoctorsDetails(lastTextMessageDetails.messageReceiverId)
        Log.d("Text Message 3:1 ", lastTextMessageDetails.messageReceiverId)
        chatsViewModel.getDoctorsDetailsLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
            resultResponseViewState?.let {resultViewState ->
                onResultViewStateChanged(resultViewState)
                Log.d("Text Message 3:2 ", "Reached")
            }
        })
    }

    private fun setUpTheChatsList(data: DoctorsDetails) {

        Log.d("Text Message 4", data.docName)

        var chatUserDetails  = ChatUserDetails(
            recipientId = data.docId,
            recipientName = data.docName,
            recipientLocation = data.docLocation,
            recipientImageUrl = data.docImageUrl,
            lastMessage = chatsLastTextMessageDetails.text
        )

        /*.apply {
            recipientId = data.docId
            recipientName = data.docName
            recipientLocation = data.docLocation
            recipientImageUrl = data.docImageUrl
            lastMessage = chatsLastTextMessageDetails.text
        }*/

        doctorChatList.add(chatUserDetails)

        Log.d("Text Message 5"," ${doctorChatList[0].recipientName} And ${doctorChatList[0].lastMessage}")
        populateRecyclerView(doctorChatList)

    }

    private fun populateRecyclerView(doctorChatList: List<ChatUserDetails>) {
        Log.d("Text Message 6", doctorChatList.toString())
        val recyclerAdapter = ChatsAdapter(this)
        chats_doctors_list.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
            adapter = recyclerAdapter
        }
        recyclerAdapter.setChatDoctorList(doctorChatList)
    }

    override fun chatDoctorClickListener(doctorId: String) {
        TODO("Not yet implemented")
    }

    private fun showErrorMessage(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }

}