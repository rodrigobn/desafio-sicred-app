package com.rodrigo.eventos.utils

import com.rodrigo.eventos.R

fun handlerHttpError(code: Int?): Int {
    return when (code) {
        -1 -> R.string.message_error_conn
        400 -> R.string.message_error_400
        else -> R.string.message_error_500
    }
}