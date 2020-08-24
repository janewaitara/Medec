package com.janewaitara.medec.common.utils

class CredentialsValidator: Validator {

    private lateinit var email: String
    private lateinit var password: String

    override fun setCredentials(email: String, password: String) {
        this.email = email
        this.password = password
    }

    override fun isEmailEmpty(): Boolean =  email.isEmpty()

    override fun isPasswordEmpty(): Boolean = password.isEmpty()

    override fun isPasswordValid(): Boolean  = password.length >= 6

    override fun areCredentialsValid(): Boolean = !isEmailEmpty() && !isPasswordEmpty() && isPasswordValid()

}