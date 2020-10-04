package com.janewaitara.medec.ui.doctors.accounts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janewaitara.medec.model.result.*
import com.janewaitara.medec.repository.FirebaseRepository
import kotlinx.coroutines.launch

class DoctorsAccountsViewModel ( private val firebaseRepository: FirebaseRepository)
    : ViewModel() {

    private val doctorsDetailsMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getDoctorsDetailsLiveData() = doctorsDetailsMutableLiveData

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