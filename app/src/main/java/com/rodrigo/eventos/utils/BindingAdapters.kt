package com.rodrigo.eventos.utils

import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rodrigo.eventos.R
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

object BindingImages : KoinComponent {
    private val imageLoader: ImageLoader by inject()

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        view.load(url, imageLoader) {
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_placeholder)
        }
    }
}

@BindingAdapter(value = ["dateLong", "dateFormat"], requireAll = true)
fun setTextDateFormat(tv: TextView, date: Long, format: String) {
    tv.text = Date(date).toString(format)
}

@BindingAdapter("changeIconFavorite")
fun setIconFavorite(bt: FloatingActionButton, isFavorite: Boolean?) {
    when (isFavorite) {
        true -> {
            bt.setImageResource(R.drawable.ic_baseline_favorite_24)
            bt.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(bt.context, R.color.colorHeart)
            )
        }
        else -> {
            bt.setImageResource(R.drawable.ic_baseline_not_favorite_border_24)
            bt.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(bt.context, R.color.white)
            )
        }
    }
}

@BindingAdapter("changeIconFavorite")
fun setIconFavorite(img: ImageView, isFavorite: Boolean?) {
    img.setColorFilter(ContextCompat.getColor(img.context, R.color.colorHeart))
    when (isFavorite) {
        true -> img.setImageResource(R.drawable.ic_baseline_favorite_24)
        else -> img.setImageResource(R.drawable.ic_baseline_not_favorite_border_24)
    }
}