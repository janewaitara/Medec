package com.janewaitara.medec.ui.patients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.janewaitara.medec.R
import kotlinx.android.synthetic.main.fragment_patients.*
import kotlinx.android.synthetic.main.fragment_patients.view.*

class PatientsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_patients, container, false)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.patient_nav_host_fragment) as NavHostFragment

        view.bottomNavigationView.setupWithNavController(navHostFragment.navController)
        // Inflate the layout for this fragment
        return view
    }

}