package com.janewaitara.medec.ui.patients.allDoctors

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janewaitara.medec.model.result.*
import com.janewaitara.medec.repository.FirebaseRepository
import kotlinx.coroutines.launch

class AllDoctorsViewModel( private val firebaseRepository: FirebaseRepository)
    : ViewModel() {

    private val doctorsDetailsListMutableLiveData = MutableLiveData<ResultResponseViewState>()
    fun getDoctorsListLiveData() = doctorsDetailsListMutableLiveData

    fun getDoctorsListFromFireStore(){
        viewModelScope.launch {
            firebaseRepository.getDoctorsFromFireStore {result ->
                when(result){
                    is Success -> {
                        Log.d("DoctorsDetailsList", result.data.toString())
                        doctorsDetailsListMutableLiveData.postValue(
                            DoctorsListSuccessResult(result.data)
                        )
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

}