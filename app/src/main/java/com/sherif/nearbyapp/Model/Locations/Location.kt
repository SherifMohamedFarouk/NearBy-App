package com.sherif.nearbyapp.Model.Locations

data class Location(
    val address: String,
    val cc: String,
    val city: String,
    val country: String,
    val formattedAddress: List<String>
)