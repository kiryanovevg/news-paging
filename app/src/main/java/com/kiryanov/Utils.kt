package com.kiryanov

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.gson.JsonParser
import retrofit2.HttpException

fun getHttpError(e: Throwable): String? {
    return if (e is HttpException) {
        e.response()?.errorBody()?.string()?.let { msg ->
            JsonParser().parse(msg)?.let {
                val error = "message"
                if (it.isJsonObject && it.asJsonObject.has(error))
                    it.asJsonObject.get(error).asString
                else
                    msg
            } ?: msg
        } ?: e.message()
    } else e.message
}

@BindingAdapter("src_url")
fun ImageView.setImageUrl(url: String?) = url?.let { Glide.with(this) }
    ?.load(url)
    ?.into(this)