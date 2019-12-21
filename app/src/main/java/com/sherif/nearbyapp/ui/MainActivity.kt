package com.sherif.nearbyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.sherif.nearbyapp.ViewModel.MainViewModel
import com.sherif.nearbyapp.utils.*
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by inject()
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var sharedPref = SharedPref()
    lateinit var  locationUtils: LocationUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        locationUtils = LocationUtils()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getviewModel()
//        getLastLocation()
//
//        chooseMode()
//
//        initMode()
//        val mainHandler = Handler(Looper.getMainLooper())
//        mainHandler.post(object : Runnable {
//            override fun run() {
//                requestNewLocationData()
//                mainHandler.postDelayed(this, 10000)
//            }
//        })


    }
    private fun getviewModel(){
        mainViewModel.exception.observe(this, Observer { ExpMessage ->
            Toast.makeText(this , ExpMessage , Toast.LENGTH_SHORT).show()
//            mainProgressBar.visibility = View.GONE

        })

        mainViewModel.locationlist.observe(this, Observer {locationList ->  Toast.makeText(this , locationList.toString() , Toast.LENGTH_SHORT).show()
//            mainProgressBar.visibility = View.GONE
        })
//        mainProgressBar.visibility = View.VISIBLE
        mainViewModel.LoadLocation()
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (locationUtils.checkPermissions()) {
            if (locationUtils.isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        sharedPref.setLatLong(LATITUDE,location.latitude.toFloat())
                        sharedPref.setLatLong(LONGITUDE,location.longitude.toFloat())
                        val latitude1 = sharedPref.getLatLong(LATITUDE)
                        val longitude1 = sharedPref.getLatLong(LONGITUDE)
                        findViewById<TextView>(R.id.latTextView).text = latitude1.toString()
                        findViewById<TextView>(R.id.lonTextView).text = longitude1.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            locationUtils.requestPermissions(this)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            val latitude1 = sharedPref.getLatLong(LATITUDE)
            val longitude1 = sharedPref.getLatLong(LONGITUDE)
            val Distance =  locationUtils.measure(latitude1.toDouble(),longitude1.toDouble(), mLastLocation.latitude ,mLastLocation.longitude)
            val toast = Toast.makeText(applicationContext, Distance.toString(), Toast.LENGTH_SHORT)
            findViewById<TextView>(R.id.latTextView).text = latitude1.toString()
            findViewById<TextView>(R.id.lonTextView).text =  Distance.toString()
            toast.show()
        }
    }


  fun chooseMode(){
      textchossenmode.setOnClickListener{
          if(textchossenmode.text == "Realtime" ){
              sharedPref.setMode("Single Update")
              textchossenmode.text ="Single Update"
          }else {sharedPref.setMode("Realtime" )
              textchossenmode.text ="Realtime"
          } }
  }

    fun initMode(){
        textchossenmode.text = sharedPref.getMode()
    }

}


