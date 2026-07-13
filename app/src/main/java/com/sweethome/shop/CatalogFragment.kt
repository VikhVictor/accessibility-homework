package com.sweethome.shop

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sweethome.R
import com.sweethome.base.BaseFragment
import com.sweethome.base.viewModelFactory
import com.sweethome.domain.model.Product
import com.sweethome.presentation.catalog.CatalogUiState
import com.sweethome.presentation.catalog.CatalogViewModel
import com.sweethome.shop.catalog.CatalogAdapter
import com.sweethome.shop.category.OnItemClickListener
import kotlinx.coroutines.launch

class CatalogFragment : BaseFragment() {

    private lateinit var catalog: RecyclerView
    private lateinit var cartItemsAmount: TextView
    private lateinit var cartIcon: View
    private val adapter: CatalogAdapter = CatalogAdapter()
    private val viewModel: CatalogViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory(CatalogViewModel::class.java) {
                CatalogViewModel(
                    application.catalogRepository,
                    application.cartRepository
                )
            }
        )[CatalogViewModel::class.java]
    }
    private val onItemClickListener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(model: Product) {
            rootRouter.openDetails(model)
        }
    }

    override fun onViewInflated(view: View) {
        super.onViewInflated(view)
        catalog = view.findViewById(R.id.catalog)
        catalog.layoutManager = LinearLayoutManager(context)
        catalog.adapter = adapter
        adapter.setOnItemClickListener(onItemClickListener)
        cartItemsAmount = view.findViewById(R.id.items_count)
        cartIcon = view.findViewById(R.id.cart_icon)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartIcon.setOnClickListener {
            rootRouter.openShoppingCart()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: CatalogUiState) {
        adapter.setCatalogItems(state.categories)
        if (state.cartItemsCount == 0) {
            cartItemsAmount.visibility = View.GONE
        } else {
            cartItemsAmount.visibility = View.VISIBLE
            cartItemsAmount.text = state.cartItemsCount.toString()
        }
    }

    override fun layoutId(): Int {
        return R.layout.shop_catalog_layout
    }

    override fun title(): String {
        return resources.getString(R.string.shop_catalog_title)
    }

    override fun showCart(): Boolean {
        return true
    }

    companion object {
        fun newInstance(): CatalogFragment {
            return CatalogFragment()
        }
    }
}
