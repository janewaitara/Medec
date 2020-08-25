package com.janewaitara.medec.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.janewaitara.medec.common.utils.CredentialsValidator

class RegisterViewModel(
    private val credentialsValidator: CredentialsValidator
): ViewModel() {

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


