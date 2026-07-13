package com.sweethome.checkout.payment

import com.sweethome.R
import java.util.*

class PaymentInteractor {

    var selectedPayment: String? = invalidPaymentMethod.id

    private val paymentMethods: List<PaymentViewModel> = arrayListOf(
        invalidPaymentMethod,
        validPaymentMethod
    )

    fun getPaymentMethods(): List<PaymentViewModel> {
        return paymentMethods
    }

    fun selectedPaymentMethodIsValid(): Boolean {
        return selectedPayment == validPaymentMethod.id
    }

    companion object {
        const val VALID_CREDIT_CARD = "Visa •••• 7777"
        const val INVALID_CREDIT_CARD = "Visa •••• 3322"

        val validPaymentMethod = PaymentViewModel(
            UUID.randomUUID().toString(),
            R.drawable.visa,
            INVALID_CREDIT_CARD
        )

        val invalidPaymentMethod = PaymentViewModel(
            UUID.randomUUID().toString(),
            R.drawable.visa,
            VALID_CREDIT_CARD
        )
    }
}