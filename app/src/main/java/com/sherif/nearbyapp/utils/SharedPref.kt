package com.sherif.nearbyapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.sherif.nearbyapp.MyApplication.Companion.appContext
import com.sherif.nearbyapp.model.enum.ModeType


object SharedPref {
    private val sharedPrefFile = "kotlinsharedpreference"
     var  sharedPreferences: SharedPreferences = appContext.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

    fun setMode(value :String ){
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString(MODEKEY,value)
        editor.apply()
        editor.commit()
    }
    fun getMode( ):String?{
        return  sharedPreferences.getString(MODEKEY,ModeType.REALTIME.value)

    }
    fun setLatLong(value: LatLng){
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        val gson = Gson()
        val latLongString = gson.toJson(value)
        editor.putString(CURRENTLATLONG,latLongString)
        editor.apply()
        editor.commit()
    }

    fun getLatLong(): LatLng {

        val latLongString = sharedPreferences.getString(CURRENTLATLONG,null)
        var latLong = Gson().fromJson(latLongString,LatLng::class.java)
        if(latLong == null){
            latLong = LatLng(0.0,0.0)
        }

        return latLong
    }
}