package com.rodrigo.eventos

import android.app.Application
import com.mlykotom.valifi.ValiFi
import com.rodrigo.eventos.di.localModule
import com.rodrigo.eventos.di.netModule
import com.rodrigo.eventos.di.repositoryModule
import com.rodrigo.eventos.di.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        ValiFi.install(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@App)

            koin.loadModules(arrayListOf(localModule, netModule, repositoryModule, viewModel))
            koin.createRootScope()
        }
    }
}