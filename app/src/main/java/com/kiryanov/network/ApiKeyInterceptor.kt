package com.kiryanov.network

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.request().url.newBuilder()
        .addQueryParameter("apiKey", apiKey).build()
        .let { chain.request().newBuilder() }
        .run { chain.proceed(build()) }

    /*override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url.newBuilder().addQueryParameter("apiKey", apiKey).build()
        chain.request().
    }*/
}