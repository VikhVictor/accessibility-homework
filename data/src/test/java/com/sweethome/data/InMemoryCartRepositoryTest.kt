package com.sweethome.data

import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryCartRepositoryTest {

    @Test
    fun addAndRemoveItems() {
        val repository = InMemoryCartRepository()

        repository.addItem("chair")
        repository.addItem("chair")
        repository.addItem("lamp")

        assertEquals(3, repository.itemsCount())
        assertEquals(2, repository.cartItems.value["chair"])

        repository.removeItem("chair")

        assertEquals(1, repository.itemsCount())
    }
}
