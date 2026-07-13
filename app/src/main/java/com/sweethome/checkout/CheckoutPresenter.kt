package com.sweethome.checkout

import com.sweethome.R
import com.sweethome.RootRouter
import com.sweethome.base.MockPresenter
import com.sweethome.base.selector.SelectorItemModel
import com.sweethome.data.CartRepository
import com.sweethome.data.CatalogRepository
import com.sweethome.checkout.address.AddressInteractor
import com.sweethome.checkout.delivery.DeliveryInteractor
import com.sweethome.checkout.delivery.DeliveryViewModel
import com.sweethome.checkout.payment.PaymentInteractor

class CheckoutPresenter(
    rootRouter: RootRouter,
    catalogRepository: CatalogRepository,
    val cartRepository: CartRepository
): MockPresenter<CheckoutMvpView>(catalogRepository) {

    private lateinit var itemsPrice: String
    private val paymentInteractor = PaymentInteractor()
    private val addreInteractor = AddressInteractor()
    private val deliveryInteractor = DeliveryInteractor()


    override fun attach(mvpView: CheckoutMvpView) {
        super.attach(mvpView)
        updateAddresses()
        updateDeliveryTypes()
        upatePaymentMethods()
        updateFooter()
    }

    private fun updateFooter() {
        mvpView?.updateFooter(
            "₽" + itemsPrice,
            "₽" + deliveryInteractor.selectedTypePrice(),
            "₽" + (itemsPrice.toInt() + deliveryInteractor.selectedTypePrice().toInt())
        )
    }

    private fun updateAddresses() {
        mvpView?.updateShipmentAddresses(
            addreInteractor.addresses.map {
                SelectorItemModel(
                    it.id,
                    it.icon,
                    it.address,
                    it.additionalInfo
                )
            } as ArrayList<SelectorItemModel>
        )
    }

    private fun updateDeliveryTypes() {
        mvpView?.updateDeliveryTypes(
            deliveryInteractor.deliveryTypes.map {
                DeliveryViewModel(
                    it.id,
                    it.name,
                    it.deliveryTimeFrom,
                    it.deliveryTimeTo,
                    it.currency + it.price,
                    it.id == deliveryInteractor.selectedType
                )
            }.toList() as ArrayList<DeliveryViewModel>
        )

    }

    private fun upatePaymentMethods() {
        mvpView?.updatePaymentTypes(
            paymentInteractor.getPaymentMethods()
                .map {
                    SelectorItemModel(
                        it.id,
                        it.icon,
                        it.number,
                        checked = it.id == paymentInteractor.selectedPayment
                    )
                }.toList() as ArrayList<SelectorItemModel>
        )
    }

    fun onAddressChanged(
        item: SelectorItemModel,
        checked: Boolean
    ) {
        addreInteractor.selectedAddress = item.id
        updateAddresses()
    }

    fun onPaymentChanged(
        item: SelectorItemModel,
        checked: Boolean
    ) {
        paymentInteractor.selectedPayment = item.id
        upatePaymentMethods()
    }

    fun onConfirm() {
        if (paymentInteractor.selectedPaymentMethodIsValid()) {
            //mvpView.onSuccess()
        } else {
            //mvpView.onError()
        }
    }

    fun init(fullPrice: String) {
        this.itemsPrice = fullPrice

    }

    fun onDeliveryTypeSelected(id: String) {
        deliveryInteractor.selectedType = id
        updateDeliveryTypes()
        updateFooter()
    }
}
