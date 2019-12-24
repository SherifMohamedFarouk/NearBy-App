package com.sherif.nearbyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sherif.nearbyapp.R
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinkoinmvp.adapters.LocationAdapter
import com.google.android.gms.location.*
import com.sherif.nearbyapp.model.enum.ModeType
import com.sherif.nearbyapp.viewmodel.MainViewModel
import com.sherif.nearbyapp.utils.*
import com.sherif.nearbyapp.utils.SharedPref.getMode
import com.sherif.nearbyapp.utils.SharedPref.setMode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by inject()
    private lateinit var locationAdapter: LocationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        startRecycleview()
        getviewModel()
        chooseMode()
        initMode()


    }

    private fun startRecycleview() {
        // initializing catAdapter with empty list
        locationAdapter = LocationAdapter(ArrayList())
        // apply allows you to alter variables inside the object and assign them
        LocationRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        LocationRecyclerView.adapter = locationAdapter
    }

    private fun getviewModel() {
        mainViewModel.exception.observe(this, Observer { ExpMessage ->
            Toast.makeText(this, ExpMessage, Toast.LENGTH_SHORT).show()
//            mainProgressBar.visibility = View.GONE

        })

        mainViewModel.locationlist.observe(this, Observer { locationList ->
            locationAdapter.updatelist(locationList)
//            mainProgressBar.visibility = View.GONE

        })
        mainViewModel.Photoslist.observe(
            this,
            Observer { photoslist -> locationAdapter.updateID(photoslist) })

//        mainProgressBar.visibility = View.VISIBLE
        mainViewModel.getLastLocation(this)
    }

    fun chooseMode() {
        textchossenmode.setOnClickListener {
            when (textchossenmode.text) {
                ModeType.REALTIME.value -> {
                    updateMode(ModeType.SINGLE_UPDATE.value)
                    mainViewModel.StopUpdating()
                }
                else -> {
                    updateMode(ModeType.REALTIME.value)
                    mainViewModel.Updating()
                }
            }
        }
    }



    fun initMode() {
        textchossenmode.text = getMode()
        when (getMode()) {
            ModeType.REALTIME.value -> {
                mainViewModel.Updating()

            }
        }
    }

    private fun updateMode(modeType: String) {
        setMode(modeType)
        textchossenmode.text = modeType
    }


}



