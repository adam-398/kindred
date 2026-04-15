package com.example.kindred.API.Google

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Manges all data state for the UI and survives screen rotations.
 * Calls the repository on a background thread using viewModelScope.launch.
 * Exposes read only StateFlows for the UI to observe.
 * The UI never touches the network directly, only ready from the state flow
 */

class GoogleViewModel : ViewModel() {

    private val _bookData = MutableStateFlow<List<BookItem>>(emptyList())
    val bookData: StateFlow<List<BookItem>> = _bookData

    private val bookRepository = GoogleRepository()

    private val _error = MutableStateFlow<String?>(null)

    val error: StateFlow<String?> = _error

    fun fetchBookData(title: String) {
        viewModelScope.launch {
            try {
                _bookData.value = bookRepository.getBookData(title)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun clearSuggestions() {
        _bookData.value = emptyList()
    }
}