package com.janewaitara.medec.ui.location

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.janewaitara.medec.R
import com.janewaitara.medec.model.County
import kotlinx.android.synthetic.main.fragment_location.*
import java.io.IOException
import java.io.InputStream


class LocationFragment : Fragment(), CountyAdapter.CountyClickListener {

    private val countyAdapter = CountyAdapter(this)

    lateinit var counties: List<County>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    }

}