package com.janewaitara.medec.common.utils

import com.google.android.material.textfield.TextInputLayout

interface Validator {

    fun setCredentials(email: String, password: String)

    fun isEmailEmpty(): Boolean

    fun isPasswordEmpty(): Boolean

    fun isPasswordValid(): Boolean

    fun areCredentialsValid(): Boolean

    fun validatePassword(password: String, passwordWrapper: TextInputLayout): Boolean
    fun validateEmail(email: String, emailWrapper: TextInputLayout): Boolean
}