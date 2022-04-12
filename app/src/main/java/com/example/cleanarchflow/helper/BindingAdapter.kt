package com.example.cleanarchflow.helper


import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("bind_imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let { view.setPhoto(it, view.context) }
}
