package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TeamMemberItem
import com.example.data.model.UserProfile
import com.example.ui.components.copyToClipboard
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurpleDark
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextLightGray

@Composable
fun TeamScreen(
    userProfile: UserProfile?,
    teamMembers: List<TeamMemberItem>,
    onViewRewardsDetail: () -> Unit,
    onToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val inviteUrl = userProfile?.referralCode ?: "https://ynwww.goldensizzle.com/register/in78068"

    fun shareInvite(platform: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Join MM Pay and earn ₹3,000+ daily completing P2P task commissions! Use my invite link: $inviteUrl"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share via $platform")
        context.startActivity(shareIntent)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED), Color(0xFF6D28D9))
                )
            )
            .verticalScroll(scrollState)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(36.dp))
            Text(
                text = "My Team",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0x33FFFFFF),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onViewRewardsDetail() }
                    .testTag("view_team_rewards_pill")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View team rewards",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // White Container Sheet (Matching Screenshot 2)
        Card(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("team_content_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // My Balance Row
                Text(
                    text = "My Balance",
                    fontSize = 14.sp,
                    color = TextGray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Wallet",
                        tint = PurplePrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "₹ ${String.format("%.2f", userProfile?.balance ?: 0.0)}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = TextDark
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Today's Team Data
                Text(
                    text = "Today's Team Data",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Team Commissions", fontSize = 12.sp, color = TextGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹ ${String.format("%.2f", userProfile?.teamCommissionsToday ?: 0.0)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurplePrimary
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Team Members", fontSize = 12.sp, color = TextGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${userProfile?.teamMembersCount ?: 0}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurplePrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Total Team Data
                Text(
                    text = "Total Team Data",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Team Commissions", fontSize = 12.sp, color = TextGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "₹ ${String.format("%.2f", userProfile?.totalTeamCommissions ?: 0.0)}",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PurplePrimary
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = PurplePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Team Members", fontSize = 12.sp, color = TextGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${userProfile?.teamMembersCount ?: 0}",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PurplePrimary
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = PurplePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Info Box (Matching Screenshot 2)
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row {
                            Text(
                                text = "You will receive ",
                                fontSize = 13.sp,
                                color = TextDark
                            )
                            Text(
                                text = "0.4% ",
                                fontSize = 13.sp,
                                color = PurplePrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "for each purchase transaction of the team.",
                                fontSize = 13.sp,
                                color = TextDark
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "For example: You invited 50 friends to join MM Pay.\nIf each friend buys ₹100,000 daily, you will earn commissions:\n50 * 100,000 * 0.4% = ₹20000 daily",
                            fontSize = 12.sp,
                            color = TextGray,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Invitation Link Box (Matching Screenshot 2)
                Text(
                    text = "Invitation Link",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF3F4F6),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = inviteUrl,
                            fontSize = 13.sp,
                            color = TextGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                copyToClipboard(context, inviteUrl, "Invitation Link")
                                onToast("Invitation link copied to clipboard!")
                            },
                            modifier = Modifier.size(24.dp).testTag("copy_invite_link_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Link",
                                tint = TextDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // More Ways to Invite (Telegram, Whatsapp, Facebook, Twitter, Instagram)
                Text(
                    text = "More Ways to Invite",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val shareChannels = listOf(
                        Triple("Telegram", Color(0xFF0088CC), "telegram"),
                        Triple("Whatsapp", Color(0xFF25D366), "whatsapp"),
                        Triple("Facebook", Color(0xFF1877F2), "facebook"),
                        Triple("Twitter", Color(0xFF111111), "twitter"),
                        Triple("Instagram", Color(0xFFE4405F), "instagram")
                    )

                    shareChannels.forEach { (name, color, key) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { shareInvite(name) }
                                .testTag("share_btn_$key")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(color),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = name,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = name,
                                fontSize = 11.sp,
                                color = TextDark,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFFF3F4F6))
                Spacer(modifier = Modifier.height(14.dp))

                // Direct Team Members Breakdown
                Text(
                    text = "Direct Team Members (${teamMembers.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                teamMembers.forEach { member ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "UID: ${member.uid} (${member.phoneMasked})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "Joined: ${member.joinDate}",
                                fontSize = 11.sp,
                                color = TextLightGray
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "+ ₹${member.commissionEarned.toInt()} Earned",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenIncome
                            )
                            Text(
                                text = "Vol: ₹${member.totalPurchases.toInt()}",
                                fontSize = 11.sp,
                                color = TextGray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
