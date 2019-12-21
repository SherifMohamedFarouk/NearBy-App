package com.sherif.nearbyapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sherif.nearbyapp.R
import com.sherif.nearbyapp.utils.LOCATION_REQUEST_CODE_PERMISSION
import com.sherif.nearbyapp.utils.LocationUtils
import java.util.*

class SplashActivity : AppCompatActivity() {
    lateinit var  locationUtils: LocationUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        locationUtils = LocationUtils()
        locationUtils.requestPermissions(this)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_REQUEST_CODE_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (locationUtils.checkPermissions()) {

                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }, 2500)
                }

            }
            else{
            permissionDialog()
            }
        }
    }



    fun permissionDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Location Permission")
        builder.setMessage("Please accept the location permission the application wont work without it")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES"){dialog, which ->
            locationUtils.requestPermissions(this)
        }
        // Display a negative button on alert dialog
        builder.setNegativeButton("No"){dialog,which ->
            finish()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}