package com.sherif.nearbyapp.network

import com.sherif.nearbyapp.viewmodel.MainViewModel
import com.sherif.nearbyapp.network.NetworkUtils.createWebService
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    single { createWebService() }
    single { LocationRepo(locationApi = get()) }
    viewModel { MainViewModel( get()) }

}
