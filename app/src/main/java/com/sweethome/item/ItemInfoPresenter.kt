package com.sweethome.item

import com.sweethome.RootRouterImpl
import com.sweethome.base.MockPresenter
import com.sweethome.data.CartRepository
import com.sweethome.data.CatalogRepository

class ItemInfoPresenter(
    private val rootRouter: RootRouterImpl,
    catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository
): MockPresenter<ItemInfoMvpView>(catalogRepository) {

    private var itemId = ""

    fun init(itemId: String) {
        this.itemId = itemId
    }

    override fun attach(mvpView: ItemInfoMvpView) {
        super.attach(mvpView)
        val itemInfo = findItem(itemId)
        if (itemInfo != null) {
            mvpView.updateInfo(itemInfo)
        }
        mvpView.updateItemsCount(cartRepository.itemsCount())
    }

    private fun findItem(itemId: String): FullItemViewModel? {
        return catalogRepository.loadCatalog().find { it.id == itemId }
    }

    fun onAddToCartClicked() {
        cartRepository.addItem(itemId)
        mvpView?.updateItemsCount(cartRepository.itemsCount())
    }

    fun onCartClicked() {
        rootRouter.openShoppingCart()
    }
}
