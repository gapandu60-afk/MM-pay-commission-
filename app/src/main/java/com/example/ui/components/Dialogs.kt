package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurpleDark
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray

@Composable
fun DailyRewardDialog(
    onDismiss: () -> Unit,
    onClaim: () -> Unit,
    alreadyClaimed: Boolean
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("daily_reward_dialog")
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFFFBBF24), Color(0xFFF59E0B)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "Reward",
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "HOT Daily Rewards",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (alreadyClaimed) "You have already collected today's daily reward! Come back tomorrow for more."
                    else "Check in today to earn instant ₹50 cash bonus added directly to your trading balance.",
                    fontSize = 14.sp,
                    color = TextGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (!alreadyClaimed) onClaim() else onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("claim_daily_reward_button")
                ) {
                    Text(
                        text = if (alreadyClaimed) "Claimed (Done)" else "Claim ₹50 Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun NewbieRewardDialog(
    onDismiss: () -> Unit,
    onClaim: () -> Unit,
    alreadyClaimed: Boolean
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("newbie_reward_dialog")
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF34D399), Color(0xFF059669)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Newbie Reward",
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Newbie Starter Rewards",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Welcome to MM Pay! Complete your starter KYC & bind 1 UPI to unlock ₹100 Welcome Bonus!",
                    fontSize = 14.sp,
                    color = TextGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (!alreadyClaimed) onClaim() else onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (alreadyClaimed) Color.Gray else GreenIncome
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("claim_newbie_button")
                ) {
                    Text(
                        text = if (alreadyClaimed) "Already Claimed (₹100)" else "Claim ₹100 Starter Reward",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AddWalletDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, upiId: String, iconKey: String) -> Unit
) {
    var selectedWallet by remember { mutableStateOf("Paytm Business") }
    var upiIdInput by remember { mutableStateOf("") }
    var accountHolder by remember { mutableStateOf("Merchant Account") }

    val walletOptions = listOf(
        Pair("Paytm Business", "paytm"),
        Pair("PhonePe", "phonepe"),
        Pair("Amazon Pay", "amazon"),
        Pair("Mobikwik", "mobikwik"),
        Pair("Google Pay", "gpay"),
        Pair("Freecharge", "freecharge"),
        Pair("Bank Account (IMPS)", "bank")
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(12.dp).testTag("add_wallet_dialog")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bind UPI / Wallet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextGray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Select Payment Provider",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(walletOptions[0], walletOptions[1], walletOptions[2]).forEach { (name, icon) ->
                        val isSelected = selectedWallet == name
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PurplePrimary else Color(0xFFF3F4F6))
                                .clickable { selectedWallet = name }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.split(" ").first(),
                                color = if (isSelected) Color.White else TextDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(walletOptions[3], walletOptions[4], walletOptions[6]).forEach { (name, icon) ->
                        val isSelected = selectedWallet == name
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PurplePrimary else Color(0xFFF3F4F6))
                                .clickable { selectedWallet = name }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.split(" ").first(),
                                color = if (isSelected) Color.White else TextDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = upiIdInput,
                    onValueChange = { upiIdInput = it },
                    label = { Text("UPI ID / Account (e.g. name@icici)") },
                    placeholder = { Text("6304754340@paytm") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_upi_id")
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = accountHolder,
                    onValueChange = { accountHolder = it },
                    label = { Text("Account Holder Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_holder_name")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val finalUpi = if (upiIdInput.isBlank()) "6304754340@ybl" else upiIdInput
                        val icon = walletOptions.find { it.first == selectedWallet }?.second ?: "paytm"
                        onAdd(selectedWallet, finalUpi, icon)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_wallet_button")
                ) {
                    Text("Bind & Activate Wallet", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CustomBuyDialog(
    onDismiss: () -> Unit,
    onCreate: (amount: Double, channel: String) -> Unit
) {
    var amountText by remember { mutableStateOf("1000") }
    var selectedChannel by remember { mutableStateOf("Bank") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(12.dp).testTag("custom_buy_dialog")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Quick Match Buy Order",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Enter any amount to instantly match an escrow liquidity order.",
                    fontSize = 13.sp,
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Order Amount (INR)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    prefix = { Text("₹ ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("custom_buy_amount_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("500", "964", "1500", "3000").forEach { preset ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (amountText == preset) PurpleBg else Color(0xFFF3F4F6))
                                .clickable { amountText = preset }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "₹$preset",
                                color = if (amountText == preset) PurplePrimary else TextDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Payment Method",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Bank", "Paytm", "PhonePe").forEach { ch ->
                        val sel = selectedChannel == ch
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (sel) PurplePrimary else Color(0xFFF3F4F6))
                                .clickable { selectedChannel = ch }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ch,
                                color = if (sel) Color.White else TextDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val parsed = amountText.toDoubleOrNull() ?: 1000.0
                        onCreate(parsed, selectedChannel)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("confirm_custom_buy_button")
                ) {
                    Text("Match & Buy Now", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun LanguageDialog(
    currentLanguage: String,
    onSelectLanguage: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf("English", "हिन्दी (Hindi)", "తెలుగు (Telugu)", "தமிழ் (Tamil)", "मराठी (Marathi)", "বাংলা (Bengali)", "ಕನ್ನಡ (Kannada)")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("language_dialog")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Select Language",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                languages.forEach { lang ->
                    val isSelected = currentLanguage == lang.split(" ").first()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectLanguage(lang.split(" ").first()) }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = lang,
                            color = if (isSelected) PurplePrimary else TextDark,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 15.sp
                        )
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = "Selected", tint = PurplePrimary)
                        }
                    }
                }
            }
        }
    }
}
