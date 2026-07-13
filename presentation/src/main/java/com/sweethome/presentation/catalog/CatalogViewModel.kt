package com.sweethome.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweethome.domain.repository.CartRepository
import com.sweethome.domain.repository.CatalogRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val catalogRepository: CatalogRepository,
    cartRepository: CartRepository
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            cartRepository.cartItems.collectLatest { items ->
                mutableUiState.update {
                    it.copy(cartItemsCount = items.values.sum())
                }
            }
        }
        viewModelScope.launch {
            loadCatalog()
        }
    }

    private suspend fun loadCatalog() {
        try {
            val products = catalogRepository.loadCatalog()
            val categoryNames = catalogRepository.loadCategories().associateBy { it.id }
            val categories = products.groupBy { it.category }.map { (categoryId, items) ->
                CategoryUiModel(
                    title = categoryNames[categoryId]?.name.orEmpty(),
                    items = items
                )
            }
            mutableUiState.update {
                it.copy(
                    isLoading = false,
                    categories = categories,
                    error = null
                )
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            mutableUiState.update {
                it.copy(
                    isLoading = false,
                    error = error.message ?: error::class.java.simpleName
                )
            }
        }
    }
}
