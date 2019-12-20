package com.sherif.nearbyapp.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref {
    private val sharedPrefFile = "kotlinsharedpreference"

    fun SharedPref(context: Context) : SharedPreferences{
        return context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
    }
}