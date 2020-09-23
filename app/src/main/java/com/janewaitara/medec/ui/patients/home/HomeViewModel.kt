package com.janewaitara.medec.ui.patients.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.result.*
import com.janewaitara.medec.repository.FirebaseRepository
import kotlinx.coroutines.launch

class HomeViewModel( private val firebaseRepository: FirebaseRepository)
    : ViewModel() {



    private val nearByDoctorsDetailsListMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getNearByDoctorsListLiveData() = nearByDoctorsDetailsListMutableLiveData

    private val doctorsDetailsListMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getDoctorsListLiveData() = doctorsDetailsListMutableLiveData

    private val patientDetailsListMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getPatientDetailsLiveData() = patientDetailsListMutableLiveData

    fun getDoctorsListFromFireStore(){
        viewModelScope.launch {
            firebaseRepository.getDoctorsFromFireStore {result ->
                when(result){
                    is Success -> {
                        Log.d("DoctorsDetailsList", result.data.toString())
                        doctorsDetailsListMutableLiveData.postValue(
                            DoctorsListSuccessResult(result.data))
                    }
                    is Failure -> {
                        doctorsDetailsListMutableLiveData.postValue(
                            ResultResponseError(result.error.localizedMessage ?: "")
                        )
                    }
                }

            }
        }
    }

    fun getNearByDoctorsFromFireStore(userLocation: String){
        viewModelScope.launch {
            firebaseRepository.getNearByDoctorsFromFireStore(userLocation){ result ->
                when(result){
                    is Success -> {
                        Log.d("Near By Doctors Details", result.data.toString())
                        nearByDoctorsDetailsListMutableLiveData.postValue(
                            DoctorsListSuccessResult(result.data))
                    }
                    is Failure -> {
                        nearByDoctorsDetailsListMutableLiveData.postValue(
                            ResultResponseError(result.error.localizedMessage ?: "")
                        )
                    }
                    is EmptySuccess -> {
                        nearByDoctorsDetailsListMutableLiveData.postValue(
                            ResultResponseError(result.message)
                        )
                    }

                }

            }
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