package com.sherif.nearbyapp.Model.Locations

import com.sherif.nearbyapp.Model.Locations.Group

data class Response(
    val groups: List<Group>,
    val totalResults: Int
)