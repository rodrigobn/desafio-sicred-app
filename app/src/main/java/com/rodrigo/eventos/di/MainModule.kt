package com.rodrigo.eventos.di

import com.rodrigo.eventos.ui.EventViewModel
import com.rodrigo.eventos.ui.checkIn.EventCheckInViewModel
import com.rodrigo.eventos.ui.detail.EventDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel { EventViewModel(get()) }
    viewModel { EventDetailViewModel(get()) }
    viewModel { EventCheckInViewModel(get()) }
}