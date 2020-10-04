package com.janewaitara.medec.ui.doctors.accounts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.janewaitara.medec.R
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.result.DoctorDetailsSuccessResult
import com.janewaitara.medec.model.result.EmptySuccessMessage
import com.janewaitara.medec.model.result.ResultResponseError
import com.janewaitara.medec.model.result.ResultResponseViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

class DoctorsAccountFragment : Fragment() {

    private var userId: String? = null

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
    }

    private fun subscribeToData() {
        doctorsAccountViewModel.getDoctorsDetails(userId!!)

        doctorsAccountViewModel.getDoctorsDetailsLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
            resultResponseViewState?.let { responseViewState->
                onResultViewStateChanged(responseViewState)
            }
        })
    }

    private fun onResultViewStateChanged(responseViewState: ResultResponseViewState) {
        when(responseViewState) {
            is DoctorDetailsSuccessResult -> displayDoctorsDetails(responseViewState.data)
            is ResultResponseError -> {}
            is EmptySuccessMessage -> {}
        }
    }

    private fun displayDoctorsDetails(doctorsDetails: DoctorsDetails) {
        TODO("Add doctors layout")
    }


}