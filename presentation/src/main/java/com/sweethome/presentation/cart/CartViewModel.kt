package com.sweethome.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweethome.domain.model.Product
import com.sweethome.domain.repository.CartRepository
import com.sweethome.domain.repository.CatalogRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartViewModel(
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = mutableUiState.asStateFlow()

    private var products: List<Product>? = null
    private var cartItems: Map<String, Int> = emptyMap()

    init {
        viewModelScope.launch {
            cartRepository.cartItems.collectLatest { items ->
                cartItems = items
                updateUiState()
            }
        }
        viewModelScope.launch {
            try {
                products = catalogRepository.loadCatalog()
                updateUiState()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                mutableUiState.value = mutableUiState.value.copy(
                    isLoading = false,
                    error = error.message ?: error::class.java.simpleName
                )
            }
        }
    }

    private fun updateUiState() {
        val loadedProducts = products ?: return
        val selectedProducts = loadedProducts.filter { it.id in cartItems }
        val fullPrice = selectedProducts.sumOf { it.price.toInt() }
        mutableUiState.value = CartUiState(
            isLoading = false,
            items = selectedProducts,
            itemsCount = cartItems.values.sum(),
            totalPrice = "₽$fullPrice",
            checkoutPrice = fullPrice.toString()
        )
    }
}
