package com.janewaitara.medec.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.PatientsDetails


class FirebaseRepository(var fireStore: FirebaseFirestore) {
    companion object {
        const val DOCTOR_COLLECTION = "doctors"
        const val PATIENT_COLLECTION = "patients"
    }

    init {
        fireStore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    /**
     * Saving Doctor's Details*/
    fun saveDoctorDetails(doctorsDetails: DoctorsDetails) {
        fireStore.collection(DOCTOR_COLLECTION)
            .document(doctorsDetails.docId)
            .set(doctorsDetails)
            .addOnSuccessListener {
                Log.d("FireStore", "User profile created for: ${doctorsDetails.docName}")
            }
            .addOnFailureListener {
                Log.d("FireStore", "Error adding document to firestore  with error: $it")
            }

    }

    /**
     * Updating Doctor's Details*/
    fun updateDoctorLocation(doctorsDetails: DoctorsDetails) {
        fireStore.collection(DOCTOR_COLLECTION)
            .document(doctorsDetails.docId)
            .update("docLocation", doctorsDetails.docLocation)
            .addOnSuccessListener {
                Log.d("FireStore", "Updated location for: ${doctorsDetails.docName}")
            }
            .addOnFailureListener {
                Log.d("FireStore", "Error updating location details to firestore  with error: $it")
            }

    }


    /**
     * Saving Patients Details*/
    fun savePatientDetails(patientsDetails: PatientsDetails) {
        fireStore.collection(PATIENT_COLLECTION)
            .document(patientsDetails.patId)
            .set(patientsDetails)
            .addOnSuccessListener {
                Log.d("FireStore", "User profile created for: ${patientsDetails.patientName}")
            }
            .addOnFailureListener {
                Log.d("FireStore", "Error adding document to firestore  with error: $it")
            }

    }

    /**
     * Updating Patients Details*/
    fun updatePatientLocation(patientsDetails: PatientsDetails) {
        fireStore.collection(PATIENT_COLLECTION)
            .document(patientsDetails.patId)
            .update("patientLocation", patientsDetails.patientLocation)
            .addOnSuccessListener {
                Log.d("FireStore", "Updated location for: ${patientsDetails.patientName}")
            }
            .addOnFailureListener {
                Log.d("FireStore", "Error updating location details to firestore  with error: $it")
            }


    }

}