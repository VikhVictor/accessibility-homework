package com.sweethome.cart

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sweethome.R
import com.sweethome.base.BaseFragment
import com.sweethome.base.MvpView

class CartFragment : BaseFragment<CartPresenter, CartMvpView>() {

    private lateinit var itemsList: RecyclerView
    private lateinit var confirmButton: View
    private lateinit var shipment: TextView
    private lateinit var emptyCart: ImageView
    private lateinit var itemsCount: TextView
    private lateinit var fullPrice: TextView

    private val adapter: CartItemsAdapter = CartItemsAdapter()

    init {
        mvpView = object : CartMvpView {
            override fun onDataLoaded(viewModel: CartModel) {
                adapter.updateList(viewModel.items)
                emptyCart.visibility = View.GONE
                shipment.text = getString(R.string.delivery_from, viewModel.shipment)
                itemsCount.text = getString(R.string.items_on, viewModel.itemsCount)
                fullPrice.text = viewModel.price
                confirmButton.alpha = 1f
                confirmButton.setOnClickListener(checkoutClickListener)
            }

            override fun showEmptyCart() {
                emptyCart.visibility = View.VISIBLE
                shipment.visibility = View.GONE
                itemsCount.visibility = View.GONE
                fullPrice.visibility = View.GONE
                confirmButton.alpha = 0.3f
                confirmButton.setOnClickListener(null)
            }
        }
    }

    private val checkoutClickListener: View.OnClickListener =
        View.OnClickListener { presenter.onCheckoutClick() }

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

interface CartMvpView : MvpView {
    fun onDataLoaded(viewModel: CartModel)
    fun showEmptyCart()
}
