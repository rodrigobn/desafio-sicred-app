package com.rodrigo.eventos.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigo.eventos.data.model.Event
import com.rodrigo.eventos.data.repository.Repository
import com.rodrigo.eventos.data.repository.network.helpers.ResultWrapper
import com.rodrigo.eventos.utils.handlerHttpError
import kotlinx.coroutines.launch

class EventViewModel(private val repository: Repository) : ViewModel() {
    private var _eventStateView: MutableLiveData<EventViewState> = MutableLiveData()

    fun getEventStateView(): MutableLiveData<EventViewState> = _eventStateView

    fun loadEvents(search: String? = null) {
        viewModelScope.launch {
            _eventStateView.value = EventViewState.Loading(true)

            when (val result = repository.listEvents(search)) {
                is ResultWrapper.Success -> handlerResultSuccess(result.value)
                is ResultWrapper.Error -> handlerResultError(result.code)
            }

            _eventStateView.value = EventViewState.Loading(false)
        }
    }

    private fun handlerResultSuccess(events: List<Event>) {
        when (events.isEmpty()) {
            true -> _eventStateView.value = EventViewState.NoData
            else -> _eventStateView.value = EventViewState.ShowData(events)
        }
    }

    private fun handlerResultError(code: Int?) {
        _eventStateView.value = EventViewState.Error(handlerHttpError(code))
    }
}