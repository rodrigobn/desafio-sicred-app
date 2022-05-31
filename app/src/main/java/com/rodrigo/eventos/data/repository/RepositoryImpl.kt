package com.rodrigo.eventos.data.repository

import com.rodrigo.eventos.data.model.CheckIn
import com.rodrigo.eventos.data.model.Event
import com.rodrigo.eventos.data.repository.local.PreferencesHelper
import com.rodrigo.eventos.data.repository.network.EventApi
import com.rodrigo.eventos.data.repository.network.helpers.ResultWrapper

class RepositoryImpl(
    private val pref: PreferencesHelper,
    private val eventApi: EventApi
) : Repository {
    companion object {
        private const val KEY_EVENTS_FAVORITES = "key_favorites"
        private const val KEY_EVENTS_CHECK_IN = "key_check_in"
    }

    override suspend fun listEvents(search: String?): ResultWrapper<List<Event>> {
        val favorites = getFavorites()
        return eventApi.listEvents(search).apply {
            if (this is ResultWrapper.Success) {
                for (fav in favorites) {
                    findEventId(this.value, fav).also { eventIndex ->
                        if (eventIndex > -1) this.value[eventIndex].isFavorite = true
                    }
                }
            }
        }
    }

    override suspend fun checkIn(checkIn: CheckIn): ResultWrapper<Unit> {
        return eventApi.checkIn(checkIn).apply {
            if (this is ResultWrapper.Success) {
                val checkInList = getListOfString(KEY_EVENTS_CHECK_IN).apply {
                    if (!this.contains(checkIn.eventId)) add(checkIn.eventId)
                }
                saveListOfString(KEY_EVENTS_CHECK_IN, checkInList)
            }
        }
    }

    private fun findEventId(events: List<Event>, id: String): Int {
        for ((index, elem) in events.withIndex()) {
            if (elem.id == id) return index
        }
        return -1
    }

    override suspend fun saveListOfString(key: String, values: List<String>) {
        pref.saveListOfString(key, values)
    }

    override suspend fun getListOfString(key: String): MutableList<String> {
        return pref.getListOfString(key)
    }

    override suspend fun getFavorites(): List<String> {
        return getListOfString(KEY_EVENTS_FAVORITES)
    }

    override suspend fun saveFavorite(eventId: String) {
        val favorites = getListOfString(KEY_EVENTS_FAVORITES).apply { add(eventId) }
        saveListOfString(KEY_EVENTS_FAVORITES, favorites)
    }

    override suspend fun removeFavorite(eventId: String) {
        val favorites = getListOfString(KEY_EVENTS_FAVORITES).apply { remove(eventId) }
        saveListOfString(KEY_EVENTS_FAVORITES, favorites)
    }

    override suspend fun listCheckInEvents(): ResultWrapper<List<Event>> {
        val checkInList = getListOfString(KEY_EVENTS_CHECK_IN)
        val result = arrayListOf<Event>()
        return eventApi.listEvents().apply {
            if (this is ResultWrapper.Success) {
                for (check in checkInList) {
                    findEventId(this.value, check).also { eventIndex ->
                        if (eventIndex > -1) result.add(this.value[eventIndex])
                    }
                }
                return ResultWrapper.Success(result)
            }
        }
    }
}