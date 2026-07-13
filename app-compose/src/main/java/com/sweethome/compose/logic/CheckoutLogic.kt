package com.sweethome.compose.logic

import java.util.UUID

class CheckoutLogic(
    private val itemsPrice: String
) {
    private val addresses = listOf(
        SelectorOption(
            id = UUID.randomUUID().toString(),
            icon = SelectorIcon.ADDRESS,
            title = "Ангарская улица, 26к4",
            subtitle = "Москва, Россия, 125412"
        ),
        SelectorOption(
            id = UUID.randomUUID().toString(),
            icon = SelectorIcon.ADDRESS,
            title = "Чистопрудный бульвар, 6/19с1",
            subtitle = "Москва, Россия, 101000"
        )
    )

    private val validPaymentMethod = SelectorOption(
        id = UUID.randomUUID().toString(),
        icon = SelectorIcon.VISA,
        title = INVALID_CREDIT_CARD
    )
    private val invalidPaymentMethod = SelectorOption(
        id = UUID.randomUUID().toString(),
        icon = SelectorIcon.VISA,
        title = VALID_CREDIT_CARD
    )
    private val paymentMethods = listOf(invalidPaymentMethod, validPaymentMethod)

    private val deliveryTypes = listOf(
        DeliveryOption(UUID.randomUUID().toString(), "DHL Express", "2", "3", "255", "₽"),
        DeliveryOption(UUID.randomUUID().toString(), "Post", "2", "4", "305", "₽"),
        DeliveryOption(UUID.randomUUID().toString(), "Global delivery", "1", "2", "450", "₽")
    )

    private var selectedAddress = addresses.first().id
    private var selectedPayment = invalidPaymentMethod.id
    private var selectedDelivery = deliveryTypes.first().id

    fun selectAddress(id: String) {
        selectedAddress = id
    }

    fun selectPayment(id: String) {
        selectedPayment = id
    }

    fun selectDelivery(id: String) {
        selectedDelivery = id
    }

    fun confirm(): Boolean = selectedPayment == validPaymentMethod.id

    fun snapshot(): CheckoutSnapshot {
        val deliveryPrice = deliveryTypes.find { it.id == selectedDelivery }?.price ?: "0"
        return CheckoutSnapshot(
            // Intentionally mirrors CheckoutPresenter: address selection is stored, but the
            // checked state is never passed back to the UI.
            addresses = addresses,
            paymentMethods = paymentMethods.map { it.copy(checked = it.id == selectedPayment) },
            deliveryTypes = deliveryTypes.map { it.copy(chosen = it.id == selectedDelivery) },
            subtotal = "₽$itemsPrice",
            shipment = "₽$deliveryPrice",
            total = "₽${itemsPrice.toInt() + deliveryPrice.toInt()}"
        )
    }

    private companion object {
        const val VALID_CREDIT_CARD = "Visa •••• 7777"
        const val INVALID_CREDIT_CARD = "Visa •••• 3322"
    }
}

data class CheckoutSnapshot(
    val addresses: List<SelectorOption>,
    val paymentMethods: List<SelectorOption>,
    val deliveryTypes: List<DeliveryOption>,
    val subtotal: String,
    val shipment: String,
    val total: String
)

data class SelectorOption(
    val id: String,
    val icon: SelectorIcon,
    val title: String,
    val subtitle: String = "",
    val checked: Boolean = false
)

enum class SelectorIcon {
    ADDRESS,
    VISA
}

data class DeliveryOption(
    val id: String,
    val name: String,
    val deliveryTimeFrom: String,
    val deliveryTimeTo: String,
    val price: String,
    val currency: String,
    val chosen: Boolean = false
)
