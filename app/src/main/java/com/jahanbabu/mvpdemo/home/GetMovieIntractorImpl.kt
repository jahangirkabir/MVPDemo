package com.jahanbabu.mvpdemo.home

import android.util.Log
import com.jahanbabu.mvpdemo.Data.Movie
import com.jahanbabu.mvpdemo.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetMovieIntractorImpl: MainContract.GetMovieIntractor {

    override fun getMovieArrayList(onFinishedListener: MainContract.GetMovieIntractor.OnFinishedListener) {

        val service = ApiService.create()
        val call = service.getVideos()

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
//                Utility.hideProgressDialog(progressDialog);
                Log.e("RESPONSE", response.raw().toString())
                if (response.raw().code() == 200) {
                    onFinishedListener.onFinished(response.body() as ArrayList<Movie>)
                } else {

                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                onFinishedListener.onFailure(t)
            }
        })
    }
}