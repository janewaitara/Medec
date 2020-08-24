package com.janewaitara.medec.common.utils

import com.google.android.material.textfield.TextInputLayout
import com.janewaitara.medec.App
import com.janewaitara.medec.R

class CredentialsValidator: Validator {
    override fun validatePassword(password: String, passwordWrapper: TextInputLayout): Boolean {
        return when{
            password.isEmpty() -> {
                passwordWrapper.error = App.getResources().getString(R.string.password_empty_error)
                false
            }
            password.length < 6 -> {
                passwordWrapper.error = App.getResources().getString(R.string.password_length_error)
                false
            }
            else -> {
                passwordWrapper.isErrorEnabled = false
                true
            }
        }
    }

    override fun validateEmail(email: String, emailWrapper: TextInputLayout): Boolean {
        return when {
            email.isEmpty() -> {
                emailWrapper.error = App.getResources().getString(R.string.email_empty_error)
                false
            }

            else -> {
                emailWrapper.isErrorEnabled = false
                true
            }
        }
    }
}