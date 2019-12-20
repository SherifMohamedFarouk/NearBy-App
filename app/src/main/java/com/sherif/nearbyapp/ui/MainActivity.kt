package com.sherif.nearbyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sherif.nearbyapp.R
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.Settings
import com.google.android.gms.location.*
import com.sherif.nearbyapp.utils.LocationUtils
import com.sherif.nearbyapp.utils.SharedPref


class MainActivity : AppCompatActivity() {

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var  sharedPreferences: SharedPreferences
     var sharedPref = SharedPref()
    lateinit var  locationUtils: LocationUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        locationUtils = LocationUtils(this,  PERMISSION_ID)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        chooseMode()
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
            locationUtils.requestPermissions()
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
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }


  fun chooseMode(){
      sharedPreferences = sharedPref.SharedPref(this)
      val editor:SharedPreferences.Editor =  sharedPreferences.edit()
      textchossenmode.setOnClickListener{
          if(textchossenmode.text == "Realtime" ){
              editor.putString("mode_key","Single Update")
              editor.apply()
              editor.commit()
          }else{
              editor.putString("mode_key","Realtime")
              editor.apply()
              editor.commit()
          }
          val sharedNameValue = sharedPreferences.getString("mode_key","Realtime")
          textchossenmode.text = sharedNameValue

      }
      val sharedNameValue = sharedPreferences.getString("mode_key","Realtime")
      textchossenmode.text = sharedNameValue

  }

}


