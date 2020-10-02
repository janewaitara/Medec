package com.janewaitara.medec.ui.patients.allDoctors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.janewaitara.medec.R
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.result.DoctorsListSuccessResult
import com.janewaitara.medec.model.result.ResultResponseError
import com.janewaitara.medec.model.result.ResultResponseViewState
import com.janewaitara.medec.ui.patients.home.DoctorsRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_all_doctors.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllDoctorsFragment : Fragment(), AllDoctorsAdapter.DoctorDetailsClickListener, AllDoctorsAdapter.SendTextMessageClickListener {
    private val allDoctorsViewModel: AllDoctorsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_doctors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToData()
    }

    private fun subscribeToData() {
        allDoctorsViewModel.getDoctorsListFromFireStore()

        allDoctorsViewModel.getDoctorsListLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
            resultResponseViewState?.let { resultViewState ->
                onResultViewStateChanged(resultViewState)
            }
        })
    }

    private fun onResultViewStateChanged(resultResponseViewState: ResultResponseViewState) {
        when(resultResponseViewState){
            is DoctorsListSuccessResult -> populateRecyclerView(resultResponseViewState.data)
            is ResultResponseError -> showErrorMessage(resultResponseViewState.error)
        }
    }

    private fun showErrorMessage(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }

    private fun populateRecyclerView(doctorsList: List<DoctorsDetails>) {
        doctors_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = AllDoctorsAdapter(this, this)
        doctors_list.adapter = adapter

        adapter.setDoctorsList(doctorsList)
    }

    override fun doctorDetailsClickListener(doctorId: String) {
        //navigate to doctors details
    }

    override fun sendDoctorTextMessageClickListener(
        doctorId: String,
        doctorName: String,
        doctorProfileImage: String
    ) {
       view.let {
           val action = AllDoctorsFragmentDirections.actionAllDoctorsToChatMessagingFragment(doctorId, doctorName, doctorProfileImage)
           findNavController().navigate(action)
       }
    }
}