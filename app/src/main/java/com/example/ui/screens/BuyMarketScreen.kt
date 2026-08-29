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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CurrencyType
import com.example.data.model.P2POrder
import com.example.data.model.TierCategory
import com.example.ui.components.CurrencyToggle
import com.example.ui.components.IncomeRateBanner
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextLightGray
import com.example.ui.viewmodel.SortDirection

@Composable
fun BuyMarketScreen(
    orders: List<P2POrder>,
    selectedTier: TierCategory,
    selectedCurrency: CurrencyType,
    sortDirection: SortDirection,
    onBack: () -> Unit,
    onSelectTier: (TierCategory) -> Unit,
    onSelectCurrency: (CurrencyType) -> Unit,
    onToggleSort: () -> Unit,
    onBuyOrder: (P2POrder) -> Unit,
    onOpenCustomBuy: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bannerRateText = when (selectedTier) {
        TierCategory.ALL -> "2.9% + 6"
        TierCategory.MINI -> "2.8% + 5"
        TierCategory.SMALL -> "2.9% + 6"
        TierCategory.MEDIUM -> "3.0% + 8"
        TierCategory.LARGE -> "3.1% + 12"
        TierCategory.MAX -> "3.3% + 20"
    }

    Scaffold(
        modifier = modifier.fillMaxSize().background(Color.White),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenCustomBuy,
                containerColor = PurplePrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("custom_buy_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Custom Match")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Quick Match", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("buy_screen_back")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextDark
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Buy",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Currency Tabs (INR | USDT)
            CurrencyToggle(
                selectedCurrency = selectedCurrency,
                onCurrencySelected = onSelectCurrency
            )

            // Dynamic Purple Income Banner (Matching Screenshot 1)
            IncomeRateBanner(
                rateText = bannerRateText,
                subText = "Income per Order"
            )

            // Tier Category Filter Row (All, Mini, Small, Medium, Large, Max)
            val chipScrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(chipScrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TierCategory.entries.forEach { tier ->
                    val isSelected = tier == selectedTier
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onSelectTier(tier) }
                            .padding(vertical = 4.dp)
                            .testTag("tier_tab_${tier.name.lowercase()}")
                    ) {
                        Text(
                            text = when (tier) {
                                TierCategory.ALL -> "All"
                                TierCategory.MINI -> "Mini"
                                TierCategory.SMALL -> "Small"
                                TierCategory.MEDIUM -> "Medium"
                                TierCategory.LARGE -> "Large"
                                TierCategory.MAX -> "Max"
                            },
                            color = if (isSelected) PurplePrimary else TextGray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        if (tier != TierCategory.ALL) {
                            Text(
                                text = "${tier.ratePercent}%",
                                color = if (isSelected) PurplePrimary else GreenIncome,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
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

            // Sort dropdown bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onToggleSort() }
                    .testTag("sort_filter_bar"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (sortDirection == SortDirection.HIGH_TO_LOW) "From High to Low" else "From Low to High",
                    color = TextDark,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (sortDirection == SortDirection.HIGH_TO_LOW) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                    contentDescription = "Sort Direction",
                    tint = TextDark
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                color = Color(0xFFF3F4F6)
            )

            // Order List (Matching Screenshot 1)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .testTag("buy_orders_list"),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders, key = { it.orderNo }) { order ->
                    BuyOrderRow(
                        order = order,
                        onBuy = { onBuyOrder(order) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun BuyOrderRow(
    order: P2POrder,
    onBuy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.Top) {
            // Black circle rupee icon (Screenshot 1)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CurrencyRupee,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${order.amount.toInt()} ${order.currency.name}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(GoldAccent)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = order.channel,
                        fontSize = 12.sp,
                        color = TextDark,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${String.format("%.2f", order.income)} ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "(${order.incomeRatePercent}%+${order.flatBonus.toInt()})",
                        fontSize = 12.sp,
                        color = GreenIncome,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "Income",
                    fontSize = 11.sp,
                    color = TextLightGray
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Button(
                onClick = onBuy,
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(86.dp)
                    .height(34.dp)
                    .testTag("buy_item_button_${order.amount.toInt()}")
            ) {
                Text(
                    text = "Buy",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "+ ${String.format("%.2f", order.balanceAfter)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = "Balance",
                fontSize = 11.sp,
                color = TextLightGray
            )
        }
    }
}
