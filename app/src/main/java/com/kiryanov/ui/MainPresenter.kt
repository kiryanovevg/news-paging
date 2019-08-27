package com.kiryanov.ui

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.paging.PagedList
import com.arellomobile.mvp.InjectViewState
import com.kiryanov.App
import com.kiryanov.data.Repository
import com.kiryanov.data.data_source.NewsDataSource
import com.kiryanov.model.News
import com.kiryanov.mvp.BasePresenter
import com.kiryanov.network.network_state.NetworkState
import javax.inject.Inject

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    @Inject
    lateinit var repository: Repository

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var dataSource: NewsDataSource
    private lateinit var pagedList: PagedList<News>

    init {
        App.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadNews()
    }

    fun loadRetry() {
        dataSource.loadRetry()
    }

    fun loadNews() {
        dataSource = NewsDataSource(repository)
        dataSource.disposables.unsubscribeOnDestroy()

        handleInitialState()
        handleLoadingState()

        pagedList = PagedList.Builder<Int, News>(
            dataSource,
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build()
        )
            .setFetchExecutor { handler.post(it) }
            .setNotifyExecutor { handler.post(it) }
            .build()

        viewState.setNewsList(pagedList)
    }

    private fun handleInitialState() {
        dataSource.initialState = { when(it.status) {
            NetworkState.Status.LOADING -> {
                viewState.setLoadingState(null)
                viewState.setRefreshing(true)
                viewState.setRefreshingErrorVisibility(false)
            }
            NetworkState.Status.SUCCESS -> {
                viewState.setRefreshing(false)
            }
            NetworkState.Status.FAILED -> {
                viewState.setRefreshing(false)
                viewState.setRefreshingErrorVisibility(true, it.message)
            }
        } }
    }

    private fun handleLoadingState() {
        dataSource.loadingState = { viewState.setLoadingState(it) }
    }

    fun openInBrowser(item: News) {
        Intent(Intent.ACTION_VIEW)
            .apply { data = Uri.parse(item.url) }
            .run { viewState.show(this) }
    }
}