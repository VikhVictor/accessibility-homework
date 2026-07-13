package com.sweethome.checkout.delivery

import java.util.*

class DeliveryInteractor {
    fun selectedTypePrice(): String {
        return deliveryTypes.find { it.id == selectedType }?.price ?: "0"
    }

    companion object {
        val DHL = DeliveryType(
            UUID.randomUUID().toString(),
            "DHL Express",
            "2",
            "3",
            "255",
            "₽"
        )

        val POST = DeliveryType(
            UUID.randomUUID().toString(),
            "Post",
            "2",
            "4",
            "305",
            "₽"
        )

        val GLOBAL = DeliveryType(
            UUID.randomUUID().toString(),
            "Global delivery",
            "1",
            "2",
            "450",
            "₽"
        )
    }

    val deliveryTypes = arrayListOf(
        DHL, POST, GLOBAL
    )

    var selectedType: String? = deliveryTypes[0].id


}