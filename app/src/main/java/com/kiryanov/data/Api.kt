package com.kiryanov.data

import com.kiryanov.model.NewsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("everything?q=sports")
    fun getNews(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Single<NewsResponse>
}