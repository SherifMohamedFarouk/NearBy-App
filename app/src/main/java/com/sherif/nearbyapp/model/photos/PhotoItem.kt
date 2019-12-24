package com.sherif.nearbyapp.model.photos

data class PhotoItem(
    val createdAt: Int,
    val height: Int,
    val id: String,
    val prefix: String,
    val suffix: String,
    val width: Int
)