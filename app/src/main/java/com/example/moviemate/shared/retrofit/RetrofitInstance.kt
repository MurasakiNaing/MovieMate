package com.example.moviemate.shared.retrofit

import com.example.moviemate.shared.retrofit.interceptor.ApiKeyInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: TMdbApiService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()

        Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(
            GsonConverterFactory.create()
        )
            .client(client)
            .build()
            .create(TMdbApiService::class.java)
    }

}