package com.sweethome.data

import com.sweethome.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryCartRepository : CartRepository {

    private val mutableCartItems = MutableStateFlow<Map<String, Int>>(emptyMap())

    override val cartItems: StateFlow<Map<String, Int>> = mutableCartItems.asStateFlow()

    override fun addItem(id: String) {
        mutableCartItems.update { items ->
            items + (id to ((items[id] ?: 0) + 1))
        }
    }

    override fun removeItem(id: String) {
        mutableCartItems.update { items -> items - id }
    }

    override fun itemsCount(): Int = mutableCartItems.value.values.sum()
}
