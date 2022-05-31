package com.rodrigo.eventos.data.repository.network

import com.rodrigo.eventos.data.model.CheckIn
import com.rodrigo.eventos.data.model.Event
import com.rodrigo.eventos.data.repository.network.helpers.ResultWrapper

interface EventApi {
    suspend fun listEvents(search: String? = null): ResultWrapper<List<Event>>
    suspend fun checkIn(checkIn: CheckIn): ResultWrapper<Unit>
}