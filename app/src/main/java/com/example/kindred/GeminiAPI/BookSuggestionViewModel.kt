package com.example.kindred.GeminiAPI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kindred.DataModels.Book
import com.example.kindred.DataModels.BookSuggestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

/**
 * Viewmodel for book suggestion from Gemini API, survives screen rotation
 */
class BookSuggestionViewModel : ViewModel() {

    private val _selectedBooks = MutableStateFlow<List<Book>>(emptyList())
    val selectedBooks: StateFlow<List<Book>> = _selectedBooks

    private val _selectedAttributes = MutableStateFlow<List<String>>(emptyList())
    val selectedAttributes: StateFlow<List<String>> = _selectedAttributes

    private val _selectedGenres = MutableStateFlow<List<String>>(emptyList())
    val selectedGenres: StateFlow<List<String>> = _selectedGenres

    private val _attributeOrder = MutableStateFlow<List<String>>(emptyList())
    val attributeOrder: StateFlow<List<String>> = _attributeOrder

    private val _suggestions = MutableStateFlow<List<BookSuggestion>>(emptyList())
    val suggestions: StateFlow<List<BookSuggestion>> = _suggestions

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun setSelectedBooks(books: List<Book>) {
        _selectedBooks.value = books
    }

    fun setSelectedAttributes(attributes: List<String>) {
        _selectedAttributes.value = attributes
    }

    fun setAttributeOrder(order: List<String>) {
        _attributeOrder.value = order
    }

    /**
     * fetches suggestions from Gemini API
     */

    fun fetchSuggestions() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = getGeminiSuggestions(
                    entities = _selectedBooks.value.joinToString(", ") { it.title ?: "" },
                    attributes = _selectedAttributes.value.joinToString(", "),
                    genres = _selectedGenres.value.joinToString(", "),
                    order = _attributeOrder.value
                )
                val json = Json { ignoreUnknownKeys = true }
                val parsed = json.decodeFromString<List<BookSuggestion>>(result)
                _suggestions.value = parsed
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }

        }
    }
}