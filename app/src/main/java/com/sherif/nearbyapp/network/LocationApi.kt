package com.sherif.nearbyapp.network

import com.sherif.nearbyapp.model.locations.ChooseLocation
import com.sherif.nearbyapp.model.photos.Photos
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationApi {

    @GET("explore?&v=20180323&limit=10&radius=1000")
    fun getLocations(@Query("client_id")client_id : String,@Query("client_secret")client_secret : String,
                     @Query("ll")lnglat : String): Observable<ChooseLocation>

    @GET("{id}/photos?&v=20180323")
    fun getPhotos(@Path(value = "id") page: String,@Query("client_id")client_id : String,@Query("client_secret")client_secret : String
    ): Observable<Photos>
}