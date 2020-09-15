package com.janewaitara.medec.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.PatientsDetails
import com.janewaitara.medec.repository.FirebaseRepository
import kotlinx.coroutines.launch

class LocationViewModel(private val firebaseRepository: FirebaseRepository): ViewModel() {

    fun updatePatientLocation(patientsDetails: PatientsDetails){
        viewModelScope.launch {
            firebaseRepository.updatePatientLocation(patientsDetails)
        }

    }
    fun updateDoctorLocation(doctorsDetails: DoctorsDetails){
        viewModelScope.launch {
            firebaseRepository.updateDoctorLocation(doctorsDetails)
        }
    }


}