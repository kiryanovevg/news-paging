package com.kiryanov.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class News(
    val title: String,
    val description: String,
    @SerializedName("urlToImage") val urlToImage: String,
    val url: String
) {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean = oldItem === newItem
            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean = oldItem == newItem
        }
    }
}

data class NewsResponse(
    @SerializedName("totalResults") val totalResults: Int,
    val status: String,
    val articles: List<News>
)