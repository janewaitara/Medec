package com.janewaitara.medec.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.janewaitara.medec.R
import com.janewaitara.medec.ui.authentication.register.RegisterActivity
import com.janewaitara.medec.common.extensions.*
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val loginViewModel: LoginViewModel by viewModel()
    private var arrayAdapter: ArrayAdapter<CharSequence>? = null
    private lateinit var mFirebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupSpinner()
        setOnClickListener()
        subscribeToData()

        mFirebaseAuth = FirebaseAuth.getInstance()

    }

    private fun setupSpinner() {
        arrayAdapter = ArrayAdapter.createFromResource(this,R.array.userRoles, android.R.layout.simple_spinner_dropdown_item)
        /* (arrayAdapter as ArrayAdapter<CharSequence>).setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

  */    login_UserRoles_spinner.adapter = arrayAdapter
        login_UserRoles_spinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this,"Nothing selected", Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }


    private fun setOnClickListener() {
        btn_login.onClick {
            val email = login_email.text.toString().trim()
            val password = login_password.text.toString().trim()
            loginViewModel.validateCredentials(email, password)
        }

        login_register.onClick {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }

        forgot_password.onClick {

        }
    }

    private fun subscribeToData() {
        loginViewModel.getLoginViewState().observe(this, Observer{ loginViewState->

            loginViewState?.let {
                onLoginViewStateChanged(it)
            }
        })
    }

    private fun onLoginViewStateChanged(loginViewState: LoginViewState) {
        when(loginViewState){
            Loading -> showLoading(true)
            LoginUser -> loginUser()
            EmptyEmail -> setEmailEmptyError(true)
            EmailNotEmpty -> setEmailEmptyError(false)
            ValidPassWord -> setPassWordValidityError(true)
            InValidPassWord -> setPassWordValidityError(false)
            PasswordEmpty -> setPassWordEmptyError(true)
            PassWordNotEmpty -> setPassWordEmptyError(false)

        }
    }

    private fun showLoading(isLoading: Boolean) {
        login_progress_bar.isVisible(isLoading)
    }

    private fun setEmailEmptyError(setError: Boolean) {
        showLoading(false)

        if (setError){
            login_emailWrapper.error = getString(R.string.email_empty_error)
        }else{
            login_emailWrapper.isErrorEnabled = false
        }
    }

    private fun setPassWordEmptyError(setError: Boolean) {
        showLoading(false)

        if (setError) {
            login_passwordWrapper.error = getString(R.string.password_empty_error)
        }else{
            login_passwordWrapper.isErrorEnabled = false
        }
    }

    private fun setPassWordValidityError(setError: Boolean) {
        showLoading(false)

        if (setError){
           login_passwordWrapper.error = getString(R.string.password_length_error)
        }else{
            login_passwordWrapper.isErrorEnabled = false
        }
    }

    private fun loginUser() {
        showLoading(true)

        val loginEmail = login_email.text.toString().trim()
        val loginPassword = login_password.text.toString().trim()

        mFirebaseAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener { task ->
            if (task.isSuccessful){
                showLoading(false)
                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
            }else{
                showLoading(false)
                Toast.makeText(this, "Login failed." + task.exception?.message,
                    Toast.LENGTH_SHORT).show();

            }

        }
    }

}