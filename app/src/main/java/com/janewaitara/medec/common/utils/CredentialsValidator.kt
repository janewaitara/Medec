package com.janewaitara.medec.common.utils

class CredentialsValidator: Validator {

    private lateinit var email: String
    private lateinit var userName: String
    private lateinit var password: String

    override fun setLoginCredentials(email: String, password: String) {
        this.email = email
        this.password = password
    }

    override fun setRegisterCredentials(userName: String, email: String, password: String) {
        this.userName = userName
        this.email = email
        this.password = password
    }

    override fun isUserNameEmpty(): Boolean  = userName.isEmpty()

    override fun isEmailEmpty(): Boolean =  email.isEmpty()

    override fun isPasswordEmpty(): Boolean = password.isEmpty()

    override fun isPasswordValid(): Boolean  = password.length >= 6

    override fun areCredentialsValid(): Boolean = !isUserNameEmpty() && !isEmailEmpty() && !isPasswordEmpty() && isPasswordValid()

    override fun areLoginCredentialsValid(): Boolean  =  !isEmailEmpty() && !isPasswordEmpty() && isPasswordValid()

}