package com.janewaitara.medec.ui.patients.chats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.Empty
import com.janewaitara.medec.model.result.ResultResponseViewState
import com.janewaitara.medec.model.result.*
import com.janewaitara.medec.repository.FirebaseRepository
import kotlinx.coroutines.launch

class ChatsViewModel(var firebaseRepository: FirebaseRepository): ViewModel() {

    private val recipientsIdMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getEngagedChatsRecipientsIdLiveData() = recipientsIdMutableLiveData

    private val channelLastMessageMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getChannelLastMessageLiveData() = channelLastMessageMutableLiveData

    private val doctorsDetailsMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getDoctorsDetailsLiveData() = doctorsDetailsMutableLiveData

    fun getEngagedChatsRecipientsIdList(userId: String){
        viewModelScope.launch {
            firebaseRepository.getEngagedChatsList(userId){ result ->
                when(result){
                    is Success ->  {
                        recipientsIdMutableLiveData.postValue(
                            RecipientsIdSuccessResult(result.data)
                        )
                    }
                    is Failure -> {
                        recipientsIdMutableLiveData.postValue(
                            ResultResponseError(result.error.localizedMessage ?: "")
                        )
                    }
                    is EmptySuccess -> {
                        recipientsIdMutableLiveData.postValue(
                            EmptySuccessMessage(result.message)
                        )
                    }
                }
            }
        }
    }

    fun getChannelLastMessage( channelId: String){
        viewModelScope.launch {
            firebaseRepository.getChannelLastMessages(channelId){ result ->
                when(result){
                    is Success -> {
                        channelLastMessageMutableLiveData.postValue(
                          LastTextMessageSuccessResult(result.data)
                        )
                    }
                    is Failure -> {
                        ResultResponseError(result.error.localizedMessage ?: "")
                    }
                }
            }
        }
    }

    fun getDoctorsDetails(doctorsId: String){
        viewModelScope.launch {
            Log.d("Text Message 4:1",doctorsId)
            firebaseRepository.getDoctorDetails(doctorsId){ result ->
                when(result){
                    is Success -> {
                        doctorsDetailsMutableLiveData.postValue(
                            DoctorDetailsSuccessResult(result.data)
                        )
                    }
                    is Failure -> {
                        doctorsDetailsMutableLiveData.postValue(
                           ResultResponseError(result.error.localizedMessage ?: "")
                        )
                    }
                    is EmptySuccess -> {
                        doctorsDetailsMutableLiveData.postValue(
                            EmptySuccessMessage(result.message)
                        )
                    }
                }
            }
        }
    }
}