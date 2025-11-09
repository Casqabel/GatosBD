package com.example.catdeployer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catdeployer.data.PreferenceStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PreferenceViewModel (private val preferenceStore: PreferenceStore): ViewModel(){
    private val _state = MutableStateFlow(State(false))
    val state: StateFlow<State> = _state

    init {
        viewModelScope.launch {
            preferenceStore.orderFlow.collect {
                _state.emit(State(it))
            }
        }
    }

    fun saveOrder(order: Boolean) {
        viewModelScope.launch {
            preferenceStore.saveOrder(order)
        }
    }

    data class State(
        val order: Boolean
    )


}