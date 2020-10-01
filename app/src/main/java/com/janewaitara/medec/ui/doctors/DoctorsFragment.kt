package com.janewaitara.medec.ui.doctors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.janewaitara.medec.R
import kotlinx.android.synthetic.main.fragment_patients.view.*


class DoctorsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctors, container, false)


        val navHostFragment = childFragmentManager.findFragmentById(R.id.doctor_nav_host_fragment) as NavHostFragment

        view.bottomNavigationView.setupWithNavController(navHostFragment.navController)
        // Inflate the layout for this fragment
        return view
    }


}