package com.janewaitara.medec.ui.patients.account

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.janewaitara.medec.R
import com.janewaitara.medec.common.extensions.onClick
import com.janewaitara.medec.model.PatientsDetails
import com.janewaitara.medec.model.result.PatientDetailsSuccessResult
import com.janewaitara.medec.model.result.ResultResponseError
import com.janewaitara.medec.model.result.ResultResponseViewState
import com.janewaitara.medec.model.result.UserProfileImageUrlReturned
import com.janewaitara.medec.ui.patients.home.HomeViewModel
import kotlinx.android.synthetic.main.doctors_near_by.view.*
import kotlinx.android.synthetic.main.fragment_account.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {

    private val accountsViewModel: AccountsViewModel by viewModel()
    private val imageRequestCode = 438
    private var imageUri: Uri? = null
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId =  FirebaseAuth.getInstance().currentUser!!.uid
        subscribeToData()
        uploadProfile()
    }

    private fun subscribeToData() {
        accountsViewModel.getPatientDetailsFromFireStore(userId!!)
        
        accountsViewModel.getPatientDetailsLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
            resultResponseViewState?.let { responseViewState->
                onResultViewStateChanged(responseViewState)
            }
        })
    }

    private fun uploadProfile() {
        patient_image.onClick {
            pickImage()
        }
    }

    private fun pickImage() {
        var intent = Intent()
        intent.type  =  "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, imageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == imageRequestCode && resultCode == Activity.RESULT_OK && data?.data != null ){
            imageUri = data.data

            Toast.makeText(activity, "Uploading...", Toast.LENGTH_LONG).show()
            uploadToFirebaseStorage()
        }
        else{
            Toast.makeText(activity, "Failed to upload...", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadToFirebaseStorage() {

        loadingProgressBar.visibility = View.VISIBLE

        if (imageUri != null){

            accountsViewModel.uploadPatientUserProfile(userId!!, imageUri!!)
            Toast.makeText(activity, "Uploading to Storage...", Toast.LENGTH_LONG).show()

            accountsViewModel.getImageUrlLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
                resultResponseViewState?.let { resultViewState->
                    onResultViewStateChanged(resultViewState)

                }
            })
        }
    }

    private fun onResultViewStateChanged(resultViewState: ResultResponseViewState) {
        when(resultViewState){
            is UserProfileImageUrlReturned -> updateUserDetails(resultViewState.imageUrl)
            is PatientDetailsSuccessResult -> displayUserDetails(resultViewState.data)
            is ResultResponseError -> showErrorMessage(resultViewState.error)
        }
    }

    private fun showErrorMessage(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
    }

    private fun displayUserDetails(patientDetails: PatientsDetails) {
        patient_name.text = patientDetails.patientName
        patient_contacts.text = patientDetails.patientContact.toString()
        Glide.with(patient_image.context)
            .load(patientDetails.patientImageUrl)
            .into(patient_image)
    }

    private fun updateUserDetails(imageUrl: String) {
        var patientsDetails = PatientsDetails().apply {
            patId = userId!!
            patientImageUrl = imageUrl
        }

        accountsViewModel.updatePatientProfileImage(patientsDetails)
        loadingProgressBar.visibility = View.GONE

    }

}