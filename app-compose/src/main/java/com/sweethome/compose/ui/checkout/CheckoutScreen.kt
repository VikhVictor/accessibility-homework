package com.sweethome.compose.ui.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sweethome.compose.R
import com.sweethome.compose.logic.CheckoutLogic
import com.sweethome.compose.logic.DeliveryOption
import com.sweethome.compose.logic.SelectorIcon
import com.sweethome.compose.logic.SelectorOption
import com.sweethome.compose.ui.IntentionalA11yTags
import com.sweethome.compose.ui.components.LegacyActionText
import com.sweethome.compose.ui.components.LegacyBlack60
import com.sweethome.compose.ui.components.LegacyGreen
import com.sweethome.compose.ui.components.LegacyToolbar

@Composable
fun CheckoutScreen(
    fullPrice: String,
    onBack: () -> Unit
) {
    val logic = remember(fullPrice) { CheckoutLogic(fullPrice) }
    var revision by remember { mutableIntStateOf(0) }
    val snapshot = revision.let { logic.snapshot() }

    Column(modifier = Modifier.fillMaxSize()) {
        LegacyToolbar(
            title = stringResource(R.string.checkout_title),
            cartItemsCount = 0,
            showCart = false,
            onBack = onBack,
            onCartClick = {}
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            CheckoutHeader(stringResource(R.string.shipment_address))
            SelectorRow(
                options = snapshot.addresses,
                onSelected = {
                    logic.selectAddress(it)
                    revision++
                }
            )
            CheckoutHeader(stringResource(R.string.delivery_type))
            snapshot.deliveryTypes.forEach { delivery ->
                DeliveryRow(
                    delivery = delivery,
                    onSelected = {
                        logic.selectDelivery(delivery.id)
                        revision++
                    }
                )
            }
            CheckoutHeader(stringResource(R.string.payment_type))
            SelectorRow(
                options = snapshot.paymentMethods,
                onSelected = {
                    logic.selectPayment(it)
                    revision++
                }
            )
        }
        CheckoutFooter(
            subtotal = snapshot.subtotal,
            shipment = snapshot.shipment,
            total = snapshot.total,
            onConfirm = { logic.confirm() }
        )
    }
}

@Composable
private fun CheckoutHeader(title: String) {
    Text(
        text = title,
        color = Color.Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 6.dp)
    )
}

@Composable
private fun SelectorRow(
    options: List<SelectorOption>,
    onSelected: (String) -> Unit
) {
    val itemWidth = (LocalConfiguration.current.screenWidthDp - 72).dp
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(options, key = { it.id }) { option ->
            SelectorCard(
                option = option,
                onSelected = { onSelected(option.id) },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(itemWidth)
            )
        }
    }
}

@Composable
private fun SelectorCard(
    option: SelectorOption,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(64.dp)
            .background(Color(0xE6F8F8F8), androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                when (option.icon) {
                    SelectorIcon.ADDRESS -> R.drawable.address_icon
                    SelectorIcon.VISA -> R.drawable.visa
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .padding(12.dp)
                .size(40.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = option.title, color = Color.Black, fontSize = 16.sp)
            if (option.subtitle.isNotEmpty()) {
                Text(text = option.subtitle, color = LegacyBlack60)
            }
        }
        Checkbox(
            checked = option.checked,
            onCheckedChange = { onSelected() },
            modifier = Modifier.testTag(IntentionalA11yTags.SELECTOR_CHECKBOX),
            colors = CheckboxDefaults.colors(checkedColor = LegacyGreen)
        )
    }
}

@Composable
private fun DeliveryRow(
    delivery: DeliveryOption,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = delivery.name, color = Color.Black, fontSize = 16.sp)
        Text(
            text = " " + stringResource(
                R.string.time_from_to,
                delivery.deliveryTimeFrom,
                delivery.deliveryTimeTo
            ),
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(text = delivery.currency + delivery.price)
        Image(
            painter = painterResource(
                if (delivery.chosen) {
                    R.drawable.ic_toggle_checked
                } else {
                    R.drawable.ic_toggle_unchecked
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(20.dp)
                .testTag(IntentionalA11yTags.DELIVERY_TOGGLE)
                .clickable(onClick = onSelected)
        )
    }
}

@Composable
private fun CheckoutFooter(
    subtotal: String,
    shipment: String,
    total: String,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(text = stringResource(R.string.subtotal_price, subtotal))
                Text(text = stringResource(R.string.shipment_price, shipment))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = stringResource(R.string.total_price_title), fontSize = 12.sp)
                Text(text = total, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LegacyActionText(text = stringResource(R.string.checkout_confirm), onClick = onConfirm)
    }
}
