package com.example.moviemate.shared.retrofit.interceptor

import android.util.Log
import com.example.moviemate.shared.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentUrl = chain.request().url
        val newUrl = currentUrl.newBuilder().addQueryParameter("api_key", Constants.API_KEY).build()
        val currentRequest = chain.request().newBuilder()
        val newRequest = currentRequest.url(newUrl).build()
        Log.d("LOAD", newRequest.toString())
        return chain.proceed(newRequest)
    }
}