package com.sherif.nearbyapp.di

import com.sherif.nearbyapp.network.LocationRepo
import com.sherif.nearbyapp.viewmodel.MainViewModel
import com.sherif.nearbyapp.network.NetworkUtils.createWebService
import com.sherif.nearbyapp.utils.LocationUtils
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    single { createWebService() }
    single { LocationRepo(locationApi = get()) }
    viewModel { MainViewModel( get()) }
    single { LocationUtils() }

}
