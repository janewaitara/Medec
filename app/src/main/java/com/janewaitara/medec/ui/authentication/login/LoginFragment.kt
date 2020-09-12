package com.janewaitara.medec.ui.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.janewaitara.medec.R
import com.janewaitara.medec.common.extensions.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val loginViewModel: LoginViewModel by viewModel()
    private var arrayAdapter: ArrayAdapter<CharSequence>? = null
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var userType: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setOnClickListener()
        subscribeToData()

        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    private fun setupSpinner() {
        arrayAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.userRoles, android.R.layout.simple_spinner_dropdown_item)
        /* (arrayAdapter as ArrayAdapter<CharSequence>).setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

  */    login_UserRoles_spinner.adapter = arrayAdapter
        login_UserRoles_spinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(activity,"Nothing selected", Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        userType = parent!!.getItemAtPosition(position).toString()
        Toast.makeText(activity, "You selected $userType", Toast.LENGTH_SHORT).show()
    }


    private fun setOnClickListener() {
        btn_login.onClick {
            val email = login_email.text.toString().trim()
            val password = login_password.text.toString().trim()
            loginViewModel.validateCredentials(email, password)
        }

        login_register.onClick {
           view?.let {
               val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
               it.findNavController().navigate(action)
           }
        }

        forgot_password.onClick {

        }
    }

    private fun subscribeToData() {
        loginViewModel.getLoginViewState().observe(viewLifecycleOwner, Observer{ loginViewState->

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
                Toast.makeText(activity, "Logged in successfully", Toast.LENGTH_SHORT).show()

                view?.let {
                    var action = LoginFragmentDirections.actionLoginFragmentToLocationFragment(userType)
                    it.findNavController().navigate(action)
                }

            }else{
                showLoading(false)
                Toast.makeText(activity, "Login failed." + task.exception?.message,
                    Toast.LENGTH_SHORT).show();

            }

        }
    }

}