package com.fawry.task.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("app:loadImageUrl")
fun load(view: ImageView, url: String?) {
    Glide.with(view).load("https://image.tmdb.org/t/p/w500/$url").into(view)
}