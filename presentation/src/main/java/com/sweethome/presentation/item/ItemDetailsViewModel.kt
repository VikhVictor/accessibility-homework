package com.sweethome.presentation.item

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

class ItemDetailsViewModel(
    private val itemId: String,
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(ItemDetailsUiState())
    val uiState: StateFlow<ItemDetailsUiState> = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            cartRepository.cartItems.collectLatest { items ->
                mutableUiState.update {
                    it.copy(cartItemsCount = items.values.sum())
                }
            }
        }
        viewModelScope.launch {
            try {
                val product = catalogRepository.loadCatalog().find { it.id == itemId }
                mutableUiState.update {
                    it.copy(
                        isLoading = false,
                        product = product,
                        error = if (product == null) "Product not found" else null
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

    fun addToCart() {
        cartRepository.addItem(itemId)
    }
}
