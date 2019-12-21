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
import com.google.android.gms.location.*
import com.sherif.nearbyapp.utils.LOCATION_REQUEST_CODE_PERMISSION
import com.sherif.nearbyapp.utils.LocationUtils
import com.sherif.nearbyapp.utils.SharedPref


class MainActivity : AppCompatActivity() {

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var sharedPref = SharedPref()
    lateinit var  locationUtils: LocationUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        locationUtils = LocationUtils()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        chooseMode()

        initMode()
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
             val Distance =  measure(37.4406,-122.1430,37.4429 ,-122.1444)

                val toast = Toast.makeText(applicationContext, Distance.toString(), Toast.LENGTH_SHORT)
                toast.show()
                mainHandler.postDelayed(this, 10000)
            }
        })
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

                        findViewById<TextView>(R.id.latTextView).text = location.latitude.toString()
                        findViewById<TextView>(R.id.lonTextView).text = location.longitude.toString()
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
            findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
            findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
            val toast = Toast.makeText(applicationContext, mLastLocation.latitude.toString(), Toast.LENGTH_SHORT)
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
    fun measure(latitude1:Double, longitude1:Double, latitude2:Double, longitude2:Double):Double{  // generally used geo measurement function
        var radius = 6378.137; // Radius of earth in KM
        var dlatitude = latitude2 * Math.PI / 180 - latitude1 * Math.PI / 180;
        var dlongitude = longitude2 * Math.PI / 180 - longitude1 * Math.PI / 180;
        var area = Math.sin(dlatitude/2) * Math.sin(dlatitude/2) +
                Math.cos(latitude1 * Math.PI / 180) * Math.cos(latitude2 * Math.PI / 180) *
                Math.sin(dlongitude/2) * Math.sin(dlongitude/2);
        var circumference = 2 * Math.atan2(Math.sqrt(area), Math.sqrt(1-area));
        var ditance = radius * circumference;
        return ditance * 1000; // meters
    }
}


