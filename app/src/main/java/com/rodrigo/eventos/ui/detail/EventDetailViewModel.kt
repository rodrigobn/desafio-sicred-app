package com.rodrigo.eventos.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.rodrigo.eventos.data.model.Event
import com.rodrigo.eventos.data.repository.Repository
import kotlinx.coroutines.launch

class EventDetailViewModel(private val repository: Repository) : ViewModel() {

    private var _eventStateView: MutableLiveData<EventDetailViewState> = MutableLiveData()

    val email = ValiFieldEmail()

    fun getEventStateView(): MutableLiveData<EventDetailViewState> = _eventStateView

    fun saveRemoveFavorite(event: Event) {
        if (event.id.isNullOrEmpty()) return

        viewModelScope.launch {
            event.isFavorite = !event.isFavorite
            when (event.isFavorite) {
                true -> {
                    repository.saveFavorite(event.id)
                    _eventStateView.value = EventDetailViewState.SaveFavorite(event)
                }
                else -> {
                    repository.removeFavorite(event.id)
                    _eventStateView.value = EventDetailViewState.RemoveFavorite(event)
                }
            }
        }
    }
}