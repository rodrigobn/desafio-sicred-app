package com.rodrigo.eventos.data.repository.network

import com.rodrigo.eventos.data.model.CheckIn
import com.rodrigo.eventos.data.model.Event
import com.rodrigo.eventos.data.repository.network.helpers.NetworkHelper
import com.rodrigo.eventos.data.repository.network.helpers.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher

class EventApiImpl(
    private val service: EventService,
    private val networkHelper: NetworkHelper,
    private val dispatcher: CoroutineDispatcher
) : EventApi {
    override suspend fun listEvents(search: String?): ResultWrapper<List<Event>> {
        return networkHelper.safeApiCall(dispatcher) {
            service.listEvents(search)
        }
    }

    override suspend fun checkIn(checkIn: CheckIn): ResultWrapper<Unit> {
        return networkHelper.safeApiCall(dispatcher) {
            service.checkIn(checkIn)
        }
    }
}