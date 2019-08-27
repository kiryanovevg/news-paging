package com.roonyx.orcheya.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.roonyx.orcheya.adapter.view_holder.BindingViewHolder
import com.roonyx.orcheya.adapter.view_holder.NetworkStateViewHolder
import com.roonyx.orcheya.network.network_state.NetworkState

class RecyclerViewPagedAdapter<T, VM : ViewModel>(
    @LayoutRes private val layoutId: Int,
    private val vm: VM,
    diffUtil: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        private const val VIEW_ITEM = 111
        private const val VIEW_NETWORK_STATE = 222
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        VIEW_ITEM -> BindingViewHolder.create(layoutId, parent)
        VIEW_NETWORK_STATE -> NetworkStateViewHolder.create(parent)
        else -> throw IllegalArgumentException("Unknown view type")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BindingViewHolder -> getItem(position)?.also { holder.bind(vm, it) }
            is NetworkStateViewHolder -> networkState?.also { holder.bind(it) }
            else -> throw IllegalArgumentException("Unknown type View Holder")
        }
    }

    override fun getItemViewType(position: Int): Int = if (hasNetworkStateItem && position == itemCount - 1)
        VIEW_NETWORK_STATE else VIEW_ITEM

    var networkState: NetworkState? = null
        set(newNetworkState) {
            currentList?.also { list ->
                if (list.size != 0) {
                    val prevState = field
                    val hadNetworkStateItem = hasNetworkStateItem
                    field = newNetworkState

                    if (hadNetworkStateItem != hasNetworkStateItem) {
                        if (hadNetworkStateItem) notifyItemRemoved(itemCount)
                        else notifyItemInserted(itemCount)
                    } else if (hasNetworkStateItem && prevState != newNetworkState)
                        notifyItemChanged(itemCount - 1)
                }
            }
        }

    private val hasNetworkStateItem get() = networkState != null && networkState != NetworkState.SUCCESS
}