package com.sam.fivehundredmeters.locationusecasetesting

import android.content.Context

import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.sherif.nearbyapp.MyApplication.Companion.appContext
import com.sherif.nearbyapp.network.LocationRepo
import com.sherif.nearbyapp.network.NetworkUtils
import com.sherif.nearbyapp.utils.CLIENT_ID
import com.sherif.nearbyapp.utils.CLIENT_SECRET
import com.sherif.nearbyapp.utils.LocationUtils
import com.sherif.nearbyapp.viewmodel.MainViewModel
import org.junit.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mock

@RunWith(MockitoJUnitRunner::class)
class IntegratingViewModelWithRepoLayerLocationTesting : KoinTest {

    lateinit var mainViewModel: MainViewModel
    lateinit var mv: MainViewModel
    val locationRepo: LocationRepo by inject()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun onSetup() {
        appContext = mock(Context::class.java)
        startKoin {
            androidContext(appContext)
            loadKoinModules(module {
                single { NetworkUtils.createWebService() }
                single { LocationRepo(locationApi = get()) }
                viewModel { MainViewModel(get()) }
                single { LocationUtils() }
            })
        }
        mainViewModel = MainViewModel(locationRepo)
        mv = mock(MainViewModel::class.java)

    }

    @Test
    fun testCallBetweenViewModelAndRepository() {
        val lat = 35.0
        val long = 34.0
        val latlng = "$lat,$long"
        val list = locationRepo.getLocations(
            CLIENT_ID,
            CLIENT_SECRET, latlng)
            ?.blockingFirst()
        `when`(mv.LoadLocation(LatLng(lat, long))).then {
            mainViewModel.locationlist.value = list
            null
        }

        mv.LoadLocation(LatLng(lat, long))
        // Has received data
        Assert.assertNotNull(mainViewModel.locationlist.value)
        Assert.assertTrue(mainViewModel.locationlist.value!!.isNotEmpty())

    }

    @After
    fun stop() {
        stopKoin()
    }
}