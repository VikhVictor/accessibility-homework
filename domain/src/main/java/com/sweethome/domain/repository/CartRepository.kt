package com.sweethome.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cartItems: StateFlow<Map<String, Int>>

    fun addItem(id: String)

    fun removeItem(id: String)

    fun itemsCount(): Int
}
