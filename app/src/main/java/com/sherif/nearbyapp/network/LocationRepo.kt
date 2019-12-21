package com.sherif.nearbyapp.network


import com.sherif.nearbyapp.Model.Locations.ChooseLocation
import io.reactivex.Observable
import java.util.*

class LocationRepo(private val locationApi:LocationApi) {


    fun getLocations(lnglat: String): Observable<ChooseLocation> {
        return locationApi.getLocations(lnglat)
    }

}