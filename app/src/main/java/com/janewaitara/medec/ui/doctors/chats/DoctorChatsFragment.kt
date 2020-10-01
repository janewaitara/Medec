package com.janewaitara.medec.ui.doctors.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.janewaitara.medec.R

class DoctorChatsFragment : Fragment(), DoctorsChatAdapter.ChatPatientClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_chats, container, false)
    }

    override fun chatPatientClickListener(patientId: String) {
        TODO("Not yet implemented")
    }

}