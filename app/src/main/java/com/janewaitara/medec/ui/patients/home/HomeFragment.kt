package com.janewaitara.medec.ui.patients.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.janewaitara.medec.R
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.result.DoctorsListSuccessResult
import com.janewaitara.medec.model.result.ResultResponseError
import com.janewaitara.medec.model.result.ResultResponseViewState
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(),DoctorsRecyclerAdapter.DoctorDetailsClickListener {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToData()
    }

    private fun subscribeToData() {
        homeViewModel.getDoctorsListFromFireStore()

        homeViewModel.getDoctorsListLiveData().observe(viewLifecycleOwner, Observer { resultResponseViewState ->
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
        rv_doctors_details.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val adapter = DoctorsRecyclerAdapter(this)
        rv_doctors_details.adapter = adapter

        adapter.setDoctorsList(doctorsList)
    }

    override fun doctorDetailsClickListener(doctorId: String) {
        TODO("Not yet implemented")
    }

}