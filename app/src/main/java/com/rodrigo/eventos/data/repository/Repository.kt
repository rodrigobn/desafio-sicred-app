package com.rodrigo.eventos.data.repository

import com.rodrigo.eventos.data.model.Event
import com.rodrigo.eventos.data.repository.local.PreferencesHelper
import com.rodrigo.eventos.data.repository.network.EventApi
import com.rodrigo.eventos.data.repository.network.helpers.ResultWrapper

interface Repository : PreferencesHelper, EventApi {
    suspend fun getFavorites(): List<String>
    suspend fun saveFavorite(eventId: String)
    suspend fun removeFavorite(eventId: String)
    suspend fun listCheckInEvents(): ResultWrapper<List<Event>>
}