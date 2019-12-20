package com.sherif.nearbyapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.sherif.nearbyapp.MyApplication.Companion.appContext
import kotlinx.android.synthetic.main.activity_main.*

class SharedPref {
    private val sharedPrefFile = "kotlinsharedpreference"
     var  sharedPreferences: SharedPreferences = appContext.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

    fun setMode(value :String ){
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString(MODEKEY,value)
        editor.apply()
        editor.commit()
    }
    fun getMode( ):String?{
        return  sharedPreferences.getString(MODEKEY,DEFAULTMODE)

    }
}