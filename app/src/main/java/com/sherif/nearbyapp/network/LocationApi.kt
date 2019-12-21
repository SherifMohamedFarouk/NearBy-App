package com.sherif.nearbyapp.network

import com.sherif.nearbyapp.model.Locations.ChooseLocation
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {

    @GET("explore?&v=20180323&limit=20")
    fun getLocations(@Query("client_id")client_id : String,@Query("client_secret")client_secret : String,
                     @Query("ll")lnglat : String): Observable<ChooseLocation>
}