package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.data.model.BalanceRecordItem
import com.example.data.model.UserProfile
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.RedFailed
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextLightGray

@Composable
fun BalanceRecordsScreen(
    userProfile: UserProfile?,
    records: List<BalanceRecordItem>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize().background(Color.White),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("balance_records_back")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextDark
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Balance Records",
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
        ) {
            // Balance Summary Banner
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E8FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Current Available Balance", fontSize = 12.sp, color = TextGray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "₹ ${String.format("%.2f", userProfile?.balance ?: 0.0)}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = PurplePrimary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = null,
                        tint = PurplePrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(vertical = 4.dp))

            // History List
            if (records.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No balance transactions recorded yet.", color = TextGray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .testTag("balance_records_list")
                ) {
                    items(records, key = { it.id }) { record ->
                        BalanceRecordRow(record = record)
                        HorizontalDivider(color = Color(0xFFF9FAFB))
                    }
                }
            }
        }
    }
}

@Composable
fun BalanceRecordRow(
    record: BalanceRecordItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (record.isPositive) Color(0xFFE6F9F0) else Color(0xFFFEE2E2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (record.isPositive) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = if (record.isPositive) GreenIncome else RedFailed,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = record.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = record.timeFormatted,
                    fontSize = 11.sp,
                    color = TextLightGray
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (record.isPositive) "+" else "-"} ₹${String.format("%.2f", record.amount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (record.isPositive) GreenIncome else RedFailed
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Bal: ₹${String.format("%.2f", record.balanceAfter)}",
                fontSize = 11.sp,
                color = TextGray
            )
        }
    }
}
