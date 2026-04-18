package com.example.kindred

import androidx.lifecycle.ViewModel
import com.example.kindred.DataModels.AudibleItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * ViewModel for the Import screen, required as NavController cannot pass complex objects between composables.
 *
 */
class ImportViewModel : ViewModel() {
    private val _importedItems = MutableStateFlow<List<AudibleItem>>(emptyList())
    val importedItems: StateFlow<List<AudibleItem>> = _importedItems

    fun setWishList(items: List<AudibleItem>) {
        _importedItems.value = items
    }

    fun setOwnedItems(items: List<AudibleItem>) {
        _importedItems.value = items
    }

    fun removeItem(item: AudibleItem) {
        _importedItems.value = _importedItems.value.filter { it.asin != item.asin }
    }

    private val _importStatus = MutableStateFlow("wishlist")
    val importStatus: StateFlow<String> = _importStatus

    fun setImportStatus(status: String) {
        _importStatus.value = status
    }
}