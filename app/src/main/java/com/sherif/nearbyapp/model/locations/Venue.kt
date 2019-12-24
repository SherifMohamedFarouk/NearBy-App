package com.sherif.nearbyapp.model.locations

data class Venue(
    val categories: List<Category>,
    val id: String,
    val location: Location,
    val name: String,
    var imageUrl: String
)