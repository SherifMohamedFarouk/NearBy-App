package com.sherif.nearbyapp.model.locations

data class Location(
    val address: String,
    val cc: String,
    val city: String,
    val country: String,
    val formattedAddress: List<String>
)