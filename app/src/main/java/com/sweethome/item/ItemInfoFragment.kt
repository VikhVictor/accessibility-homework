package com.sweethome.item

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sweethome.R
import com.sweethome.base.BaseFragment
import com.sweethome.base.viewModelFactory
import com.sweethome.domain.model.Product
import com.sweethome.presentation.item.ItemDetailsUiState
import com.sweethome.presentation.item.ItemDetailsViewModel
import kotlinx.coroutines.launch

class ItemInfoFragment : BaseFragment() {

    private lateinit var cartItemsAmount: TextView
    private lateinit var aboutText: TextView
    private lateinit var modelName: TextView
    private lateinit var addToCartButton: View
    private lateinit var image: ImageView
    private lateinit var designer: TextView
    private lateinit var price: TextView
    private lateinit var cartBtn: View
    private lateinit var itemId: String
    private lateinit var collection: String
    private val viewModel: ItemDetailsViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory(ItemDetailsViewModel::class.java) {
                ItemDetailsViewModel(
                    itemId,
                    application.catalogRepository,
                    application.cartRepository
                )
            }
        )[ItemDetailsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemId = arguments?.getString("item_id", "") ?: ""
        collection = arguments?.getString("collection", "") ?: ""
    }

    override fun onViewInflated(view: View) {
        super.onViewInflated(view)
        image = view.findViewById(R.id.image)
        designer = view.findViewById(R.id.designer)
        addToCartButton = view.findViewById(R.id.add_to_cart)
        cartItemsAmount = view.findViewById(R.id.items_count)
        aboutText = view.findViewById(R.id.about_text)
        modelName = view.findViewById(R.id.model)
        price = view.findViewById(R.id.price)
        cartBtn = view.findViewById(R.id.cart_icon)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addToCartButton.setOnClickListener {
            viewModel.addToCart()
        }
        cartBtn.setOnClickListener {
            rootRouter.openShoppingCart()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: ItemDetailsUiState) {
        if (state.cartItemsCount == 0) {
            cartItemsAmount.visibility = View.GONE
        } else {
            cartItemsAmount.visibility = View.VISIBLE
            cartItemsAmount.text = state.cartItemsCount.toString()
        }

        state.product?.let { product ->
            val imageId = resources.getIdentifier(
                product.image,
                "drawable",
                context?.packageName
            )
            image.setImageResource(imageId)
            designer.text = product.designer
            price.text = "${product.currency} ${product.price}"
            modelName.text = product.model
            aboutText.text = product.about
        }
    }

    override fun layoutId(): Int {
        return R.layout.item_info_layout
    }

    override fun title(): String {
        return collection
    }

    override fun showCart(): Boolean {
        return true
    }

    companion object {
        fun newInstance(model: Product): ItemInfoFragment {
            val fragment = ItemInfoFragment()
            val bundle = Bundle()
            bundle.putString("item_id", model.id)
            bundle.putString("collection", model.collection)
            fragment.arguments = bundle
            return fragment
        }
    }
}
