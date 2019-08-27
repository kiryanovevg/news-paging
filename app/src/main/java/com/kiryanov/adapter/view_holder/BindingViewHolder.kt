package com.roonyx.orcheya.adapter.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.roonyx.orcheya.BR

class BindingViewHolder private constructor(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(@LayoutRes layoutId: Int, parent: ViewGroup) = BindingViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
        )
    }

    fun <T> bind(vm: ViewModel, item: T) {
        binding.setVariable(BR.item, item)
        binding.setVariable(BR.vm, vm)
        binding.executePendingBindings()
    }
}