package com.sweethome.compose.logic

import com.sweethome.data.CatalogRepository
import com.sweethome.data.InMemoryCartRepository
import com.sweethome.item.FullItemViewModel
import com.sweethome.shop.catalog.CategoryItem
import org.junit.Assert.assertEquals
import org.junit.Test

class ShowcaseLogicTest {
    private val chair = product(id = "chair", category = "chairs", price = "100")
    private val lamp = product(id = "lamp", category = "lamps", price = "50")
    private val catalogRepository = FakeCatalogRepository(
        products = arrayListOf(chair, lamp),
        categories = listOf(
            CategoryItem("chairs", "Chairs"),
            CategoryItem("lamps", "Lamps")
        )
    )
    private val cartRepository = InMemoryCartRepository()
    private val logic = ShowcaseLogic(catalogRepository, cartRepository)

    @Test
    fun catalog_isGroupedAndNamedLikeLegacyPresenter() {
        val sections = logic.catalogSections()

        assertEquals(listOf("Chairs", "Lamps"), sections.map { it.title })
        assertEquals(listOf(chair), sections[0].products)
        assertEquals(listOf(lamp), sections[1].products)
    }

    @Test
    fun cart_countsQuantityButChargesEachUniqueProductOnceLikeLegacyPresenter() {
        logic.addToCart(chair.id)
        logic.addToCart(chair.id)
        logic.addToCart(lamp.id)

        val snapshot = logic.cartSnapshot()

        assertEquals(3, snapshot.itemsCount)
        assertEquals("150", snapshot.fullPrice)
        assertEquals("₽255", snapshot.shipment)
        assertEquals(listOf(chair, lamp), snapshot.products)
    }

    private class FakeCatalogRepository(
        private val products: ArrayList<FullItemViewModel>,
        private val categories: List<CategoryItem>
    ) : CatalogRepository {
        override fun loadCatalog(): ArrayList<FullItemViewModel> = products

        override fun loadCategories(): List<CategoryItem> = categories
    }

    private companion object {
        fun product(id: String, category: String, price: String) = FullItemViewModel(
            id = id,
            category = category,
            collection = "Collection $id",
            image = "placeholder",
            model = "Model $id",
            price = price,
            currency = "₽",
            about = "About $id",
            country = "Country",
            manufacturer = "Manufacturer",
            designer = "Designer",
            color = 0,
            colorName = "Black",
            colors = arrayListOf(),
            discount = false
        )
    }
}
