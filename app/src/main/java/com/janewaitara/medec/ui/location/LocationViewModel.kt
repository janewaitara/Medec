package com.janewaitara.medec.ui.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.PatientsDetails
import kotlinx.coroutines.launch

class LocationViewModel: ViewModel() {

    private var fireStore: FirebaseFirestore

    init {
        fireStore = FirebaseFirestore.getInstance()
        fireStore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun updatePatientLocation(patientsDetails: PatientsDetails){
        viewModelScope.launch {
            fireStore.collection("patients")
                .document(patientsDetails.patId)
                .update("patientLocation",patientsDetails.patientLocation)
                .addOnSuccessListener {
                    Log.d("FireStore", "Updated location for: ${patientsDetails.patientName}")
                }
                .addOnFailureListener {
                    Log.d("FireStore", "Error updating location details to firestore  with error: $it")
                }
        }

    }
    fun updateDoctorLocation(doctorsDetails: DoctorsDetails){
        viewModelScope.launch {
            fireStore.collection("doctors")
                .document(doctorsDetails.docId)
                .update("docLocation",doctorsDetails.docLocation)
                .addOnSuccessListener {
                    Log.d("FireStore", "Updated location for: ${doctorsDetails.docName}")
                }
                .addOnFailureListener {
                    Log.d("FireStore", "Error updating location details to firestore  with error: $it")
                }
        }
    }


}