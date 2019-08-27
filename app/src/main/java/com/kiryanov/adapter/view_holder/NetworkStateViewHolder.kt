package com.roonyx.orcheya.adapter.view_holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roonyx.orcheya.databinding.ItemNetworkStateBinding
import com.roonyx.orcheya.network.network_state.NetworkState
import kotlinx.android.synthetic.main.item_network_state.view.*

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