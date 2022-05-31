package com.rodrigo.eventos.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.rodrigo.eventos.BuildConfig
import com.rodrigo.eventos.data.repository.local.PreferencesHelper
import com.rodrigo.eventos.data.repository.local.PreferencesHelperImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val localModule = module {
    single { provideSharedPreferences(androidApplication()) }
    single { providePreferencesHelper(get()) }
}

fun provideSharedPreferences(application: Application): SharedPreferences {
    return application.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
}

fun providePreferencesHelper(appPref: SharedPreferences): PreferencesHelper {
    return PreferencesHelperImpl(appPref)
}