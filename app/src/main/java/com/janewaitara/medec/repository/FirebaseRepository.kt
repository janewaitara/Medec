package com.janewaitara.medec.repository

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.janewaitara.medec.App
import com.janewaitara.medec.model.*
import com.janewaitara.medec.model.result.EmptySuccess
import com.janewaitara.medec.model.result.Failure
import com.janewaitara.medec.model.result.Result
import com.janewaitara.medec.model.result.Success


class FirebaseRepository(var fireStore: FirebaseFirestore, var firebaseStorage: FirebaseStorage) {

    var patientsImagesStorageReference = firebaseStorage.reference.child(PATIENTS_PROFILE_IMAGE)

    val chatChannelCollectionRef = fireStore.collection("chatChannels")

    var confirmedUserType: String = ""

    companion object {
        const val DOCTOR_COLLECTION = "doctors"
        const val PATIENT_COLLECTION = "patients"
        const val PATIENTS_PROFILE_IMAGE = "Patients Profile Images"
        const val DOCTORS_PROFILE_IMAGE = "Doctors Profile Images"
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
     * Get nearBy doctors */
    fun getNearByDoctorsFromFireStore(
        userLocation: String,
        onDoctorsReturned: (result: Result<List<DoctorsDetails>>) -> Unit
    ) {
        fireStore.collection(DOCTOR_COLLECTION)
            .whereEqualTo("docLocation", userLocation)
            .get()
            .addOnSuccessListener { querySnapsShot ->
                if (querySnapsShot != null) {
                    val nearByDoctors = querySnapsShot.toObjects(DoctorsDetails::class.java)
                    onDoctorsReturned.invoke(
                        Success(nearByDoctors)
                    )
                } else {
                    onDoctorsReturned.invoke(
                        EmptySuccess("There are no doctors near you")
                    )
                }
            }
            .addOnFailureListener { exception ->
                onDoctorsReturned.invoke(
                    Failure(exception)
                )
            }
    }

    /**
     * Get all doctors details*/
    fun getDoctorsFromFireStore(onDoctorsReturned: (result: Result<List<DoctorsDetails>>) -> Unit) {
        fireStore.collection(DOCTOR_COLLECTION)
            .addSnapshotListener { value, error ->
                error?.let { exception ->
                    onDoctorsReturned.invoke(
                        Failure(
                            exception
                        )
                    )
                }
                value.let { taskQuerySnapShot ->
                    if (taskQuerySnapShot != null) {
                        var doctorsList = taskQuerySnapShot.toObjects(DoctorsDetails::class.java)
                        onDoctorsReturned.invoke(
                            Success(
                                doctorsList
                            )
                        )
                    } else {
                        Toast.makeText(App.getAppContext(), "Document is empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        /*    .get()
            .addOnSuccessListener { taskQuerySnapShot ->
                if (taskQuerySnapShot != null) {
                    var doctorsList = taskQuerySnapShot.toObjects(DoctorsDetails::class.java)
                    onDoctorsReturned.invoke(
                        Success(
                            doctorsList
                        )
                    )
                } else {
                    Toast.makeText(App.getAppContext(), "Document is empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .addOnFailureListener { exception ->
                onDoctorsReturned.invoke(
                    Failure(
                        exception
                    )
                )
            }*/
    }

    /**
     * used to confirm whether a certain user is in the userType collection*/
    fun confirmUserExistsInCollection(
        userId: String,
        userType: String,
        onUserNameReturned: (result: Result<Boolean>) -> Unit
    ) {
        fireStore.collection("${userType}s")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    onUserNameReturned.invoke(Success(true))

                    this.confirmedUserType = userType
                } else {
                    onUserNameReturned.invoke(Success(false))
                }
            }
            .addOnFailureListener { exception ->
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
     * Updating Patients Location Details*/
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

    /**
     * get realtime patient Detail*/
    fun getPatientsDetails(
        userId: String,
        onPatientsDetailsReturned: (result: Result<PatientsDetails>) -> Unit
    ) {
        fireStore.collection(PATIENT_COLLECTION)
            .document(userId)
            .addSnapshotListener { value, error ->
                error?.let { exception ->
                    onPatientsDetailsReturned.invoke(
                        Failure(exception)
                    )
                }
                value?.let { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userDetails = documentSnapshot.toObject(PatientsDetails::class.java)
                        onPatientsDetailsReturned.invoke(Success(userDetails!!))

                    } else {
                        onPatientsDetailsReturned.invoke(
                            EmptySuccess("The user does not exist ")
                        )
                    }
                }
            }

    }

    fun uploadPatientsUserProfile(
        userId: String,
        imageUri: Uri,
        onDownloadUriReturned: (result: Result<String>) -> Unit
    ) {
        var imageReference = patientsImagesStorageReference.child("{$userId}.jpg")

        var uploadTask = imageReference.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@continueWithTask imageReference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val url = task.result.toString()
                    //overwriteUserDetails
                    onDownloadUriReturned.invoke(Success(url))
                }
            }
    }

    /**
     * Updating Patients profile Image*/
    fun updatePatientProfileImage(patientsDetails: PatientsDetails) {
        fireStore.collection(PATIENT_COLLECTION)
            .document(patientsDetails.patId)
            .update("patientImageUrl", patientsDetails.patientImageUrl)
            .addOnSuccessListener {
                Log.d("FireStore", "Updated location for: ${patientsDetails.patientName}")
            }
            .addOnFailureListener {
                Log.d("FireStore", "Error updating location details to firestore  with error: $it")
            }
    }

    /**
     * Send message to user*/
    fun sendMessage() {

    }

    /**
     * */
    fun getOrCreateChatChannel(
        userId: String,
        messageReceiverId: String,
        onComplete: (channelId: Result<String>) -> Unit
    ) {
        /**we need to know in which chat channels one is
         * chatting from hence we add a collection to
         * each user for chat channels*/

        val currentUserReference = if (confirmedUserType == "doctor") {
            fireStore.collection(DOCTOR_COLLECTION)
                .document(userId)
        } else {
            fireStore.collection(PATIENT_COLLECTION)
                .document(userId)
        }

        val messageRecipientReference = if (confirmedUserType == "doctor") {
            fireStore.collection(PATIENT_COLLECTION)
                .document(messageReceiverId)
        } else {
            fireStore.collection(DOCTOR_COLLECTION)
                .document(messageReceiverId)

        }

        currentUserReference
            .collection("engagedChatChannels")
            .document(messageReceiverId)
            .get()
            .addOnSuccessListener { documentSnapShot ->
                if (documentSnapShot.exists()) {
                    //meaning we are already chatting with recipient
                    onComplete.invoke(Success((documentSnapShot["channelId"] as String)))
                    return@addOnSuccessListener
                } else {
                    //if doesn't exists, we create it
                    val newChannel = chatChannelCollectionRef.document()
                    newChannel.set(ChatChannel(mutableListOf(userId, messageReceiverId)))

                    //saving channel id to user who will chat together
                    currentUserReference
                        .collection("engagedChatChannels")
                        .document(messageReceiverId)
                        .set(mapOf("channelId" to newChannel.id))

                    //saving the channel to the messageRecipient engagedChatChannels
                   messageRecipientReference
                        .collection("engagedChatChannels")
                        .document(userId)
                        .set(mapOf("channelId" to newChannel.id))

                    onComplete.invoke(
                        Success(
                            (newChannel.id)
                        )
                    )
                }
            }
            .addOnFailureListener { exception ->
                onComplete.invoke(
                    Failure(
                        exception
                    )
                )
            }
    }

    /**
     * Listening for all Messages inside a channel*/

    fun addChatMessageListener(
        channelId: String,
        onChatsReturned: (result: Result<List<TextMessage>>) -> Unit
    ): ListenerRegistration {
        return chatChannelCollectionRef.document(channelId).collection("messages").orderBy("time")
            .addSnapshotListener { value, error ->
                error?.let { firebaseFirestoreException ->
                    onChatsReturned.invoke(
                        Failure(
                            firebaseFirestoreException
                        )
                    )
                    Log.e("FIRESTORE", "ChatMessageListener error", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                value?.let { querySnapShot ->
                    val items = mutableListOf<TextMessage>()
                    querySnapShot.documents.forEach { documentSnapShot ->
                        if (documentSnapShot["type"] == MessageType.TEXT) {
                            // items.add(documentSnapShot.toObject(TextMessage::class.java))
                            documentSnapShot.toObject(TextMessage::class.java)
                                ?.let { items.add(it) }
                        } else {
                            //if type of msg is image
                            TODO("Add Image message")
                        }
                    }
                    onChatsReturned.invoke(
                        Success(items)
                    )
                }
            }
    }


}