package com.janewaitara.medec.common.utils

import com.google.android.material.textfield.TextInputLayout

interface Validator {

    fun setCredentials(email: String, password: String)

    fun isEmailEmpty(): Boolean

    fun isPasswordEmpty(): Boolean

    fun isPasswordValid(): Boolean

    fun areCredentialsValid(): Boolean

}