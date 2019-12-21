package com.sherif.nearbyapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.sherif.nearbyapp.MyApplication.Companion.appContext

class LocationUtils {


     fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

     fun requestPermissions(context:Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE_PERMISSION
        )
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