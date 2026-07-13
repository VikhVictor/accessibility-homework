package com.sweethome.data

class InMemoryCartRepository : CartRepository {

    private val mutableCartItems = hashMapOf<String, Int>()

    override val cartItems: Map<String, Int>
        get() = mutableCartItems

    override fun addItem(id: String) {
        mutableCartItems[id] = (mutableCartItems[id] ?: 0) + 1
    }

    override fun removeItem(id: String) {
        mutableCartItems.remove(id)
    }

    override fun itemsCount(): Int = mutableCartItems.values.sum()
}
