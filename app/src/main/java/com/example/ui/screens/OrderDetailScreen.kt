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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.data.model.P2POrder
import com.example.ui.components.StatusBadge
import com.example.ui.components.copyToClipboard
import com.example.ui.theme.BlueChecking
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangePending
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.RedFailed
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextLightGray
import kotlinx.coroutines.delay

@Composable
fun OrderDetailScreen(
    order: P2POrder?,
    onBack: () -> Unit,
    onSubmitUtr: (orderId: Long, utr: String) -> Unit,
    onCompleteOrder: (orderId: Long) -> Unit,
    onCancelOrder: (orderId: Long) -> Unit,
    onToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (order == null) return
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    var utrInput by remember { mutableStateOf(order.paymentRefNumber) }
    var secondsLeft by remember { mutableIntStateOf(890) }

    LaunchedEffect(order.status) {
        while (order.status == OrderStatus.PAYING && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val timerString = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        modifier = modifier.fillMaxSize().background(Color.White),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("order_detail_back")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextDark
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Order Details",
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
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Status Header Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (order.status) {
                        OrderStatus.SUCCESS -> Color(0xFFECFDF5)
                        OrderStatus.CHECKING -> Color(0xFFEFF6FF)
                        OrderStatus.PAYING -> Color(0xFFFFFBEB)
                        OrderStatus.FAILED -> Color(0xFFFEF2F2)
                    }
                ),
                modifier = Modifier.fillMaxWidth().testTag("order_status_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = when (order.status) {
                                OrderStatus.PAYING -> "Waiting for Payment"
                                OrderStatus.CHECKING -> "Verifying UTR Reference"
                                OrderStatus.SUCCESS -> "Order Completed & Settled"
                                OrderStatus.FAILED -> "Order Cancelled"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (order.status) {
                                OrderStatus.PAYING -> Color(0xFFD97706)
                                OrderStatus.CHECKING -> BlueChecking
                                OrderStatus.SUCCESS -> GreenSuccess
                                OrderStatus.FAILED -> RedFailed
                            }
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = when (order.status) {
                                OrderStatus.PAYING -> "Pay the merchant via UPI before timer expires"
                                OrderStatus.CHECKING -> "Bank verification in progress (< 2 mins)"
                                OrderStatus.SUCCESS -> "Commission added to your balance"
                                OrderStatus.FAILED -> "This order was cancelled"
                            },
                            fontSize = 12.sp,
                            color = TextGray
                        )
                    }

                    if (order.status == OrderStatus.PAYING) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = timerString,
                                color = Color(0xFFD97706),
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        StatusBadge(status = order.status)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Amount & Profit Breakdown Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Amount to Pay", fontSize = 14.sp, color = TextGray)
                        Text(
                            text = "₹ ${String.format("%.2f", order.amount)}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = TextDark
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Commission (${order.incomeRatePercent}% + ₹${order.flatBonus.toInt()})",
                            fontSize = 13.sp,
                            color = GreenIncome,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "+ ₹ ${String.format("%.2f", order.income)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenIncome
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFE5E7EB))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Balance Credited",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "₹ ${String.format("%.2f", order.balanceAfter)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = PurplePrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Merchant / Seller Details
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Merchant Payment Details",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // UPI ID Box
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF3F4F6),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Seller UPI ID", fontSize = 11.sp, color = TextGray)
                                Text(
                                    text = order.sellerUpi,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }
                            IconButton(
                                onClick = {
                                    copyToClipboard(context, order.sellerUpi, "UPI ID")
                                    onToast("UPI ID copied! Open Paytm/PhonePe/GPay to pay.")
                                },
                                modifier = Modifier.testTag("copy_seller_upi_btn")
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = PurplePrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Order No.", fontSize = 12.sp, color = TextGray)
                        Text(text = order.orderNo, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextDark)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Payment Channel", fontSize = 12.sp, color = TextGray)
                        Text(text = order.channel, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextDark)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Order Time", fontSize = 12.sp, color = TextGray)
                        Text(text = order.orderTime, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // UTR Submission Form (For Paying or Checking)
            if (order.status == OrderStatus.PAYING || order.status == OrderStatus.CHECKING) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Submit UTR / UPI Reference No.",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "After sending ₹${order.amount.toInt()} on UPI, paste the 12-digit UTR number from your payment receipt.",
                            fontSize = 12.sp,
                            color = TextGray
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = utrInput,
                            onValueChange = { utrInput = it },
                            placeholder = { Text("e.g. 423891002931") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    clipboardManager.getText()?.text?.let {
                                        utrInput = it
                                        onToast("Pasted from clipboard")
                                    }
                                }) {
                                    Icon(Icons.Default.ContentPaste, contentDescription = "Paste", tint = PurplePrimary)
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("utr_input_field")
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val utr = if (utrInput.isBlank()) "UPI/${System.currentTimeMillis()}" else utrInput
                                onSubmitUtr(order.id, utr)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("submit_utr_button")
                        ) {
                            Text(
                                text = if (order.status == OrderStatus.CHECKING) "Update UTR Number" else "I Have Paid (Submit UTR)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            if (order.status != OrderStatus.SUCCESS && order.status != OrderStatus.FAILED) {
                Button(
                    onClick = { onCompleteOrder(order.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenIncome),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("simulate_complete_order_button")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirm & Release Commission (Instant)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { onCancelOrder(order.id) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("cancel_order_button")
                ) {
                    Text(
                        text = "Cancel Order",
                        color = RedFailed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
