package com.rodrigo.eventos.di

import android.app.Application
import android.content.Context
import coil.ImageLoader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rodrigo.eventos.BuildConfig
import com.rodrigo.eventos.data.repository.network.EventApi
import com.rodrigo.eventos.data.repository.network.EventApiImpl
import com.rodrigo.eventos.data.repository.network.EventService
import com.rodrigo.eventos.data.repository.network.helpers.NetworkHelper
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


val netModule = module {
    factory { provideGson() }
    factory { provideLoggerInterceptor() }
    factory { provideCache(androidApplication()) }
    factory { NetworkHelper(get()) }

    single { provideRetrofit(get(), get()) }
    single { provideEventService(get()) }
    single { provideEventApi(get(), get()) }
    single { provideHttpClient(get(), get()) }
    single { provideImageLoader(get(), get()) }
}

/**
 * Provide Retrofit instance.
 */
fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(factory))
        .build()
}

/**
 * Provide Cache instance used in OkHttpClient.
 */
fun provideCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024
    return Cache(application.cacheDir, cacheSize.toLong())
}

/**
 * Provide Gson instance used in Retrofit.
 */
fun provideGson(): Gson {
    return GsonBuilder().create()
}

/**
 * Provide http client security.
 */
fun provideHttpClient(cache: Cache, interceptor: Interceptor): OkHttpClient {
    return OkHttpClient
        .Builder()
        .cache(cache)
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(interceptor)
        .build()
}

/**
 * Provides Interceptor for logs used in OkHttpClient.
 */
fun provideLoggerInterceptor(): Interceptor {
    val logging = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}

/**
 * Provide EventService instance.
 */
fun provideEventService(retrofit: Retrofit): EventService {
    return retrofit.create(EventService::class.java)
}

/**
 * Provide EventApi instance.
 */
fun provideEventApi(service: EventService, networkHelper: NetworkHelper): EventApi {
    return EventApiImpl(service, networkHelper, Dispatchers.IO)
}

/**
 * Provide ImageLoader(Coil) instance.
 */
fun provideImageLoader(context: Context, httpClient: OkHttpClient): ImageLoader {
    return ImageLoader.Builder(context)
        .crossfade(false)
        .okHttpClient { httpClient }
        .build()
}