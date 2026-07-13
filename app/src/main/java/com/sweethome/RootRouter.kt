package com.sweethome

import com.sweethome.item.FullItemViewModel

interface RootRouter {
    fun openDetails(model: FullItemViewModel)
    fun openShoppingCart()
    fun openCheckout(fullPrice: String)
    fun showCatalog()
    fun onBackPressed(): Boolean
}