package com.janewaitara.medec.repository

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.janewaitara.medec.App
import com.janewaitara.medec.model.*
import com.janewaitara.medec.model.result.Failure
import com.janewaitara.medec.model.result.Result
import com.janewaitara.medec.model.result.Success


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
    fun getDoctorsFromFireStore(onDoctorsReturned: (result: Result<List<DoctorsDetails>>) -> Unit) {
        fireStore.collection(DOCTOR_COLLECTION)
            .get()
            .addOnSuccessListener {taskQuerySnapShot ->
                if (taskQuerySnapShot != null){
                   var doctorsList =  taskQuerySnapShot.toObjects(DoctorsDetails::class.java)
                    onDoctorsReturned.invoke(
                        Success(
                            doctorsList
                        )
                    )
                }else{
                    Toast.makeText(App.getAppContext(),"Document is empty", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                onDoctorsReturned.invoke(
                    Failure(
                        exception
                    )
                )
            }
    }

    /**
     * used to confirm whether a certain user is in the userType collection*/
    fun confirmUserExistsInCollection(userId: String, userType: String, onUserNameReturned: (result: Result<Boolean>) -> Unit ){

        fireStore.collection("${userType}s")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()){
                    onUserNameReturned.invoke(Success(true))
                    /*
                    var userDetailsName = ""
                    if (collection == DOCTOR_COLLECTION){
                        userDetailsName = documentSnapshot.toObject(DoctorsDetails::class.java)!!.docName
                    }else if (collection == PATIENT_COLLECTION){
                        userDetailsName = documentSnapshot.toObject(PatientsDetails::class.java)!!.patientName
                    }
                    onUserNameReturned.invoke(Success(userDetailsName))*/
                }else{
                    onUserNameReturned.invoke(Success(false))
                }
            }
            .addOnFailureListener { exception->
                onUserNameReturned.invoke(
                    Failure(exception)
                )
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