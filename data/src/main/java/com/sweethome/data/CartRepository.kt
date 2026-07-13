package com.sweethome.data

interface CartRepository {
    val cartItems: Map<String, Int>

    fun addItem(id: String)

    fun removeItem(id: String)

    fun itemsCount(): Int
}
