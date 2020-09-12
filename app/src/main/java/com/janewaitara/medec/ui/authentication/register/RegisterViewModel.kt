package com.janewaitara.medec.ui.authentication.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.janewaitara.medec.common.utils.CredentialsValidator
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.PatientsDetails
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val credentialsValidator: CredentialsValidator
): ViewModel() {

    private lateinit var fireStore : FirebaseFirestore

    init {
        fireStore = FirebaseFirestore.getInstance()
        fireStore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    private val registerViewState = MutableLiveData<RegisterViewState>()

    fun getRegisterViewState(): LiveData<RegisterViewState> = registerViewState

    fun validateCredentials(userName: String,email: String, password: String){
        registerViewState.value =
            Loading

        credentialsValidator.setRegisterCredentials(userName, email, password)
        checkIfUserNameIsEmpty()
        checkIfEmailIsEmpty()
        checkIfPasswordIsEmpty()
        checkPasswordValidity()
        if (credentialsValidator.areCredentialsValid()){
            registerViewState.value =
                RegisterUser
        }

    }
    private fun checkIfUserNameIsEmpty(){
        if (credentialsValidator.isUserNameEmpty()){
            registerViewState.value  =
                UserNameIsEmpty
        }else{
            registerViewState.value =
                UserNameIsNotEmpty
        }
    }
    private fun checkIfEmailIsEmpty(){
        if (credentialsValidator.isEmailEmpty()){
            registerViewState.value  =
                EmptyEmail
        }else{
            registerViewState.value =
                EmailNotEmpty
        }
    }
    private fun checkIfPasswordIsEmpty(){
        if (credentialsValidator.isPasswordEmpty()){
            registerViewState.value =
                PasswordEmpty
        }else{
            registerViewState.value =
                PassWordNotEmpty
        }
    }

    private fun checkPasswordValidity(){
        if (!credentialsValidator.isPasswordValid()){
            registerViewState.value =
                InValidPassWord
        }else{
            registerViewState.value =
                ValidPassWord
        }
    }

    fun saveDoctorDetails(doctorsDetails: DoctorsDetails){
        viewModelScope.launch {
            fireStore.collection("doctors")
                .document(doctorsDetails.docId)
                .set(doctorsDetails)
                .addOnSuccessListener {
                    Log.d("FireStore", "User profile created for: ${doctorsDetails.docName}")
                }
                .addOnFailureListener {
                    Log.d("FireStore", "Error adding document to firestore  with error: $it")
                }
        }
    }

    fun savePatientDetails(patientsDetails: PatientsDetails){
        viewModelScope.launch {
            fireStore.collection("patients")
                .document(patientsDetails.patId)
                .set(patientsDetails)
                .addOnSuccessListener {
                    Log.d("FireStore", "User profile created for: ${patientsDetails.patientName}")
                }
                .addOnFailureListener {
                    Log.d("FireStore", "Error adding document to firestore  with error: $it")
                }
        }
    }
}

sealed class RegisterViewState
object RegisterUser: RegisterViewState()
object Loading: RegisterViewState()
object UserNameIsEmpty: RegisterViewState()
object UserNameIsNotEmpty: RegisterViewState()
object EmptyEmail: RegisterViewState()
object EmailNotEmpty: RegisterViewState()
object ValidPassWord: RegisterViewState()
object InValidPassWord: RegisterViewState()
object PasswordEmpty: RegisterViewState()
object PassWordNotEmpty: RegisterViewState()


