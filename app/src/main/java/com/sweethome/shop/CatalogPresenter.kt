package com.sweethome.shop

import com.sweethome.RootRouter
import com.sweethome.base.MockPresenter
import com.sweethome.data.CartRepository
import com.sweethome.data.CatalogRepository
import com.sweethome.item.FullItemViewModel
import com.sweethome.shop.catalog.CategoryViewModel

class CatalogPresenter(
    val rootRouter: RootRouter,
    catalogRepository: CatalogRepository,
    val cartRepository: CartRepository
) : MockPresenter<CatalogMvpView>(catalogRepository) {

    override fun attach(mvpView: CatalogMvpView) {
        super.attach(mvpView)
        mvpView.updateItemsCount(cartRepository.itemsCount())


        val fullItemsList = catalogRepository.loadCatalog().groupBy { it.category }
        val categories = catalogRepository.loadCategories()

        val viewModels = arrayListOf<CategoryViewModel>()
        for (category in fullItemsList.keys) {
            viewModels.add(
                CategoryViewModel(
                    categories.find { it.id == category }?.name ?: "",
                    fullItemsList[category] as ArrayList<FullItemViewModel>
                )
            )
        }

        mvpView.updateList(viewModels)
    }

    fun onItemClick(model: FullItemViewModel) {
        rootRouter.openDetails(model)
    }

    fun onCartClick() {
        rootRouter.openShoppingCart()
    }
}
