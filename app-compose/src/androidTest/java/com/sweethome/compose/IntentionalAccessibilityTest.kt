package com.sweethome.compose

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.sweethome.compose.logic.CatalogSection
import com.sweethome.compose.ui.IntentionalA11yTags
import com.sweethome.compose.ui.catalog.CatalogScreen
import com.sweethome.compose.ui.checkout.CheckoutScreen
import com.sweethome.compose.ui.components.LegacyActionText
import com.sweethome.compose.ui.components.LegacyToolbar
import com.sweethome.compose.ui.components.LegacyTouchTargets
import com.sweethome.item.FullItemViewModel
import org.junit.Rule
import org.junit.Test

class IntentionalAccessibilityTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val noContentDescription =
        SemanticsMatcher.keyNotDefined(SemanticsProperties.ContentDescription)

    @Test
    fun toolbarIcons_remainUnlabelledAndUndersized() {
        composeRule.setContent {
            LegacyTouchTargets {
                LegacyToolbar(
                    title = "Title",
                    cartItemsCount = 2,
                    showCart = true,
                    onBack = {},
                    onCartClick = {}
                )
            }
        }

        composeRule.onNodeWithTag(IntentionalA11yTags.TOOLBAR_BACK, useUnmergedTree = true)
            .assertHasClickAction()
            .assert(noContentDescription)
            .assertWidthIsEqualTo(20.dp)
            .assertHeightIsEqualTo(20.dp)
        composeRule.onNodeWithTag(IntentionalA11yTags.TOOLBAR_CART, useUnmergedTree = true)
            .assertHasClickAction()
            .assert(noContentDescription)
            .assertWidthIsEqualTo(20.dp)
            .assertHeightIsEqualTo(20.dp)
        composeRule.onNodeWithTag(IntentionalA11yTags.CART_BADGE, useUnmergedTree = true)
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.LiveRegion))
    }

    @Test
    fun actionText_remainsClickableWithoutButtonRole() {
        composeRule.setContent {
            LegacyActionText(text = "Checkout", onClick = {})
        }

        composeRule.onNodeWithTag(IntentionalA11yTags.ACTION_TEXT, useUnmergedTree = true)
            .assertHasClickAction()
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Role))
    }

    @Test
    fun catalogImageHasNoDescriptionAndSectionTitleIsNotAHeading() {
        composeRule.setContent {
            CatalogScreen(
                sections = listOf(CatalogSection("Section", listOf(product()))),
                cartItemsCount = 0,
                onBack = {},
                onCartClick = {},
                onProductClick = {}
            )
        }

        composeRule.onNodeWithTag(IntentionalA11yTags.PRODUCT_IMAGE, useUnmergedTree = true)
            .assert(noContentDescription)
        composeRule.onNodeWithText("Section", useUnmergedTree = true)
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Heading))
    }

    @Test
    fun checkoutSelectorsKeepSeparateUnlabelledControls() {
        composeRule.setContent {
            LegacyTouchTargets {
                CheckoutScreen(fullPrice = "100", onBack = {})
            }
        }

        composeRule.onAllNodesWithTag(
            IntentionalA11yTags.SELECTOR_CHECKBOX,
            useUnmergedTree = true
        ).assertAll(noContentDescription)
        composeRule.onAllNodesWithTag(
            IntentionalA11yTags.DELIVERY_TOGGLE,
            useUnmergedTree = true
        )
            .assertAll(noContentDescription)
            .assertAll(hasClickAction())
    }

    private fun product() = FullItemViewModel(
        id = "id",
        category = "category",
        collection = "Collection",
        image = "placeholder",
        model = "Model",
        price = "100",
        currency = "₽",
        about = "About",
        country = "Country",
        manufacturer = "Manufacturer",
        designer = "Designer",
        color = 0,
        colorName = "Black",
        colors = arrayListOf(),
        discount = false
    )
}
