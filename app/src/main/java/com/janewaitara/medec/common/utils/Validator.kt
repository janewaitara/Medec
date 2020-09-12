package com.janewaitara.medec.common.utils

import com.google.android.material.textfield.TextInputLayout

interface Validator {

    fun setLoginCredentials(email: String, password: String)

    fun setRegisterCredentials(userName: String, email: String, password: String)

    fun isUserNameEmpty(): Boolean

    fun isEmailEmpty(): Boolean

    fun isPasswordEmpty(): Boolean

    fun isPasswordValid(): Boolean

    fun areCredentialsValid(): Boolean

    fun areLoginCredentialsValid(): Boolean

}