package com.rodrigo.eventos.data.repository.local

import android.content.SharedPreferences

class PreferencesHelperImpl(
    private val mPrefs: SharedPreferences
) : PreferencesHelper {
    companion object {
        const val SEPARATOR = ","
    }

    override suspend fun saveListOfString(key: String, values: List<String>) {
        mPrefs.edit()
            .putString(key, values.joinToString(SEPARATOR))
            .apply()
    }

    override suspend fun getListOfString(key: String): MutableList<String> {
        val result = arrayListOf<String>()
        mPrefs.getString(key, "")?.also {
            if (it.isEmpty()) return result
            result.addAll(it.split(SEPARATOR))
        }
        return result
    }
}