package com.jahanbabu.mvpdemo.network

import com.jahanbabu.mvpdemo.data.Movie
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("media.json?print=pretty")
    fun getVideos(): Call<List<Movie>>

    /**
     * Companion object for the factory
     */
    companion object Factory {
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(NetworkConfig.gson))
                    .baseUrl(NetworkConfig.BASE_URL)
                    .client(NetworkConfig.getClient())
                    .build()

            return retrofit.create(ApiService::class.java);
        }
    }
}