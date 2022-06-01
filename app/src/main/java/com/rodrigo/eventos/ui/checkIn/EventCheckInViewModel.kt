package com.rodrigo.eventos.ui.checkIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.mlykotom.valifi.fields.ValiFieldText
import com.rodrigo.eventos.R
import com.rodrigo.eventos.data.model.CheckIn
import com.rodrigo.eventos.data.model.Event
import com.rodrigo.eventos.data.repository.Repository
import com.rodrigo.eventos.data.repository.network.helpers.ResultWrapper
import com.rodrigo.eventos.utils.handlerHttpError
import kotlinx.coroutines.launch

class EventCheckInViewModel(private val repository: Repository) : ViewModel() {

    private var _eventStateView: MutableLiveData<EventCheckInViewState> = MutableLiveData()

    val name: ValiFieldText = ValiFieldText()
        .addMinLengthValidator(R.string.message_name_required, 10)
    val email: ValiFieldText = ValiFieldEmail(R.string.message_email_required)
    val form = ValiFiForm(name, email)

    fun getEventStateView(): MutableLiveData<EventCheckInViewState> = _eventStateView

    fun loadCheckedEvents() {
        viewModelScope.launch {
            _eventStateView.value = EventCheckInViewState.Loading(true)

            when (val result = repository.listCheckInEvents()) {
                is ResultWrapper.Success -> handlerResultSuccess(result.value)
                is ResultWrapper.Error -> handlerResultError(result.code)
            }

            _eventStateView.value = EventCheckInViewState.Loading(false)
        }
    }

    fun onSaveCheckIn(eventId: String) {
        if (!form.isValid) return

        viewModelScope.launch {
            _eventStateView.value = EventCheckInViewState.Loading(true)

            val checkIn = CheckIn(name.value.toString(), email.value.toString(), eventId)
            when (val result = repository.checkIn(checkIn)) {
                is ResultWrapper.Success -> _eventStateView.value = EventCheckInViewState.Checked
                is ResultWrapper.Error -> handlerResultError(result.code)
            }

            _eventStateView.value = EventCheckInViewState.Loading(false)
        }
    }

    private fun handlerResultSuccess(events: List<Event>) {
        when (events.isEmpty()) {
            true -> _eventStateView.value = EventCheckInViewState.NoData
            else -> _eventStateView.value = EventCheckInViewState.ShowData(events)
        }
    }

    private fun handlerResultError(code: Int?) {
        _eventStateView.value = EventCheckInViewState.Error(handlerHttpError(code))
    }
}