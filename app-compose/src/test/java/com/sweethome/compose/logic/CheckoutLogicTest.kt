package com.sweethome.compose.logic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckoutLogicTest {
    private val logic = CheckoutLogic("100")

    @Test
    fun initialState_matchesLegacyCheckout() {
        val snapshot = logic.snapshot()

        assertTrue(snapshot.addresses.none { it.checked })
        assertEquals(listOf(true, false), snapshot.paymentMethods.map { it.checked })
        assertEquals(listOf(true, false, false), snapshot.deliveryTypes.map { it.chosen })
        assertEquals("₽100", snapshot.subtotal)
        assertEquals("₽255", snapshot.shipment)
        assertEquals("₽355", snapshot.total)
        assertFalse(logic.confirm())
    }

    @Test
    fun addressSelection_isStoredButNeverRenderedLikeLegacyPresenter() {
        logic.selectAddress(logic.snapshot().addresses.last().id)

        assertTrue(logic.snapshot().addresses.none { it.checked })
    }

    @Test
    fun deliverySelection_recalculatesShipmentAndTotal() {
        logic.selectDelivery(logic.snapshot().deliveryTypes[1].id)

        val snapshot = logic.snapshot()
        assertEquals("₽305", snapshot.shipment)
        assertEquals("₽405", snapshot.total)
    }

    @Test
    fun secondDisplayedCard_isTheInternallyValidPaymentMethod() {
        logic.selectPayment(logic.snapshot().paymentMethods[1].id)

        assertTrue(logic.confirm())
    }
}
