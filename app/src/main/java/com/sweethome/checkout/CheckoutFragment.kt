package com.sweethome.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.sweethome.R
import com.sweethome.base.BaseFragment
import com.sweethome.base.MvpView
import com.sweethome.base.selector.CheckedChangeListener
import com.sweethome.base.selector.HorizontalSelector
import com.sweethome.base.selector.SelectorItemModel
import com.sweethome.checkout.delivery.DeliveryItemView
import com.sweethome.checkout.delivery.DeliveryType
import com.sweethome.checkout.delivery.DeliveryViewModel
import com.sweethome.checkout.delivery.OnChosenListener
import kotlin.collections.ArrayList

class CheckoutFragment : BaseFragment<CheckoutPresenter, CheckoutMvpView>() {

    private lateinit var fullPrice: String
    lateinit var shipmentAddressesSelector: HorizontalSelector
    lateinit var paymentTypesSelector: HorizontalSelector
    lateinit var confirmButton: View
    lateinit var itemsPrice: TextView
    lateinit var shipmentPrice: TextView
    lateinit var deliveryTypesContainer: LinearLayout
    lateinit var totalPrice: TextView

    init {
        mvpView = object : CheckoutMvpView {
            override fun updateShipmentAddresses(shipmentAddresses: ArrayList<SelectorItemModel>) {
                shipmentAddressesSelector.updateValues(shipmentAddresses)
            }

            override fun updatePaymentTypes(shipmentAddresses: ArrayList<SelectorItemModel>) {
                paymentTypesSelector.updateValues(shipmentAddresses)
            }

            override fun updateFooter(
                subtotalPriceText: String,
                shipmentPriceText: String,
                totalPriceText: String
            ) {
                itemsPrice.text = getString(R.string.subtotal_price, subtotalPriceText)
                shipmentPrice.text = getString(R.string.shipment_price,  shipmentPriceText)
                totalPrice.text = totalPriceText
            }

            override fun updateDeliveryTypes(deliveryTypes: ArrayList<DeliveryViewModel>) {
                updateDeliveryView(deliveryTypes)
            }
        }
    }

    private val addressChangeListener = object : CheckedChangeListener {

        override fun onCheckedChange(item: SelectorItemModel, checked: Boolean) {
            presenter.onAddressChanged(item, checked)
        }
    }

    private val paymentChangeListener = object : CheckedChangeListener {
        override fun onCheckedChange(item: SelectorItemModel, checked: Boolean) {
            presenter.onPaymentChanged(item, checked)
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
        presenter.init(fullPrice)
        super.onViewCreated(view, savedInstanceState)
        shipmentAddressesSelector.setCheckedChangeListener(addressChangeListener)
        paymentTypesSelector.setCheckedChangeListener(paymentChangeListener)
        confirmButton.setOnClickListener {
            presenter.onConfirm()
        }

    }

    override fun layoutId(): Int {
        return R.layout.checkout_layout
    }

    override fun title(): String {
        return getString(R.string.checkout_title)
    }

    private fun updateDeliveryView(deliveryTypes: ArrayList<DeliveryViewModel>) {
        if (deliveryTypesContainer.childCount != deliveryTypes.size) {
            deliveryTypesContainer.removeAllViews()
            fillDeliveryViews(deliveryTypes)
        }

        for ((i, itemView) in deliveryTypesContainer.children.withIndex()) {
            (itemView as DeliveryItemView).update(deliveryTypes[i])
        }
    }

    private fun fillDeliveryViews(deliveryTypes: ArrayList<DeliveryViewModel>) {
        for (item in deliveryTypes) {
            val itemView = context?.let { DeliveryItemView(it) }
            itemView?.update(item)
            itemView?.onChosenListener = object : OnChosenListener {
                override fun onItemChosen(id: String) {
                    presenter.onDeliveryTypeSelected(id)
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

interface CheckoutMvpView: MvpView {
    fun updateShipmentAddresses(shipmentAddresses: ArrayList<SelectorItemModel>)
    fun updatePaymentTypes(shipmentAddresses: ArrayList<SelectorItemModel>)
    fun updateFooter(
        subtotalPriceText: String,
        shipmentPriceText: String,
        totalPriceText: String
    )
    fun updateDeliveryTypes(deliveryTypes: ArrayList<DeliveryViewModel>)
}