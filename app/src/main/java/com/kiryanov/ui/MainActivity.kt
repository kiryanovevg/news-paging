package com.kiryanov.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.gson.JsonObject
import com.kiryanov.App
import com.kiryanov.R
import com.kiryanov.model.News
import com.kiryanov.adapter.RecyclerViewPagedAdapter
import com.kiryanov.network.network_state.NetworkState
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private lateinit var adapter: RecyclerViewPagedAdapter<News, MainPresenter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = RecyclerViewPagedAdapter(R.layout.item_news, presenter, News.DIFF_UTIL)

        refresh_layout.setOnRefreshListener { presenter.loadNews() }
        btn_retry.setOnClickListener { presenter.loadRetry() }

        adapter.retryCallback = { presenter.loadRetry() }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun setNewsList(pagedList: PagedList<News>) {
        adapter.submitList(pagedList)
    }

    override fun setRefreshing(loading: Boolean) {
        refresh_layout.isRefreshing = loading
    }

    override fun setRefreshingErrorVisibility(visibility: Boolean, message: String?) {
        error_layout.visibility = if (visibility) View.VISIBLE else View.GONE
        refresh_layout.isEnabled = !visibility
        tv_message.text = message
    }

    override fun setLoadingState(networkState: NetworkState?) {
        adapter.networkState = networkState
    }

    override fun show(intent: Intent) {
        startActivity(intent)
    }
}
