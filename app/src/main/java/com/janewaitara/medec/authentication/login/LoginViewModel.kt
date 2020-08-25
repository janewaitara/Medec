package com.janewaitara.medec.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.janewaitara.medec.common.utils.CredentialsValidator

class LoginViewModel(private val credentialsValidator: CredentialsValidator
) : ViewModel(){

    private val loginViewState = MutableLiveData<LoginViewState>()

    fun getLoginViewState(): LiveData<LoginViewState> = loginViewState

    fun validateCredentials(email: String, password: String){
        loginViewState.value = Loading

        credentialsValidator.setLoginCredentials(email, password)
        checkIfLoginEmailIsEmpty()
        checkIfLoginPasswordIsEmpty()
        checkLoginPasswordValidity()

        if (credentialsValidator.areLoginCredentialsValid()){
            loginViewState.value = LoginUser
        }
    }

    private fun checkIfLoginEmailIsEmpty(){
        if (credentialsValidator.isEmailEmpty()){
            loginViewState.value  =
                EmptyEmail
        }else{
            loginViewState.value =
                EmailNotEmpty
        }
    }
    private fun checkIfLoginPasswordIsEmpty(){
        if (credentialsValidator.isPasswordEmpty()){
            loginViewState.value =
                PasswordEmpty
        }else{
            loginViewState.value =
                PassWordNotEmpty
        }
    }

    private fun checkLoginPasswordValidity(){
        if (!credentialsValidator.isPasswordValid()){
            loginViewState.value =
                InValidPassWord
        }else{
            loginViewState.value =
                ValidPassWord
        }
    }

}
sealed class LoginViewState
object LoginUser: LoginViewState()
object Loading: LoginViewState()
object EmptyEmail: LoginViewState()
object EmailNotEmpty: LoginViewState()
object ValidPassWord: LoginViewState()
object InValidPassWord: LoginViewState()
object PasswordEmpty: LoginViewState()
object PassWordNotEmpty: LoginViewState()

