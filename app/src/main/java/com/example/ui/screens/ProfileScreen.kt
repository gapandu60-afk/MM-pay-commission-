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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.components.UserAvatar
import com.example.ui.components.copyToClipboard
import com.example.ui.theme.DarkHeaderEnd
import com.example.ui.theme.DarkHeaderStart
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextLightGray
import com.example.ui.viewmodel.SubScreen

@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    onNavigate: (SubScreen) -> Unit,
    onOpenLanguage: () -> Unit,
    onToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Dark Gradient Header (Matching Screenshot 5)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF3B2A45), Color(0xFF261933))
                    )
                )
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column {
                // User Details Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserAvatar(seed = userProfile?.uid ?: "78068", size = 64)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = userProfile?.phone ?: "6304754340",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                copyToClipboard(context, userProfile?.uid ?: "78068", "UID")
                                onToast("UID copied to clipboard!")
                            }
                        ) {
                            Text(
                                text = "UID: ${userProfile?.uid ?: "78068"}",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy UID",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Dark Earnings Card (Matching Screenshot 5)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF42344F).copy(alpha = 0.85f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF5A486B)),
                    modifier = Modifier.fillMaxWidth().testTag("profile_earnings_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Today's Earn",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹${String.format("%.2f", userProfile?.todayEarn ?: 0.0)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(Color.White.copy(alpha = 0.2f))
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Total Earn",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹${String.format("%.2f", userProfile?.totalEarn ?: 12699.89)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3 Quick Action Circles (Matching Screenshot 5)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Balance Record (Pink icon)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onNavigate(SubScreen.BALANCE_RECORDS) }
                    .testTag("profile_action_balance_record")
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEBF2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = "Balance Record",
                        tint = Color(0xFFEC4899),
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Balance Record",
                    fontSize = 12.sp,
                    color = TextDark,
                    fontWeight = FontWeight.Medium
                )
            }

            // Buy Order (Orange icon)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onNavigate(SubScreen.BUY_MARKET) }
                    .testTag("profile_action_buy_order")
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF4E5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Assignment,
                        contentDescription = "Buy Order",
                        tint = Color(0xFFF97316),
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Buy Order",
                    fontSize = 12.sp,
                    color = TextDark,
                    fontWeight = FontWeight.Medium
                )
            }

            // Sell Order (Green icon)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onNavigate(SubScreen.SELL_MARKET) }
                    .testTag("profile_action_sell_order")
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE6F9F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restore,
                        contentDescription = "Sell Order",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sell Order",
                    fontSize = 12.sp,
                    color = TextDark,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Menu Rows (Matching Screenshot 5)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProfileMenuItem(
                title = "Language",
                icon = Icons.Default.Language,
                onClick = onOpenLanguage,
                testTag = "menu_item_language"
            )

            ProfileMenuItem(
                title = "Service",
                icon = Icons.Default.Headphones,
                onClick = { onNavigate(SubScreen.SERVICE_CHAT) },
                testTag = "menu_item_service"
            )

            ProfileMenuItem(
                title = "Inbox",
                icon = Icons.Default.Mail,
                onClick = { onNavigate(SubScreen.INBOX) },
                testTag = "menu_item_inbox"
            )

            ProfileMenuItem(
                title = "Download APK",
                icon = Icons.Default.CloudDownload,
                onClick = { onToast("MM Pay official release APK is up to date (v2.4.0)!") },
                testTag = "menu_item_download_apk"
            )

            ProfileMenuItem(
                title = "Logout",
                icon = Icons.Default.PowerSettingsNew,
                onClick = { onToast("You are securely authenticated as UID: 78068.") },
                testTag = "menu_item_logout"
            )
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
fun ProfileMenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = TextDark,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextDark
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextLightGray,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
