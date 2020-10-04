package com.janewaitara.medec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**loading the nav graph*/
        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        mFirebaseAuth = FirebaseAuth.getInstance()


        /**
         * If the user is logged in, navigate to main activity
         * */
        /*  if(mFirebaseAuth.currentUser != null){
           navController.navigate(R.id.patientsFragment)
        }*/

        if(mFirebaseAuth.currentUser != null){
           navController.navigate(R.id.doctorsFragment)
        }
    }
}