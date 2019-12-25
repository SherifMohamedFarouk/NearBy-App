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
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

import com.sherif.nearbyapp.MyApplication.Companion.appContext
import com.sherif.nearbyapp.model.enum.PhotosSize
import com.sherif.nearbyapp.model.locations.ChooseLocation
import com.sherif.nearbyapp.model.locations.Venue
import com.sherif.nearbyapp.model.photos.PhotoItem
import com.sherif.nearbyapp.model.photos.Photos
import com.sherif.nearbyapp.network.LocationRepo
import com.sherif.nearbyapp.utils.*
import com.sherif.nearbyapp.utils.SharedPref.getLatLong
import com.sherif.nearbyapp.utils.SharedPref.setLatLong
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val locationRepo: LocationRepo):ViewModel()  {
    val locationlist = MutableLiveData<List<Venue>>()
    val Photoslist = MutableLiveData<Venue>()
    val exception = MutableLiveData<String>()
    val loading = MutableLiveData<Int>()
    var disposable: Disposable? = null
    private val  locationUtils: LocationUtils = LocationUtils()
     var mFusedLocationClient: FusedLocationProviderClient =
         LocationServices.getFusedLocationProviderClient(appContext)

    @SuppressLint("MissingPermission")
     fun getLastLocation(context: Context) {
        if (locationUtils.checkPermissions()) {
            if (locationUtils.isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(context as Activity) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val latestlatlng = LatLng(location.latitude, location.longitude)
                          setLatLong(latestlatlng)
                        LoadLocation(latestlatlng)
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
        } else {
            locationUtils.requestPermissions(context as Activity)
        }
    }

    fun LoadLocation(latLng: LatLng) {
        val latlongformat = String.format(
            "%s,%s", latLng.latitude
                .toString(), latLng.longitude
        )

        disposable =
            locationRepo.getLocations(CLIENT_ID2, CLIENT_SECRET2, latlongformat)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { loading.value = View.VISIBLE }
                ?.doOnComplete { loading.value = View.GONE }
                ?.subscribe(
                    { result ->

                        locationlist.value = result
                        LoadPhotos(result)
                    },
                    { error ->
                        exception.value = error.toString()
                    }
                )

    }

    fun LoadPhotos(listOfLocations: List<Venue>){
        listOfLocations.forEach {
            val venueObs = Observable.just(it)
            val venue =
                Observable.zip(venueObs, venueObs.flatMap { t ->
                    locationRepo.getPhotos(
                        t.id,
                        CLIENT_ID2,
                        CLIENT_SECRET2
                    )
                },
                    BiFunction<Venue, List<PhotoItem>, Venue> { t1, t2 ->
                        if (t2.size > 0)
                            t1.imageUrl = t2.get(0).prefix + PhotosSize.SMALL.value + t2.get(0).suffix
                        t1
                    }
                )
            venue
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loading.value = View.VISIBLE }
                .doFinally {
                    loading.value = View.GONE

                }
                .subscribe(
                    { result ->
                        Photoslist.value = result
                    },
                    { error ->
                        exception.value = error.toString()
                    }
                )
        }
    }


    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 2000

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            val latLong = getLatLong()
            val distance = locationUtils.measure(
                latLong.latitude,
                latLong.longitude,
                mLastLocation.latitude,
                mLastLocation.longitude
            )
            if (distance >= 500.0) {
                setLatLong(LatLng(mLastLocation.latitude, mLastLocation.longitude))
                LoadLocation(LatLng(mLastLocation.latitude, mLastLocation.longitude))
            }
        }
    }


    fun Updating(){
        requestNewLocationData()
    }
    fun StopUpdating(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    override fun onCleared() {
        super.onCleared()
        disposable!!.dispose()
    }


}





