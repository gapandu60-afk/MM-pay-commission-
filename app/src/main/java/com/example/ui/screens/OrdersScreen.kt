package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CurrencyType
import com.example.data.model.OrderStatus
import com.example.data.model.P2POrder
import com.example.ui.components.CurrencyToggle
import com.example.ui.components.StatusBadge
import com.example.ui.components.copyToClipboard
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextLightGray

@Composable
fun OrdersScreen(
    orders: List<P2POrder>,
    selectedCurrency: CurrencyType,
    selectedFilter: OrderStatus?,
    onSelectCurrency: (CurrencyType) -> Unit,
    onSelectFilter: (OrderStatus?) -> Unit,
    onOpenOrderDetail: (P2POrder) -> Unit,
    onToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val filteredOrders = orders.filter { order ->
        val currencyMatch = order.currency == selectedCurrency
        val filterMatch = selectedFilter == null || order.status == selectedFilter
        currencyMatch && filterMatch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Purchase Records",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        // Currency Toggle (INR | USDT)
        CurrencyToggle(
            selectedCurrency = selectedCurrency,
            onCurrencySelected = onSelectCurrency
        )

        // Status Tabs (All, Paying, Checking, Success, Failed)
        val statusTabs = listOf(
            Pair("All", null),
            Pair("Paying", OrderStatus.PAYING),
            Pair("Checking", OrderStatus.CHECKING),
            Pair("Success", OrderStatus.SUCCESS),
            Pair("Failed", OrderStatus.FAILED)
        )

        val tabScroll = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(tabScroll)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            statusTabs.forEach { (label, status) ->
                val isSelected = selectedFilter == status
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onSelectFilter(status) }
                        .padding(vertical = 4.dp)
                        .testTag("orders_tab_${label.lowercase()}")
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) PurplePrimary else TextGray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(PurplePrimary)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                }
            }
        }

        HorizontalDivider(color = Color(0xFFF3F4F6))

        // Orders List
        if (filteredOrders.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = TextLightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No records found",
                        fontSize = 15.sp,
                        color = TextGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("purchase_records_list"),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredOrders, key = { it.orderNo }) { order ->
                    OrderRecordCard(
                        order = order,
                        onDetails = { onOpenOrderDetail(order) },
                        onCopyOrderNo = {
                            copyToClipboard(context, order.orderNo, "Order Number")
                            onToast("Order number copied!")
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun OrderRecordCard(
    order: P2POrder,
    onDetails: () -> Unit,
    onCopyOrderNo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("order_card_${order.orderNo}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Amount and Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF7C3AED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CurrencyRupee,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${order.amount.toInt()} ${order.currency.name.lowercase()}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                StatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Order Number Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order No.: ",
                    fontSize = 12.sp,
                    color = TextGray
                )
                Text(
                    text = order.orderNo,
                    fontSize = 12.sp,
                    color = TextDark,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy Order No",
                    tint = TextGray,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onCopyOrderNo() }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Receive Row & Details Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Receive: ${order.amount.toInt()}",
                        fontSize = 13.sp,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Order Time: ${order.orderTime}",
                        fontSize = 12.sp,
                        color = TextLightGray
                    )
                }

                Button(
                    onClick = onDetails,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("details_button_${order.orderNo}")
                ) {
                    Text(
                        text = "Details",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
