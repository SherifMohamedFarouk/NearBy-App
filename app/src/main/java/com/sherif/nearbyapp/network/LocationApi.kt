package com.sherif.nearbyapp.network

import com.sherif.nearbyapp.Model.Locations.ChooseLocation
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {

    @GET("explore?client_id=SY24OORBST2CQDJLICCL3SE5AM2NC2NIMYMNVGFINS02HPXG&client_secret=53BT1R5WEJGPLG1WGCWHFHWFTMX0JAR5PLBT0MO3SO01Q3GR&v=20180323&limit=20")
    fun getLocations(@Query("ll")lnglat : String): Observable<ChooseLocation>
}