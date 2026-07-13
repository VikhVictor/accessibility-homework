package com.sweethome.checkout.delivery

data class DeliveryType(
    val id: String,
    val name: String,
    val deliveryTimeFrom: String,
    val deliveryTimeTo: String,
    val price: String,
    val currency: String
)