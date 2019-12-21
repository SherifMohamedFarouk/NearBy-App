package com.sherif.nearbyapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sherif.nearbyapp.model.Locations.ChooseLocation
import com.sherif.nearbyapp.network.LocationRepo
import com.sherif.nearbyapp.utils.CLIENT_ID
import com.sherif.nearbyapp.utils.CLIENT_SECERET
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val locationRepo: LocationRepo):ViewModel()  {
    val locationlist = MutableLiveData<ChooseLocation>()
    val exception = MutableLiveData<String>()
    var disposable: Disposable? = null

    fun LoadLocation() {

            disposable = locationRepo.getLocations(CLIENT_ID,CLIENT_SECERET,"40.7243,-74.0018")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> Log.v("Near me", "" + result)
                        locationlist.value = result
                    },
                    { error -> Log.e("ERROR", error.message)
                        exception.value = error.toString()
                    }
                )


    }

    override fun onCleared() {
        super.onCleared()
        disposable!!.dispose()
    }


}



