package com.sherif.nearbyapp.model.locations

data class Venue(
    val categories: List<Category>? = null,
    val id: String? = null,
    val location: Location? = null,
    val name: String? = null,
    var imageUrl: String? = null
)