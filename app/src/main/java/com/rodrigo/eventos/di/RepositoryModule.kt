package com.rodrigo.eventos.di

import com.rodrigo.eventos.data.repository.Repository
import com.rodrigo.eventos.data.repository.RepositoryImpl
import com.rodrigo.eventos.data.repository.local.PreferencesHelper
import com.rodrigo.eventos.data.repository.network.EventApi
import org.koin.dsl.module

val repositoryModule = module {
    single { provideRepository(get(), get()) }
}

fun provideRepository(preferencesHelper: PreferencesHelper, eventApi: EventApi): Repository {
    return RepositoryImpl(preferencesHelper, eventApi)
}