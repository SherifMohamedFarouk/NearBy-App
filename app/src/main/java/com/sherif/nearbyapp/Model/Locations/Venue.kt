package com.sherif.nearbyapp.Model.Locations

import com.sherif.nearbyapp.Model.Locations.Category
import com.sherif.nearbyapp.Model.Locations.Location

data class Venue(
    val categories: List<Category>,
    val id: String,
    val location: Location,
    val name: String
)