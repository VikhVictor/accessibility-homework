package com.sweethome

import androidx.fragment.app.FragmentManager
import com.sweethome.base.BaseFragment
import com.sweethome.cart.CartFragment
import com.sweethome.checkout.CheckoutFragment
import com.sweethome.domain.model.Product
import com.sweethome.item.ItemInfoFragment
import com.sweethome.shop.CatalogFragment

class RootRouterImpl(
    private val fragmentManager: FragmentManager
) : RootRouter {
    override fun openDetails(model: Product) {
        openFragment(ItemInfoFragment.newInstance(model))
    }

    override fun openShoppingCart() {
        openFragment(CartFragment.newInstance())
    }

    override fun openCheckout(fullPrice: String) {
        openFragment(CheckoutFragment.newInstance(fullPrice))
    }

    override fun showCatalog() {
        openFragment(CatalogFragment.newInstance())
    }

    private fun openFragment(fragment: BaseFragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed(): Boolean {
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
            return true
        }
        return false
    }

}
