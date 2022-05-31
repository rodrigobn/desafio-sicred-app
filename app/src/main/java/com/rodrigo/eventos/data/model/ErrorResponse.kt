package com.rodrigo.eventos.data.model

import androidx.annotation.Keep

@Keep
class ErrorResponse(
    val code: Int,
    val message: String?,
    val description: String?
)