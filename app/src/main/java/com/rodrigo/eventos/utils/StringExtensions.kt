package com.rodrigo.eventos.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String = "yyyy-MM-dd"): String = SimpleDateFormat(
    format, Locale.getDefault()
).apply { timeZone = TimeZone.getTimeZone("UTC") }.format(Date(this.time))