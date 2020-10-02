package com.janewaitara.medec.ui.patients.chatMessaging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.janewaitara.medec.model.result.*
import com.janewaitara.medec.repository.FirebaseRepository
import kotlinx.coroutines.launch

class ChatMessagingViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private val getOrCreateChatChannelMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getOrCreateChatChannelLiveData() = getOrCreateChatChannelMutableLiveData

    private val textMessagesMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getTextMessagesListLiveData() = textMessagesMutableLiveData

    private val messagesListenerRegistrationMutableLiveData = MutableLiveData<ListenerRegistration>()
    fun messagesListenerRegistrationLiveData() = messagesListenerRegistrationMutableLiveData


    fun getOrCreateChatChannel(
        userId: String,
        messageReceiverId: String
    ) {
        viewModelScope.launch {
            firebaseRepository.getOrCreateChatChannel(userId, messageReceiverId) { result ->
                when (result) {
                    is Success -> {
                        getOrCreateChatChannelMutableLiveData.postValue(
                            ChannelIdSuccessResult(result.data)
                        )
                    }
                    is Failure -> {
                        getOrCreateChatChannelMutableLiveData.postValue(
                            ResultResponseError(result.error.toString())
                        )
                    }
                }
            }
        }
    }

    fun addChatMessageListener(channelId: String) {
        viewModelScope.launch {

            val messagesListenerRegistration =
                firebaseRepository.addChatMessageListener(channelId) { result ->
                    when (result) {
                        is Success -> {
                            textMessagesMutableLiveData.postValue(
                                TextMessagesListSuccessResult(result.data)
                            )
                        }
                        is Failure -> {
                            textMessagesMutableLiveData.postValue(
                                ResultResponseError(result.error.localizedMessage ?: "")
                            )
                        }
                    }
                }
            messagesListenerRegistrationMutableLiveData.postValue(messagesListenerRegistration)
        }
    }
}