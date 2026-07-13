package com.sweethome.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sweethome.R
import com.sweethome.base.BaseFragment
import com.sweethome.base.selector.CheckedChangeListener
import com.sweethome.base.selector.HorizontalSelector
import com.sweethome.base.selector.SelectorItemModel
import com.sweethome.base.viewModelFactory
import com.sweethome.checkout.delivery.DeliveryItemView
import com.sweethome.checkout.delivery.OnChosenListener
import com.sweethome.presentation.checkout.CheckoutIcon
import com.sweethome.presentation.checkout.CheckoutSelectorItem
import com.sweethome.presentation.checkout.CheckoutUiState
import com.sweethome.presentation.checkout.CheckoutViewModel
import com.sweethome.presentation.checkout.DeliveryUiModel
import kotlinx.coroutines.launch

class CheckoutFragment : BaseFragment() {

    private lateinit var fullPrice: String
    lateinit var shipmentAddressesSelector: HorizontalSelector
    lateinit var paymentTypesSelector: HorizontalSelector
    lateinit var confirmButton: View
    lateinit var itemsPrice: TextView
    lateinit var shipmentPrice: TextView
    lateinit var deliveryTypesContainer: LinearLayout
    lateinit var totalPrice: TextView
    private val viewModel: CheckoutViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory(CheckoutViewModel::class.java) {
                CheckoutViewModel(fullPrice)
            }
        )[CheckoutViewModel::class.java]
    }

    private val addressChangeListener = object : CheckedChangeListener {

        override fun onCheckedChange(item: SelectorItemModel, checked: Boolean) {
            viewModel.selectAddress(item.id)
        }
    }

    private val paymentChangeListener = object : CheckedChangeListener {
        override fun onCheckedChange(item: SelectorItemModel, checked: Boolean) {
            viewModel.selectPayment(item.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fullPrice = arguments?.getString(FULL_PRICE_ARG) ?: "0"
    }

    override fun onViewInflated(view: View) {
        super.onViewInflated(view)
        shipmentAddressesSelector = view.findViewById(R.id.shipment_address_selector)
        paymentTypesSelector = view.findViewById(R.id.payment_types)
        confirmButton = view.findViewById(R.id.confirm_button)
        itemsPrice = view.findViewById(R.id.subtotal)
        shipmentPrice = view.findViewById(R.id.shipment_price)
        totalPrice = view.findViewById(R.id.total_price)
        deliveryTypesContainer = view.findViewById(R.id.delivery_types_container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shipmentAddressesSelector.setCheckedChangeListener(addressChangeListener)
        paymentTypesSelector.setCheckedChangeListener(paymentChangeListener)
        confirmButton.setOnClickListener {
            viewModel.confirm()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: CheckoutUiState) {
        shipmentAddressesSelector.updateValues(state.shipmentAddresses.toSelectorItems())
        paymentTypesSelector.updateValues(state.paymentTypes.toSelectorItems())
        itemsPrice.text = getString(R.string.subtotal_price, state.subtotalPrice)
        shipmentPrice.text = getString(R.string.shipment_price, state.shipmentPrice)
        totalPrice.text = state.totalPrice
        updateDeliveryView(state.deliveryTypes)
    }

    private fun List<CheckoutSelectorItem>.toSelectorItems(): ArrayList<SelectorItemModel> =
        mapTo(arrayListOf()) { item ->
            SelectorItemModel(
                id = item.id,
                imageId = when (item.icon) {
                    CheckoutIcon.ADDRESS -> R.drawable.address_icon
                    CheckoutIcon.VISA -> R.drawable.visa
                },
                title = item.title,
                subtitle = item.subtitle,
                checked = item.checked
            )
        }

    override fun layoutId(): Int {
        return R.layout.checkout_layout
    }

    override fun title(): String {
        return getString(R.string.checkout_title)
    }

    private fun updateDeliveryView(deliveryTypes: List<DeliveryUiModel>) {
        if (deliveryTypesContainer.childCount != deliveryTypes.size) {
            deliveryTypesContainer.removeAllViews()
            fillDeliveryViews(deliveryTypes)
        }

        for ((i, itemView) in deliveryTypesContainer.children.withIndex()) {
            (itemView as DeliveryItemView).update(deliveryTypes[i])
        }
    }

    private fun fillDeliveryViews(deliveryTypes: List<DeliveryUiModel>) {
        for (item in deliveryTypes) {
            val itemView = context?.let { DeliveryItemView(it) }
            itemView?.update(item)
            itemView?.onChosenListener = object : OnChosenListener {
                override fun onItemChosen(id: String) {
                    viewModel.selectDelivery(id)
                }
            }
            deliveryTypesContainer.addView(itemView)
        }
    }

    companion object {
        const val FULL_PRICE_ARG = "full_price"

        fun newInstance(fullPrice: String): CheckoutFragment {
            val fragment = CheckoutFragment()
            val bundle = Bundle()
            bundle.putString(FULL_PRICE_ARG, fullPrice)
            fragment.arguments = bundle
            return fragment
        }
    }
}
