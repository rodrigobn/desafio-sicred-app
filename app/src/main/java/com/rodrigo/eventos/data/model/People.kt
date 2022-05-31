package com.rodrigo.eventos.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class People(
    val id: String?,
    val name: String,
    val picture: String,
    val eventId: String?
) : Parcelable
