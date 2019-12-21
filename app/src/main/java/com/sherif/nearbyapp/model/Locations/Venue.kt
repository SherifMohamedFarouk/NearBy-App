package com.sherif.nearbyapp.model.Locations

data class Venue(
    val categories: List<Category>,
    val id: String,
    val location: Location,
    val name: String
)