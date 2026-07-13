package com.sweethome.checkout.payment

data class PaymentViewModel(
    val id: String,
    val icon: Int,
    val number: String
)

data class AddressViewModel(
    val id: String,
    val icon: Int,
    val address: String,
    val addressAdditional: String
)