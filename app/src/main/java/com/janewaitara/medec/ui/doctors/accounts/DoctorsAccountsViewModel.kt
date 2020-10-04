package com.janewaitara.medec.ui.doctors.accounts

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.result.*
import com.janewaitara.medec.repository.FirebaseRepository
import kotlinx.coroutines.launch

class DoctorsAccountsViewModel ( private val firebaseRepository: FirebaseRepository)
    : ViewModel() {

    private val doctorsDetailsMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getDoctorsDetailsLiveData() = doctorsDetailsMutableLiveData

    private val docProfileImageUrlLiveData = MutableLiveData<ResultResponseViewState>()
    fun getDocProfileImageUrlLiveData() = docProfileImageUrlLiveData

    private val docProfileImageUpdatedLiveData = MutableLiveData<ResultResponseViewState>()
    fun getDocProfileUpdatedLiveData() = docProfileImageUpdatedLiveData

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

    fun  uploadDoctorsProfileImageToStorage(doctorsId: String,imageUri: Uri){
        viewModelScope.launch {
            firebaseRepository.uploadDoctorsProfileImageToStorage(doctorsId,imageUri){result ->
                when(result){
                    is Success -> {
                        docProfileImageUrlLiveData.postValue(
                            UserProfileImageUrlReturned(result.data)
                        )
                    }
                    is Failure -> {
                        docProfileImageUrlLiveData.postValue(
                            ResultResponseError(result.error.localizedMessage ?: "")
                        )
                    }
                }
            }
        }
    }

    fun updateDocProfileImage(doctorsDetails: DoctorsDetails){
        viewModelScope.launch {
            firebaseRepository.updateDoctorProfileImage(doctorsDetails){ result ->
                when(result){
                    is Success -> {
                        docProfileImageUpdatedLiveData.postValue(
                            UserProfilePicUpdateSuccessResult(result.data)
                        )
                    }
                    is Failure -> {
                        docProfileImageUpdatedLiveData.postValue(
                            ResultResponseError(result.error.localizedMessage ?: "")
                        )
                    }
                }

            }
        }
    }

}