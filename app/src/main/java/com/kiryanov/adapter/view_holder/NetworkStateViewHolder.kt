package com.kiryanov.adapter.view_holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiryanov.databinding.ItemNetworkStateBinding
import com.kiryanov.network.network_state.NetworkState

class NetworkStateViewHolder private constructor(
    private val binding: ItemNetworkStateBinding,
    private val retryCallback: (() -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup, retryCallback: (() -> Unit)? = null) = NetworkStateViewHolder(
            ItemNetworkStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            retryCallback
        )
    }

    fun bind(networkState: NetworkState) = when(networkState.status) {
        NetworkState.Status.SUCCESS -> {}
        NetworkState.Status.LOADING -> loading()
        NetworkState.Status.FAILED -> failed(networkState.message)
        else -> throw IllegalArgumentException("Unknown state")
    }

    private fun loading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRetry.visibility = View.GONE
        binding.tvMessage.visibility = View.GONE
    }

    private fun failed(msg: String?) {
        binding.progressBar.visibility = View.GONE
        binding.btnRetry.visibility = View.VISIBLE
        binding.btnRetry.setOnClickListener { retryCallback?.invoke() }
        binding.tvMessage.visibility =  if (msg != null) {
            binding.tvMessage.text = msg
            View.VISIBLE
        } else View.GONE
    }
}