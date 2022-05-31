package com.rodrigo.eventos.data.repository.local

interface PreferencesHelper {
    suspend fun saveListOfString(key: String, values: List<String>)
    suspend fun getListOfString(key: String): MutableList<String>
}