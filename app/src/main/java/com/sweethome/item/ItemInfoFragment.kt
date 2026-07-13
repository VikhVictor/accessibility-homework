package com.sweethome.item

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sweethome.R
import com.sweethome.base.BaseFragment
import com.sweethome.base.MvpView

class ItemInfoFragment : BaseFragment<ItemInfoPresenter, ItemInfoMvpView>() {

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

    init {
        mvpView = object : ItemInfoMvpView {
            override fun updateItemsCount(itemsCount: Int) {
                if (itemsCount == 0) {
                    cartItemsAmount.visibility = View.GONE
                } else {
                    cartItemsAmount.visibility = View.VISIBLE
                    cartItemsAmount.text = itemsCount.toString()
                }
            }

            override fun updateInfo(viewModel: FullItemViewModel) {
                val imageId = resources.getIdentifier(
                    viewModel.image,
                    "drawable", context?.packageName
                )
                image.setImageResource(imageId)
                designer.setText(viewModel.designer)
                price.text = "${viewModel.currency} ${viewModel.price}"
                modelName.text = viewModel.model
                aboutText.text = viewModel.about
            }
        }
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
        presenter.init(itemId)
        presenter.attach(mvpView)
        addToCartButton.setOnClickListener {
            presenter.onAddToCartClicked()
        }
        cartBtn.setOnClickListener {
            presenter.onCartClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()
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
        fun newInstance(model: FullItemViewModel): ItemInfoFragment {
            val fragment = ItemInfoFragment()
            val bundle = Bundle()
            bundle.putString("item_id", model.id)
            bundle.putString("collection", model.collection)
            fragment.arguments = bundle
            return fragment
        }
    }
}

interface ItemInfoMvpView : MvpView {
    fun updateItemsCount(count: Int)
    fun updateInfo(viewModel: FullItemViewModel)
}