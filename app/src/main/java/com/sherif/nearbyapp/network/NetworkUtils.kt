package com.sherif.nearbyapp.network


import com.sherif.nearbyapp.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtils {

    // OkHttpClient allows us to Log using OkHttp and see what happens in our requests and how we send and retreive in the network layer
    fun createHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return client.addInterceptor(interceptor).build()
    }

    // this method creates CatApi Object which allows us to connect with apis and get our data from server as JSON
    // GsonConverterFactory is what will parse this JSON into our Cat Object
    fun createWebService(): LocationApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient())
            .build()
        return retrofit.create(LocationApi::class.java)
    }


}