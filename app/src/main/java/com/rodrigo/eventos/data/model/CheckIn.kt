package com.rodrigo.eventos.data.model

import androidx.annotation.Keep

@Keep
data class CheckIn(
    val name: String,
    val email: String,
    val eventId: String
)
