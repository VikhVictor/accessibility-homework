package com.sweethome.presentation.checkout

data class CheckoutUiState(
    val shipmentAddresses: List<CheckoutSelectorItem> = emptyList(),
    val paymentTypes: List<CheckoutSelectorItem> = emptyList(),
    val deliveryTypes: List<DeliveryUiModel> = emptyList(),
    val subtotalPrice: String = "₽0",
    val shipmentPrice: String = "₽0",
    val totalPrice: String = "₽0"
)

data class CheckoutSelectorItem(
    val id: String,
    val icon: CheckoutIcon,
    val title: String,
    val subtitle: String = "",
    val checked: Boolean = false
)

enum class CheckoutIcon {
    ADDRESS,
    VISA
}

data class DeliveryUiModel(
    val id: String,
    val name: String,
    val timeFrom: String,
    val timeTo: String,
    val price: String,
    val chosen: Boolean
)
