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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.SupportAgent
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RankingItem
import com.example.data.model.UserProfile
import com.example.ui.components.UserAvatar
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurpleDark
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextLightGray

@Composable
fun RankingScreen(
    userProfile: UserProfile?,
    rankingItems: List<RankingItem>,
    selectedPeriod: String,
    onSelectPeriod: (String) -> Unit,
    onOpenSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Vibrant Purple Top Header (Matching Screenshot 4)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED), Color(0xFF6D28D9))
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "8/19/2026",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Text(
                    text = "Ranking",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF))
                        .clickable { onOpenSupport() }
                        .testTag("ranking_support_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HeadsetMic,
                        contentDescription = "Support",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Period Filter Tabs (Yesterday, Today, Last week, This week)
        val periodTabs = listOf("Yesterday", "Today", "Last week", "This week")
        val tabScroll = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(tabScroll)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            periodTabs.forEach { period ->
                val isSelected = selectedPeriod == period
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onSelectPeriod(period) }
                        .padding(vertical = 4.dp)
                        .testTag("period_tab_${period.replace(" ", "_").lowercase()}")
                ) {
                    Text(
                        text = period,
                        color = if (isSelected) PurplePrimary else TextGray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .width(32.dp)
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

        // Current User Banner (Matching Screenshot 4)
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .testTag("current_user_rank_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UserAvatar(seed = userProfile?.uid ?: "78068", size = 42)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = userProfile?.uid ?: "78068",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                Text(
                    text = String.format("%.2f", if (selectedPeriod == "Yesterday") 0.0 else userProfile?.todayEarn ?: 0.0),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Table Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Ranking", fontSize = 13.sp, color = TextLightGray, fontWeight = FontWeight.Medium, modifier = Modifier.width(60.dp))
            Text(text = "UID", fontSize = 13.sp, color = TextLightGray, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f).padding(start = 24.dp))
            Text(text = "Amount", fontSize = 13.sp, color = TextLightGray, fontWeight = FontWeight.Medium)
        }

        HorizontalDivider(color = Color(0xFFF3F4F6))

        // Leaderboard List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("ranking_list")
        ) {
            items(rankingItems) { item ->
                RankingRow(item = item)
                HorizontalDivider(color = Color(0xFFF9FAFB))
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RankingRow(
    item: RankingItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank Badge
        Box(
            modifier = Modifier.width(50.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            when (item.rank) {
                1 -> MedalBadge(Color(0xFFF59E0B), "1")
                2 -> MedalBadge(Color(0xFF94A3B8), "2")
                3 -> MedalBadge(Color(0xFFD97706), "3")
                else -> Text(
                    text = "${item.rank}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }

        // Avatar + UID
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f).padding(start = 12.dp)
        ) {
            UserAvatar(seed = item.uid, size = 36)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = item.uid,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        // Amount
        Text(
            text = String.format("%.2f", item.amount),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
    }
}

@Composable
fun MedalBadge(color: Color, rankText: String) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rankText,
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp
        )
    }
}
