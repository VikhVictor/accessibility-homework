package com.sweethome

import com.sweethome.domain.model.Product

interface RootRouter {
    fun openDetails(model: Product)
    fun openShoppingCart()
    fun openCheckout(fullPrice: String)
    fun showCatalog()
    fun onBackPressed(): Boolean
}
