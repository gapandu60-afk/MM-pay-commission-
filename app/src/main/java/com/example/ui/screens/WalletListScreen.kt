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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WalletAccount
import com.example.ui.theme.PurpleDark
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextLightGray

@Composable
fun WalletListScreen(
    wallets: List<WalletAccount>,
    onBack: () -> Unit,
    onToggleWallet: (walletId: Long, isBuy: Boolean, isSell: Boolean) -> Unit,
    onOpenAddWallet: () -> Unit,
    onToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize().background(Color.White),
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 6.dp,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Button(
                    onClick = onOpenAddWallet,
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("add_new_wallet_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bind New UPI / Bank Account",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
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
            // Header Bar (Matching Screenshot 6)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("wallet_list_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextDark
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Wallet List",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Wallet Items List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("wallet_items_list"),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(wallets, key = { it.id }) { wallet ->
                    WalletItemCard(
                        wallet = wallet,
                        onToggle = { isBuy, isSell ->
                            onToggleWallet(wallet.id, isBuy, isSell)
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@Composable
fun WalletItemCard(
    wallet: WalletAccount,
    onToggle: (isBuy: Boolean, isSell: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F2F6)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("wallet_card_${wallet.name.lowercase().replace(" ", "_")}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Icon & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                WalletBrandIcon(iconKey = wallet.iconKey)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = wallet.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = wallet.upiId,
                        fontSize = 11.sp,
                        color = TextLightGray
                    )
                }
            }

            // Buy & Sell Buttons (Matching Screenshot 6)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Buy pill
                Button(
                    onClick = { onToggle(!wallet.isBuyActive, wallet.isSellActive) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (wallet.isBuyActive) PurplePrimary else Color(0xFFE5E7EB)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .width(68.dp)
                ) {
                    Text(
                        text = "Buy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (wallet.isBuyActive) Color.White else TextGray
                    )
                }

                // Sell pill
                Button(
                    onClick = { onToggle(wallet.isBuyActive, !wallet.isSellActive) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (wallet.isSellActive) PurplePrimary else Color(0xFFCBD5E1)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .width(68.dp)
                ) {
                    Text(
                        text = "Sell",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (wallet.isSellActive) Color.White else TextGray
                    )
                }
            }
        }
    }
}

@Composable
fun WalletBrandIcon(iconKey: String) {
    val (bg, textColor, symbol) = when {
        iconKey.contains("amazon") -> Triple(Color(0xFFFFF3E0), Color(0xFFFF9900), "a")
        iconKey.contains("paytm") -> Triple(Color(0xFFE0F2FE), Color(0xFF002970), "Paytm")
        iconKey.contains("phonepe") -> Triple(Color(0xFFEDE9FE), Color(0xFF5F259F), "पे")
        iconKey.contains("mobikwik") -> Triple(Color(0xFFDBEAFE), Color(0xFF1E40AF), "M")
        iconKey.contains("freecharge") -> Triple(Color(0xFFFEF3C7), Color(0xFFF97316), "⚡")
        else -> Triple(Color(0xFFF3F4F6), Color(0xFF4B5563), "UPI")
    }

    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = textColor,
            fontWeight = FontWeight.Black,
            fontSize = if (symbol.length > 2) 11.sp else 20.sp
        )
    }
}
