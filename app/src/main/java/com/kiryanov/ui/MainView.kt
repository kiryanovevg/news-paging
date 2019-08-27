package com.kiryanov.ui

import android.content.Intent
import androidx.paging.PagedList
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.kiryanov.model.News
import com.kiryanov.network.network_state.NetworkState

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {

    fun setNewsList(pagedList: PagedList<News>)

    fun setRefreshing(loading: Boolean)
    fun setRefreshingErrorVisibility(visibility: Boolean, message: String? = null)

    fun setLoadingState(networkState: NetworkState?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun show(intent: Intent)
}