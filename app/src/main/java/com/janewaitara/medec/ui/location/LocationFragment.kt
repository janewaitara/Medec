package com.janewaitara.medec.ui.location

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.janewaitara.medec.R
import com.janewaitara.medec.model.County
import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.PatientsDetails
import com.janewaitara.medec.ui.authentication.register.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_location.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.io.InputStream


class LocationFragment : Fragment(), CountyAdapter.CountyClickListener {

    private val locationViewModel: LocationViewModel by viewModel()
    private val countyAdapter = CountyAdapter(this)
    private lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var counties: List<County>
    private lateinit var userType: String
    private var userId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{

          userType = LocationFragmentArgs.fromBundle(it).userType

        }

        mFirebaseAuth = FirebaseAuth.getInstance()
        setUpCountyRecyclerView()
        addCountiesFromJson()
    }

    private fun addCountiesFromJson() {

        try {
            val jsonString = requireActivity().assets.open("counties.json")
                .bufferedReader()
                .use { it.readText() }

            counties = Gson().fromJson(jsonString, Array<County>::class.java).toList()
            countyAdapter.setCounties(counties)

        }catch (ioException: IOException ) {
            ioException.printStackTrace()
            Log.e(requireActivity().toString(),"Unable to parse the JSON file.$ioException")
        }

    }

    private fun setUpCountyRecyclerView() {
        county_recyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        county_recyclerView.adapter = countyAdapter
    }

    override fun countyItemClicked(countyName: String) {

        userId = mFirebaseAuth.currentUser?.uid
        when(userType){
            "Doctor" -> {
                val doctorsDetails = DoctorsDetails().apply {
                    docId = userId!!
                    docLocation = countyName
                }
                locationViewModel.updateDoctorLocation(doctorsDetails)

                Toast.makeText(activity, "Updated User Location", Toast.LENGTH_LONG).show()
            }
            "Patient" -> {
                val patientsDetails = PatientsDetails().apply {
                    patId = userId!!
                    patientLocation = countyName
                }
                locationViewModel.updatePatientLocation(patientsDetails)

                Toast.makeText(activity, "Updated User Location", Toast.LENGTH_LONG).show()

                /**
                 * navigate to Patients page*/
                view?.let {
                    val action = LocationFragmentDirections.actionLocationFragmentToPatientsFragment()
                    it.findNavController().navigate(action)
                }
            }
        }

    }

}