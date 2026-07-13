package com.sweethome.presentation

import com.sweethome.domain.model.Category
import com.sweethome.domain.model.Product
import com.sweethome.domain.repository.CartRepository
import com.sweethome.domain.repository.CatalogRepository
import com.sweethome.presentation.cart.CartViewModel
import com.sweethome.presentation.catalog.CatalogViewModel
import com.sweethome.presentation.checkout.CheckoutViewModel
import com.sweethome.presentation.item.ItemDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PresentationViewModelsTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val product = Product(
        id = "chair",
        category = "chairs",
        collection = "Collection",
        image = "chair",
        model = "Chair",
        price = "100",
        currency = "₽",
        about = "About",
        country = "Country",
        manufacturer = "Manufacturer",
        designer = "Designer",
        color = 0,
        colorName = "Black",
        colors = emptyList()
    )

    @Test
    fun catalogLoadsProductsAndObservesCart() = runTest {
        val cartRepository = FakeCartRepository()
        val viewModel = CatalogViewModel(
            FakeCatalogRepository(listOf(product)),
            cartRepository
        )

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Chairs", viewModel.uiState.value.categories.single().title)
        assertEquals(product, viewModel.uiState.value.categories.single().items.single())

        cartRepository.addItem(product.id)
        advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.cartItemsCount)
    }

    @Test
    fun cartBuildsScreenStateFromRepositories() = runTest {
        val cartRepository = FakeCartRepository().apply {
            addItem(product.id)
            addItem(product.id)
        }
        val viewModel = CartViewModel(
            FakeCatalogRepository(listOf(product)),
            cartRepository
        )

        advanceUntilIdle()

        assertEquals(listOf(product), viewModel.uiState.value.items)
        assertEquals(2, viewModel.uiState.value.itemsCount)
        assertEquals("₽100", viewModel.uiState.value.totalPrice)
        assertEquals("100", viewModel.uiState.value.checkoutPrice)
    }

    @Test
    fun itemDetailsAddsProductToCart() = runTest {
        val cartRepository = FakeCartRepository()
        val viewModel = ItemDetailsViewModel(
            product.id,
            FakeCatalogRepository(listOf(product)),
            cartRepository
        )

        advanceUntilIdle()
        viewModel.addToCart()
        advanceUntilIdle()

        assertEquals(product, viewModel.uiState.value.product)
        assertEquals(1, viewModel.uiState.value.cartItemsCount)
    }

    @Test
    fun checkoutUpdatesSelectionAndPrices() {
        val viewModel = CheckoutViewModel("100")

        assertEquals("₽355", viewModel.uiState.value.totalPrice)
        assertFalse(viewModel.confirm())

        viewModel.selectPayment("visa_3322")
        viewModel.selectDelivery("post")

        assertTrue(viewModel.confirm())
        assertEquals("₽305", viewModel.uiState.value.shipmentPrice)
        assertEquals("₽405", viewModel.uiState.value.totalPrice)
    }

    private class FakeCatalogRepository(
        private val products: List<Product>
    ) : CatalogRepository {
        override suspend fun loadCatalog(): List<Product> = products

        override suspend fun loadCategories(): List<Category> =
            listOf(Category(id = "chairs", name = "Chairs"))
    }

    private class FakeCartRepository : CartRepository {
        private val mutableItems = MutableStateFlow<Map<String, Int>>(emptyMap())
        override val cartItems: StateFlow<Map<String, Int>> = mutableItems.asStateFlow()

        override fun addItem(id: String) {
            mutableItems.update { items ->
                items + (id to ((items[id] ?: 0) + 1))
            }
        }

        override fun removeItem(id: String) {
            mutableItems.update { items -> items - id }
        }

        override fun itemsCount(): Int = mutableItems.value.values.sum()
    }
}
