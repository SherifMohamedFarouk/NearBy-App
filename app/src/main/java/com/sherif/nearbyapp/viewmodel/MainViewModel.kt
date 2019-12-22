package com.sherif.nearbyapp.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.sherif.nearbyapp.MyApplication
import com.sherif.nearbyapp.MyApplication.Companion.appContext
import com.sherif.nearbyapp.model.Locations.ChooseLocation
import com.sherif.nearbyapp.network.LocationRepo
import com.sherif.nearbyapp.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class MainViewModel(private val locationRepo: LocationRepo):ViewModel()  {
    val locationlist = MutableLiveData<ChooseLocation>()
    val exception = MutableLiveData<String>()
    var disposable: Disposable? = null
    private val  locationUtils: LocationUtils = LocationUtils()
    var sharedPref = SharedPref()
     var mFusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)

    fun LoadLocation(context: Context) {
        getLastLocation(context)

        val latlongformat = String.format("%s,%s",sharedPref.getLatLong(LATITUDE)
            .toString(),sharedPref.getLatLong(LONGITUDE).toString())

        if(sharedPref.getMode()=="Realtime") {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post(object : Runnable {
                override fun run() {
                    sharedPref.getMode()
                    val latlongformat = String.format("%s,%s",sharedPref.getLatLong(LATITUDE)
                        .toString(),sharedPref.getLatLong(LONGITUDE).toString())

                    disposable = locationRepo.getLocations(CLIENT_ID,CLIENT_SECERET,latlongformat)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { result -> Log.v("Near me", "" + result)
                                locationlist.value = result
                            },
                            { error -> Log.e("ERROR", error.message)
                                exception.value = error.toString()
                            }
                        )

                    requestNewLocationData()
                    mainHandler.postDelayed(this, 10000)
                }
            })
        }
        else{
            disposable = locationRepo.getLocations(CLIENT_ID,CLIENT_SECERET,latlongformat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> Log.v("Near me", "" + result)
                        locationlist.value = result
                    },
                    { error -> Log.e("ERROR", error.message)
                        exception.value = error.toString()
                    }
                )

        }


    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(context: Context) {
        if (locationUtils.checkPermissions()) {
            if (locationUtils.isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(context as Activity) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        sharedPref.setLatLong(LATITUDE,location.latitude.toFloat())
                        sharedPref.setLatLong(LONGITUDE,location.longitude.toFloat())
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                appContext.startActivity(intent)
            }
        } else {
            locationUtils.requestPermissions(context as Activity)
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
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
            sharedPref.setLatLong(DISTANCE,Distance.toFloat())
            val sharedDistance = sharedPref.getLatLong(DISTANCE)
            val toast = Toast.makeText(appContext, sharedDistance.toString(), Toast.LENGTH_SHORT)
            toast.show()
            if (sharedDistance >= 500.0 ){
                sharedPref.setLatLong(LATITUDE,mLastLocation.latitude.toFloat())
                sharedPref.setLatLong(LONGITUDE,mLastLocation.longitude.toFloat())
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        disposable!!.dispose()
    }


}





