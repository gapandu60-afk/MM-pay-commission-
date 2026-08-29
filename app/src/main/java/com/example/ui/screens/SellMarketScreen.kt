package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.WalletAccount
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextLightGray

@Composable
fun SellMarketScreen(
    userProfile: UserProfile?,
    wallets: List<WalletAccount>,
    onBack: () -> Unit,
    onNavigateWallets: () -> Unit,
    onToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isAutoMatchingActive by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize().background(Color.White),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("sell_screen_back")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextDark
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Sell & UPI Matching",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            // Auto-matching Status Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (isAutoMatchingActive) Color(0xFFF3E8FF) else Color(0xFFF3F4F6)),
                modifier = Modifier.fillMaxWidth().testTag("sell_auto_match_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = PurplePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAutoMatchingActive) "Auto-Dispatch Active" else "Auto-Dispatch Paused",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isAutoMatchingActive) "Your linked UPIs are receiving automated sell requests in real-time."
                            else "Toggle on to start receiving incoming buyer deposits.",
                            fontSize = 12.sp,
                            color = TextGray
                        )
                    }

                    Switch(
                        checked = isAutoMatchingActive,
                        onCheckedChange = {
                            isAutoMatchingActive = it
                            onToast(if (it) "Auto-Dispatch activated! Listening for incoming orders." else "Auto-Dispatch paused.")
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = PurplePrimary, checkedTrackColor = Color(0xFFD8B4FE)),
                        modifier = Modifier.testTag("sell_auto_match_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active Sell UPI summary
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Linked Sell Accounts (${wallets.count { it.isSellActive }}/${wallets.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "Manage >",
                            fontSize = 13.sp,
                            color = PurplePrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { onNavigateWallets() }
                                .testTag("manage_sell_wallets_btn")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    wallets.filter { it.isSellActive }.take(3).forEach { wallet ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = wallet.name, fontSize = 13.sp, color = TextDark, fontWeight = FontWeight.Medium)
                            Text(text = "Active & Ready", fontSize = 12.sp, color = GreenIncome, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Recent Sell Transactions",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F2F6)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Order #20260818789012", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Text(text = "₹ 1,500.00", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Channel: Paytm Business", fontSize = 12.sp, color = TextGray)
                        Text(text = "Settled", fontSize = 12.sp, color = GreenSuccess, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
