package com.sherif.nearbyapp.network


import com.sherif.nearbyapp.model.locations.ChooseLocation
import com.sherif.nearbyapp.model.locations.Venue
import com.sherif.nearbyapp.model.photos.PhotoItem
import io.reactivex.Observable
import io.reactivex.functions.Function

class LocationRepo(private val locationApi:LocationApi) {


    fun getLocations(
        client_id: String,
        client_secret: String,
        lnglat: String
    ): Observable<List<Venue>>? {
        return locationApi.getLocations(client_id, client_secret, lnglat).map(object :
            Function<ChooseLocation?, List<Venue>> {
            override fun apply(it: ChooseLocation): List<Venue> {
                val listOfVenues = ArrayList<Venue>()
                it.response.groups.get(0).items.forEach {
                    listOfVenues.add(it.venue)
                }
                return listOfVenues
            }
        })

    }

    fun getPhotos(
        id: String,
        client_id: String,
        client_secret: String
    ): Observable<List<PhotoItem>>? {
        return locationApi.getPhotos(id,client_id, client_secret).map { it ->
            val listOfPhotos = ArrayList<PhotoItem>()
            it.response.photos.items.forEach {
                listOfPhotos.add(it)
            }
            listOfPhotos
        }


    }
    }