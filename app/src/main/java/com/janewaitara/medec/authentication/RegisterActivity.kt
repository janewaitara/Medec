package com.janewaitara.medec.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.janewaitara.medec.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        TODO("Not yet implemented")
    }
}