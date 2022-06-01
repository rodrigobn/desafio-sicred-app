package com.rodrigo.eventos.ui.detail

import com.rodrigo.eventos.data.model.Event

sealed class EventDetailViewState {
    class SaveFavorite(val event: Event) : EventDetailViewState()
    class RemoveFavorite(val event: Event) : EventDetailViewState()
}