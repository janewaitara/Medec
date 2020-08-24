package com.janewaitara.medec.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputLayout
import com.janewaitara.medec.common.utils.Validator
import kotlinx.coroutines.launch


class RegisterViewModel(
    private val credentialsValidator: Validator
): ViewModel() {

    private val registerViewState = MutableLiveData<RegisterViewState>()

    fun getRegisterViewState(): LiveData<RegisterViewState> = registerViewState

    fun validateCredentials(email: String, password: String){
        registerViewState.value = Loading
        checkIfEmailIsEmpty()
        checkIfPasswordIsEmpty()
        checkPasswordValidity()
        if (credentialsValidator.areCredentialsValid()){
            registerViewState.value = RegisterUser
        }

    }

    private fun checkIfPasswordIsEmpty(){
        if (credentialsValidator.isPasswordEmpty()){
            registerViewState.value = PasswordEmpty
        }else{
            registerViewState.value = PassWordNotEmpty
        }
    }

    private fun checkIfEmailIsEmpty(){
        if (credentialsValidator.isEmailEmpty()){
            registerViewState.value  = EmptyEmail
        }else{
            registerViewState.value = EmailNotEmpty
        }
    }

    private fun checkPasswordValidity(){
        if (!credentialsValidator.isPasswordValid()){
            registerViewState.value = InValidPassWord
        }else{
            registerViewState.value = ValidPassWord
        }
    }
}

sealed class RegisterViewState
object RegisterUser: RegisterViewState()
object Loading: RegisterViewState()
object EmptyEmail: RegisterViewState()
object EmailNotEmpty: RegisterViewState()
object ValidPassWord: RegisterViewState()
object InValidPassWord: RegisterViewState()
object PasswordEmpty: RegisterViewState()
object PassWordNotEmpty: RegisterViewState()


