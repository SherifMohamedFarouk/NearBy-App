package com.sherif.nearbyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sherif.nearbyapp.R
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinkoinmvp.adapters.LocationAdapter
import com.google.android.gms.location.*
import com.sherif.nearbyapp.viewmodel.MainViewModel
import com.sherif.nearbyapp.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by inject()
    var sharedPref = SharedPref()
    private lateinit var locationAdapter :LocationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        startRecycleview()

        getviewModel()

        chooseMode()

        initMode()

        realtimeChangeMeters()


    }
    private fun startRecycleview(){
        // initializing catAdapter with empty list
        locationAdapter = LocationAdapter(ArrayList())
        // apply allows you to alter variables inside the object and assign them
        LocationRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        LocationRecyclerView.adapter = locationAdapter
    }
    private fun getviewModel(){
        mainViewModel.exception.observe(this, Observer { ExpMessage ->
            Toast.makeText(this , ExpMessage , Toast.LENGTH_SHORT).show()
//            mainProgressBar.visibility = View.GONE

        })

        mainViewModel.locationlist.observe(this, Observer {locationList ->  locationAdapter.updatelist(locationList.response.groups[0].items )
//            mainProgressBar.visibility = View.GONE
            mainViewModel.LoadPhotos(locationList.response.groups[0].items[1].venue.id)
        })

//        mainProgressBar.visibility = View.VISIBLE
        mainViewModel.LoadLocation(this)
    }

  fun chooseMode(){
      textchossenmode.setOnClickListener{
          if(textchossenmode.text == "Realtime" ){
              sharedPref.setMode("Single Update")
              textchossenmode.text ="Single Update"
          }else {sharedPref.setMode("Realtime" )
              textchossenmode.text ="Realtime"
          } }
  }

    fun initMode(){
        textchossenmode.text = sharedPref.getMode()

    }

    fun realtimeChangeMeters(){
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post(object : Runnable {
                override fun run() {
                    sharedPref.getMode()
                    if(sharedPref.getMode()=="Realtime"&&sharedPref.getLatLong(DISTANCE) >= 500.0) {
                        startRecycleview()
                    }
                        mainHandler.postDelayed(this, 10000)

                }
            })
        }

    }




