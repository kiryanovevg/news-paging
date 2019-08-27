package com.kiryanov.data.data_source

import androidx.paging.PageKeyedDataSource
import com.kiryanov.data.Repository
import com.kiryanov.getHttpError
import com.kiryanov.model.News
import com.kiryanov.network.network_state.NetworkState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class NewsDataSource(
    private val repository: Repository
) : PageKeyedDataSource<Int, News>() {

    var initialState: ((NetworkState) -> Unit)? = null
    var loadingState: ((NetworkState) -> Unit)? = null
    val disposables = CompositeDisposable()

    private var retryCompletable: Completable? = null

    fun loadRetry() {
        retryCompletable?.let {
            it.subscribeOn(Schedulers.io())
            it.observeOn(AndroidSchedulers.mainThread())
            it.subscribe()
        }?.also { disposables.add(it) }
    }

    private fun setRetryAction(action: Action?) {
        if (action == null) retryCompletable = null
        else retryCompletable = Completable.fromAction(action)
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, News>) {
        val perPage = params.requestedLoadSize
        val startPage = 1

        initialState?.invoke(NetworkState.LOADING)
        disposables.add(repository.getNews(startPage, perPage).subscribe(
            { response ->
                setRetryAction(null)
                callback.onResult(response.articles, null, startPage + 1)
                loadingState?.invoke(NetworkState.SUCCESS)
                initialState?.invoke(NetworkState.SUCCESS)
            },
            { throwable ->
                setRetryAction(Action { loadInitial(params, callback) })
                initialState?.invoke(NetworkState.ERROR(getHttpError(throwable)))
            }
        ))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        val perPage = params.requestedLoadSize
        val startPage = params.key

        loadingState?.invoke(NetworkState.LOADING)
        disposables.add(repository.getNews(startPage, perPage).subscribe(
            { response ->
                setRetryAction(null)
                callback.onResult(response.articles, if (startPage < response.totalResults) startPage + 1 else null)
                loadingState?.invoke(NetworkState.SUCCESS)
            },
            { throwable ->
                setRetryAction(Action { loadAfter(params, callback) })
                loadingState?.invoke(NetworkState.ERROR(getHttpError(throwable)))
            }
        ))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        //not needed
    }
}