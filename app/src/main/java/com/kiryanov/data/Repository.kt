package com.kiryanov.data

import com.kiryanov.model.NewsResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Repository(private val api: Api) {

    fun getNews(page: Int, pageSize: Int): Single<NewsResponse> = api
        .getNews(page, pageSize)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}