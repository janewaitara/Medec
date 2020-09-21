package com.janewaitara.medec.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janewaitara.medec.common.utils.CredentialsValidator
import com.janewaitara.medec.model.result.*
import com.janewaitara.medec.repository.FirebaseRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val credentialsValidator: CredentialsValidator, private val firebaseRepository: FirebaseRepository
) : ViewModel(){

    private val loginViewState = MutableLiveData<LoginViewState>()

    fun getLoginViewState(): LiveData<LoginViewState> = loginViewState

    private val userExistsMutableLiveData  = MutableLiveData<ResultResponseViewState>()
    fun getUserExistenceLiveData() = userExistsMutableLiveData

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

     fun confirmUserExistsInFireStoreCollection(userId: String, userType: String){
        viewModelScope.launch {
            firebaseRepository.confirmUserExistsInCollection(userId, userType) { result->
                when(result){
                    is Success -> {
                        userExistsMutableLiveData.postValue(UserDocumentExists(result.data))
                    }
                    is Failure -> {
                        userExistsMutableLiveData.postValue(ResultResponseError(result.error.localizedMessage ?: "" ))
                    }
                }
            }
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

