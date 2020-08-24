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

    fun validateCredentials(password: String, passwordWrapper: TextInputLayout){
        registerViewState.value = Loading

        viewModelScope.launch {
           val passwordIsValid = credentialsValidator.validatePassword(password, passwordWrapper)

        }
    }

}

sealed class RegisterViewState
data class UserRegistered(val isRegistered: Boolean): RegisterViewState()
object Loading: RegisterViewState()
