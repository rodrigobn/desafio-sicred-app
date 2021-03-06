package com.rodrigo.eventos.ui.checkIn

import com.rodrigo.eventos.data.model.Event

sealed class EventCheckInViewState {
    object NoData : EventCheckInViewState()
    object Checked : EventCheckInViewState()

    class Loading(val isActive: Boolean) : EventCheckInViewState()
    class ShowData(val events: List<Event>) : EventCheckInViewState()
    class Error(val message: Int) : EventCheckInViewState()
}