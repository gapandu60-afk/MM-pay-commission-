package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CurrencyType
import com.example.data.model.OrderStatus
import com.example.ui.theme.BlueChecking
import com.example.ui.theme.DarkHeaderEnd
import com.example.ui.theme.DarkHeaderStart
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangePending
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurpleDark
import com.example.ui.theme.PurpleGradientEnd
import com.example.ui.theme.PurpleGradientStart
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.RedFailed
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextLightGray
import com.example.ui.viewmodel.AppNavTab

fun copyToClipboard(context: Context, text: String, label: String = "Copied text") {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

@Composable
fun AppBottomBar(
    currentTab: AppNavTab,
    onTabSelected: (AppNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(AppNavTab.HOME, "Home", Icons.Default.Home),
            Triple(AppNavTab.ORDERS, "Orders", Icons.AutoMirrored.Filled.Assignment),
            Triple(AppNavTab.TEAM, "Team", Icons.Default.People),
            Triple(AppNavTab.RANKING, "Ranking", Icons.Default.EmojiEvents),
            Triple(AppNavTab.MY, "My", Icons.Default.Person)
        )

        items.forEach { (tab, title, icon) ->
            val selected = currentTab == tab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PurplePrimary,
                    selectedTextColor = PurplePrimary,
                    indicatorColor = PurpleBg,
                    unselectedIconColor = TextLightGray,
                    unselectedTextColor = TextGray
                ),
                modifier = Modifier.testTag("nav_item_${title.lowercase()}")
            )
        }
    }
}

@Composable
fun CurrencyToggle(
    selectedCurrency: CurrencyType,
    onCurrencySelected: (CurrencyType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF1F2F6),
            modifier = Modifier.width(260.dp).height(38.dp)
        ) {
            Row(modifier = Modifier.padding(3.dp)) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (selectedCurrency == CurrencyType.INR) PurplePrimary else Color.Transparent)
                        .clickable { onCurrencySelected(CurrencyType.INR) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "INR",
                        color = if (selectedCurrency == CurrencyType.INR) Color.White else TextDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (selectedCurrency == CurrencyType.USDT) PurplePrimary else Color.Transparent)
                        .clickable { onCurrencySelected(CurrencyType.USDT) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "USDT",
                        color = if (selectedCurrency == CurrencyType.USDT) Color.White else TextDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun IncomeRateBanner(
    rateText: String = "2.9% + 6",
    subText: String = "Income per Order",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED), Color(0xFF6D28D9))
                )
            )
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rateText,
                color = GoldYellow,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = subText,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StatusBadge(status: OrderStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when (status) {
        OrderStatus.SUCCESS -> Triple(Color(0xFFE6F9F0), GreenSuccess, "Success")
        OrderStatus.PAYING -> Triple(Color(0xFFFFF3E8), OrangePending, "Paying")
        OrderStatus.CHECKING -> Triple(Color(0xFFE8F2FF), BlueChecking, "Checking")
        OrderStatus.FAILED -> Triple(Color(0xFFFFEAEA), RedFailed, "Failed")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun UserAvatar(
    seed: String = "78068",
    size: Int = 46,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFFDE68A), Color(0xFFF59E0B), Color(0xFFD97706))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User Avatar",
            tint = Color.White,
            modifier = Modifier.size((size * 0.7).dp)
        )
    }
}

@Composable
fun TopHeaderPurple(
    title: String,
    onBack: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF7C3AED), Color(0xFF6D28D9))
                )
            )
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}
