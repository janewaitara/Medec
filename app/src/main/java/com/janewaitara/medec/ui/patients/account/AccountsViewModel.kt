package com.janewaitara.medec.ui.patients.account

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janewaitara.medec.model.PatientsDetails
import com.janewaitara.medec.model.result.*
import com.janewaitara.medec.repository.FirebaseRepository
import kotlinx.coroutines.launch

class AccountsViewModel( private val firebaseRepository: FirebaseRepository)
    : ViewModel() {

    private val imageUrlMutableLiveData  = MutableLiveData<ResultResponseViewState>()
    fun getImageUrlLiveData() = imageUrlMutableLiveData

    private val patientDetailsListMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getPatientDetailsLiveData() = patientDetailsListMutableLiveData

    fun uploadPatientUserProfile(userId: String, imageUri: Uri){
        viewModelScope.launch {
            firebaseRepository.uploadPatientsUserProfile(userId, imageUri){result->
                when(result){
                    is Success -> {
                      imageUrlMutableLiveData.postValue(
                          UserProfileImageUrlReturned(result.data)
                      )
                    }
                    is Failure -> {
                        imageUrlMutableLiveData.postValue(
                            ResultResponseError(result.error.localizedMessage ?: "")
                        )
                    }
                }
            }
        }
    }

    fun updatePatientProfileImage(patientsDetails: PatientsDetails){
        viewModelScope.launch {
            firebaseRepository.updatePatientProfileImage(patientsDetails)
        }
    }

    fun getPatientDetailsFromFireStore(userId: String){
        viewModelScope.launch {
            firebaseRepository.getPatientsDetails(userId){ result ->
                when(result){
                    is Success -> {
                        patientDetailsListMutableLiveData.postValue(
                            PatientDetailsSuccessResult(result.data)
                        )
                    }

                    is Failure -> {
                        patientDetailsListMutableLiveData.postValue(
                            ResultResponseError(result.error.localizedMessage ?: "")
                        )
                    }

                    is EmptySuccess -> {
                        patientDetailsListMutableLiveData.postValue(
                            ResultResponseError(result.message)
                        )
                    }
                }
            }
        }
    }
}