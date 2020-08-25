package com.janewaitara.medec.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.janewaitara.medec.R
import com.janewaitara.medec.common.extensions.isVisible
import com.janewaitara.medec.common.extensions.onClick
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val registerViewModel: RegisterViewModel by viewModel()
    private var arrayAdapter: ArrayAdapter<CharSequence>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupSpinner()
        setOnClickListener()
        subscribeToData()
    }

    private fun setupSpinner() {
       /* arrayAdapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item, R.array.userRoles)*/

        arrayAdapter = ArrayAdapter.createFromResource(this,R.array.userRoles, android.R.layout.simple_spinner_dropdown_item)
       /* (arrayAdapter as ArrayAdapter<CharSequence>).setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

 */       register_UserRoles_spinner.adapter = arrayAdapter
        register_UserRoles_spinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
       Toast.makeText(this,"Nothing selected",Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }


    private fun setOnClickListener() {
        btn_register.onClick {
            val userName = register_fullname.text.toString()
            val email = register_email.text.toString().trim()
            val password = register_password.text.toString().trim()
            registerViewModel.validateCredentials(userName, email, password)
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
            UserNameIsEmpty -> setUserNameEmptyError(true)
            UserNameIsNotEmpty ->  setUserNameEmptyError(false)
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

    private fun setUserNameEmptyError(setError: Boolean) {

        showLoading(false)
        if (setError){
            register_nameWrapper.error = getString(R.string.user_name_empty_error)
        }else  {
            register_nameWrapper.isErrorEnabled = false
        }

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