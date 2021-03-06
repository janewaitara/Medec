package com.janewaitara.medec.ui.authentication.register

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
import com.janewaitara.medec.common.extensions.isVisible
import com.janewaitara.medec.common.extensions.onClick
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.PatientsDetails
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val registerViewModel: RegisterViewModel by viewModel()
    private var arrayAdapter: ArrayAdapter<CharSequence>? = null
    private lateinit var mFirebaseAuth: FirebaseAuth
    private var userId: String? = null
    private lateinit var userType: String


    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
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

 */     register_UserRoles_spinner.adapter = arrayAdapter
        register_UserRoles_spinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
       Toast.makeText(activity,"Nothing selected",Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        userType = parent!!.getItemAtPosition(position).toString()
        Toast.makeText(activity, "You selected $userType", Toast.LENGTH_SHORT).show()
    }

    private fun setOnClickListener() {
        btn_register.onClick {
            val userName = register_fullname.text.toString()
            val email = register_email.text.toString().trim()
            val password = register_password.text.toString().trim()

            registerViewModel.validateCredentials(userName, email, password)

        }
        registered_login.onClick {
            view?.let {
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                it.findNavController().navigate(action)
            }
        }
    }
    private fun subscribeToData() {
        registerViewModel.getRegisterViewState().observe(viewLifecycleOwner, Observer{ registerViewState ->
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
        showLoading(true)

        val userName = register_fullname.text.toString()
        val email = register_email.text.toString().trim()
        val password = register_password.text.toString().trim()
        val phoneContact = register_phone.text.toString().trim()

        mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){
               // showLoading(false)
               Toast.makeText(activity, "User Created Successfully", Toast.LENGTH_SHORT).show()

                userId = mFirebaseAuth.currentUser?.uid
                //userType = register_UserRoles_spinner.selectedItem.toString()

                if (userType == "Doctor"){
                    var doctorsDetails =  DoctorsDetails().apply {
                        docId = userId!!
                        docName = userName
                        doctorEmailAddress = email
                        doctorContact = phoneContact.toInt()
                    }

                    registerViewModel.saveDoctorDetails(doctorsDetails)

                }else if (userType == "Patient"){
                    var patientsDetails = PatientsDetails().apply {
                        patId = userId!!
                        patientName = userName
                        patientEmailAddress = email
                        patientContact = phoneContact.toInt()
                    }
                    registerViewModel.savePatientDetails(patientsDetails)
                }
            }else{
                // If sign in fails, display a message to the user.
               // showLoading(false)
                Toast.makeText(activity, "Authentication failed." + task.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }

        view?.let {

            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            it.findNavController().navigate(action)
        }

    }
}