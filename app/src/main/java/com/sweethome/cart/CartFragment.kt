package com.sweethome.cart

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sweethome.R
import com.sweethome.base.BaseFragment
import com.sweethome.base.viewModelFactory
import com.sweethome.presentation.cart.CartUiState
import com.sweethome.presentation.cart.CartViewModel
import kotlinx.coroutines.launch

class CartFragment : BaseFragment() {

    private lateinit var itemsList: RecyclerView
    private lateinit var confirmButton: View
    private lateinit var shipment: TextView
    private lateinit var emptyCart: ImageView
    private lateinit var itemsCount: TextView
    private lateinit var fullPrice: TextView

    private val adapter: CartItemsAdapter = CartItemsAdapter()
    private val viewModel: CartViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory(CartViewModel::class.java) {
                CartViewModel(
                    application.catalogRepository,
                    application.cartRepository
                )
            }
        )[CartViewModel::class.java]
    }

    override fun onViewInflated(view: View) {
        super.onViewInflated(view)
        itemsList = view.findViewById(R.id.items)
        emptyCart = view.findViewById(R.id.empty_cart)
        confirmButton = view.findViewById(R.id.confirm)
        shipment = view.findViewById(R.id.shipment_condition)
        itemsCount = view.findViewById(R.id.items_count)
        fullPrice = view.findViewById(R.id.total_price)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemsList.adapter = adapter
        itemsList.layoutManager = LinearLayoutManager(context)
        itemsList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: CartUiState) {
        if (state.isLoading) return

        adapter.updateList(state.items)
        if (state.isEmpty) {
            emptyCart.visibility = View.VISIBLE
            shipment.visibility = View.GONE
            itemsCount.visibility = View.GONE
            fullPrice.visibility = View.GONE
            confirmButton.alpha = 0.3f
            confirmButton.setOnClickListener(null)
        } else {
            emptyCart.visibility = View.GONE
            shipment.visibility = View.VISIBLE
            itemsCount.visibility = View.VISIBLE
            fullPrice.visibility = View.VISIBLE
            shipment.text = getString(R.string.delivery_from, state.shipment)
            itemsCount.text = getString(R.string.items_on, state.itemsCount.toString())
            fullPrice.text = state.totalPrice
            confirmButton.alpha = 1f
            confirmButton.setOnClickListener {
                rootRouter.openCheckout(state.checkoutPrice)
            }
        }
    }

    override fun layoutId(): Int {
        return R.layout.cart_fragment
    }

    override fun title(): String {
        return resources.getString(R.string.cart_title)
    }


    companion object {
        fun newInstance(): CartFragment {
            return CartFragment()
        }
    }
}
