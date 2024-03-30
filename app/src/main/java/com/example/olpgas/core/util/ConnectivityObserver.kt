package com.example.olpgas.core.util

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe() : Flow<State>

    enum class State{
        Available, Unavailable, Lost, Losing
    }
}