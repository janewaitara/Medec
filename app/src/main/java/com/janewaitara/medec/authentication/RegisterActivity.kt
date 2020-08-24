package com.janewaitara.medec.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.janewaitara.medec.R
import com.janewaitara.medec.common.extensions.isVisible
import com.janewaitara.medec.common.extensions.onClick
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setOnClickListener()
        subscribeToData()
    }

    private fun setOnClickListener() {
        btn_register.onClick {
            val email = register_email.text.toString().trim()
            val password = register_password.text.toString().trim()
            registerViewModel.validateCredentials(email, password)
        }
    }
    private fun subscribeToData() {
        registerViewModel.getRegisterViewState().observe(this, Observer{ registerViewState ->
            registerViewState?.let { registerViewState ->
                onRegisterViewStateChanged(registerViewState)
            }
        })
    }

    private fun onRegisterViewStateChanged(registerViewState: RegisterViewState) =
        when(registerViewState){

            Loading -> showLoading(true)
            EmptyEmail -> setEmailEmptyError(true)
            EmailNotEmpty -> setEmailEmptyError(false)
            PasswordEmpty -> setPassWordEmptyError(true)
            PassWordNotEmpty -> setPassWordEmptyError(false)
            InValidPassWord -> setPassWordValidityError(true)
            ValidPassWord -> setPassWordValidityError(false)
            RegisterUser -> registerUser()

        }


    private fun showLoading(isLoading: Boolean) {
        register_progress_bar.isVisible(isLoading)
    }

    private fun setEmailEmptyError(setError: Boolean) {
        showLoading(false)

        if (setError){
            register_emailWrapper.error = getString(R.string.email_empty_error)
        }else{
            register_emailWrapper.isErrorEnabled = false
        }
    }

    private fun setPassWordEmptyError(setError: Boolean) {
        showLoading(false)

        if (setError) {
            register_passwordWrapper.error = getString(R.string.password_empty_error)
        }else{
            register_passwordWrapper.isErrorEnabled = false
        }
    }

    private fun setPassWordValidityError(setError: Boolean) {
        showLoading(false)

        if (setError){
            register_passwordWrapper.error = getString(R.string.password_length_error)
        }else{
            register_passwordWrapper.isErrorEnabled = false
        }
    }

    private fun registerUser() {
        TODO("Not yet implemented")
    }
}