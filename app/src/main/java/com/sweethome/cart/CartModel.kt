package com.sweethome.cart

import com.sweethome.item.FullItemViewModel

data class CartModel(
    val items: List<FullItemViewModel>,
    val shipment: String,
    val itemsCount: String,
    val price: String
) {

}