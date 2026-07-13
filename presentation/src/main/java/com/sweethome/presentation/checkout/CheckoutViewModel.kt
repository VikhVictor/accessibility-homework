package com.sweethome.presentation.checkout

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CheckoutViewModel(
    private val itemsPrice: String
) : ViewModel() {

    private var selectedAddress = HOME_ID
    private var selectedPayment = INVALID_PAYMENT_ID
    private var selectedDelivery = DHL_ID

    private val mutableUiState = MutableStateFlow(buildUiState())
    val uiState: StateFlow<CheckoutUiState> = mutableUiState.asStateFlow()

    fun selectAddress(id: String) {
        selectedAddress = id
        mutableUiState.value = buildUiState()
    }

    fun selectPayment(id: String) {
        selectedPayment = id
        mutableUiState.value = buildUiState()
    }

    fun selectDelivery(id: String) {
        selectedDelivery = id
        mutableUiState.value = buildUiState()
    }

    fun confirm(): Boolean = selectedPayment == VALID_PAYMENT_ID

    private fun buildUiState(): CheckoutUiState {
        val selectedDeliveryPrice = deliveryTypes
            .find { it.id == selectedDelivery }
            ?.price
            ?: "0"
        return CheckoutUiState(
            shipmentAddresses = addresses.map {
                it.copy(checked = it.id == selectedAddress)
            },
            paymentTypes = paymentMethods.map {
                it.copy(checked = it.id == selectedPayment)
            },
            deliveryTypes = deliveryTypes.map {
                DeliveryUiModel(
                    id = it.id,
                    name = it.name,
                    timeFrom = it.timeFrom,
                    timeTo = it.timeTo,
                    price = it.currency + it.price,
                    chosen = it.id == selectedDelivery
                )
            },
            subtotalPrice = "₽$itemsPrice",
            shipmentPrice = "₽$selectedDeliveryPrice",
            totalPrice = "₽${itemsPrice.toInt() + selectedDeliveryPrice.toInt()}"
        )
    }

    private data class Delivery(
        val id: String,
        val name: String,
        val timeFrom: String,
        val timeTo: String,
        val price: String,
        val currency: String
    )

    private companion object {
        const val HOME_ID = "home"
        const val WORK_ID = "work"
        const val VALID_PAYMENT_ID = "visa_3322"
        const val INVALID_PAYMENT_ID = "visa_7777"
        const val DHL_ID = "dhl"
        const val POST_ID = "post"
        const val GLOBAL_ID = "global"

        val addresses = listOf(
            CheckoutSelectorItem(
                id = HOME_ID,
                icon = CheckoutIcon.ADDRESS,
                title = "Ангарская улица, 26к4",
                subtitle = "Москва, Россия, 125412"
            ),
            CheckoutSelectorItem(
                id = WORK_ID,
                icon = CheckoutIcon.ADDRESS,
                title = "Чистопрудный бульвар, 6/19с1",
                subtitle = "Москва, Россия, 101000"
            )
        )

        val paymentMethods = listOf(
            CheckoutSelectorItem(
                id = INVALID_PAYMENT_ID,
                icon = CheckoutIcon.VISA,
                title = "Visa •••• 7777"
            ),
            CheckoutSelectorItem(
                id = VALID_PAYMENT_ID,
                icon = CheckoutIcon.VISA,
                title = "Visa •••• 3322"
            )
        )

        val deliveryTypes = listOf(
            Delivery(DHL_ID, "DHL Express", "2", "3", "255", "₽"),
            Delivery(POST_ID, "Post", "2", "4", "305", "₽"),
            Delivery(GLOBAL_ID, "Global delivery", "1", "2", "450", "₽")
        )
    }
}
