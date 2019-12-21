package com.sherif.nearbyapp.network


import com.sherif.nearbyapp.model.Locations.ChooseLocation
import io.reactivex.Observable

class LocationRepo(private val locationApi:LocationApi) {


    fun getLocations(client_id : String,client_secret: String,lnglat: String): Observable<ChooseLocation> {
        return locationApi.getLocations(client_id,client_secret,lnglat)
    }

}