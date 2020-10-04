package com.janewaitara.medec.ui.doctors.accounts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.janewaitara.medec.R
import com.janewaitara.medec.common.extensions.isVisible
import com.janewaitara.medec.common.extensions.onClick
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.result.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_doctors_account.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DoctorsAccountFragment : Fragment() {

    private var userId: String? = null
    private var docImageUri: Uri? = null
    private val imageRequestCode = 438
    private val doctorsAccountViewModel: DoctorsAccountsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctors_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId =  FirebaseAuth.getInstance().currentUser!!.uid

        subscribeToData()
        uploadProfileImage()
        edit_doc_profile_button.onClick {
            edit_doc_details_form.isVisible(true)
        }
    }

    private fun subscribeToData() {
        doctorsAccountViewModel.getDoctorsDetails(userId!!)

        doctorsAccountViewModel.getDoctorsDetailsLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
            resultResponseViewState?.let { responseViewState->
                onResultViewStateChanged(responseViewState)
            }
        })
    }
    private fun uploadProfileImage() {
        account_doc_image.onClick {
            pickImageFromLocalStrorage()
        }
    }

    private fun pickImageFromLocalStrorage() {
        var intent = Intent()
        intent.type  =  "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, imageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imageRequestCode && resultCode == Activity.RESULT_OK && data != null){
           docImageUri = data.data

            Toast.makeText(activity, "Uploading...", Toast.LENGTH_SHORT).show()

            uploadImageToFirebaseStorage()
        }else{
            Toast.makeText(activity, "Failed to upload...", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadImageToFirebaseStorage() {
        if (docImageUri != null){
            doctorsAccountViewModel.uploadDoctorsProfileImageToStorage(userId!!, docImageUri!!)

            Toast.makeText(activity, "Uploading to Storage...", Toast.LENGTH_LONG).show()
            doctorsAccountViewModel.getDocProfileImageUrlLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
                resultResponseViewState?.let { resultViewState->
                    onResultViewStateChanged(resultViewState)
                }
            })
        }
    }

    private fun onResultViewStateChanged(responseViewState: ResultResponseViewState) {
        when(responseViewState) {
            is DoctorDetailsSuccessResult -> displayDoctorsDetails(responseViewState.data)
            is UserProfileImageUrlReturned -> updateDoctorDetails(responseViewState.imageUrl)
            is UserProfilePicUpdateSuccessResult -> showProfileUpdatedSuccess(responseViewState.message)
            is ResultResponseError -> {}
            is EmptySuccessMessage -> {}
        }
    }

    private fun updateDoctorDetails(imageUrl: String) {
            var doctorsDetails = DoctorsDetails().apply {
                docId = userId!!
                docImageUrl = imageUrl
            }
        doctorsAccountViewModel.updateDocProfileImage(doctorsDetails)
        doctorsAccountViewModel.getDocProfileUpdatedLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
            resultResponseViewState?.let { resultViewState->
                onResultViewStateChanged(resultViewState)
            }
        })
    }

    private fun displayDoctorsDetails(doctorsDetails: DoctorsDetails) {
        Toast.makeText(activity, "Displaying Details", Toast.LENGTH_SHORT).show()

        Glide.with(account_doc_image.context)
            .load(doctorsDetails.docImageUrl)
            .into(account_doc_image)

        account_doc_name.text = doctorsDetails.docName
        account_doc_contacts.text = doctorsDetails.doctorContact.toString()
        account_doc_title.text = doctorsDetails.doctorsTitle
        account_doc_years_ofExperience.text = doctorsDetails.yearsOfExperience

    }

    private fun showProfileUpdatedSuccess(message: String) {
       Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }


}