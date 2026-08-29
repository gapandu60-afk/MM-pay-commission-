package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class OrderType {
    BUY, SELL
}

enum class OrderStatus {
    PAYING, CHECKING, SUCCESS, FAILED
}

enum class CurrencyType {
    INR, USDT
}

enum class TierCategory(val displayName: String, val ratePercent: Double, val bonusFlat: Double) {
    ALL("All", 2.9, 6.0),
    MINI("Mini\n2.8%", 2.8, 5.0),
    SMALL("Small\n2.9%", 2.9, 6.0),
    MEDIUM("Medium\n3%", 3.0, 8.0),
    LARGE("Large\n3.1%", 3.1, 12.0),
    MAX("Max\n3.3%", 3.3, 20.0)
}

@Entity(tableName = "orders")
data class P2POrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNo: String = "20260819000000000000000",
    val orderType: OrderType = OrderType.BUY,
    val currency: CurrencyType = CurrencyType.INR,
    val amount: Double = 950.0,
    val incomeRatePercent: Double = 2.9,
    val flatBonus: Double = 6.0,
    val income: Double = 33.55,
    val balanceAfter: Double = 983.55,
    val channel: String = "Bank",
    val status: OrderStatus = OrderStatus.PAYING,
    val orderTime: String = "30/07/2026 07:28:10",
    val timestamp: Long = System.currentTimeMillis(),
    val buyerUid: String = "78068",
    val sellerUid: String = "92415",
    val sellerUpi: String = "mmpay.merchant@icici",
    val paymentRefNumber: String = "",
    val paymentProofPath: String? = null
)

@Entity(tableName = "wallets")
data class WalletAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconKey: String,
    val upiId: String,
    val isBuyActive: Boolean = true,
    val isSellActive: Boolean = true,
    val accountHolder: String = "Merchant User",
    val isVerified: Boolean = true
)

@Entity(tableName = "user_account")
data class UserProfile(
    @PrimaryKey
    val uid: String = "78068",
    val phone: String = "6304754340",
    val balance: Double = 598.70,
    val todayEarn: Double = 33.96,
    val totalEarn: Double = 12699.89,
    val inSellUpiCount: Int = 2,
    val referralCode: String = "https://ynwww.goldensizzle.com/register/in78068",
    val teamMembersCount: Int = 18,
    val teamCommissionsToday: Double = 142.50,
    val totalTeamCommissions: Double = 4380.00,
    val dailyCheckInDone: Boolean = false,
    val newbieRewardsClaimed: Boolean = false
)

@Entity(tableName = "balance_records")
data class BalanceRecordItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val timeFormatted: String,
    val title: String,
    val amount: Double,
    val balanceAfter: Double,
    val isPositive: Boolean,
    val orderNo: String? = null
)

data class TeamMemberItem(
    val uid: String,
    val phoneMasked: String,
    val joinDate: String,
    val todayPurchases: Double,
    val totalPurchases: Double,
    val commissionEarned: Double
)

data class RankingItem(
    val rank: Int,
    val uid: String,
    val amount: Double,
    val isCurrentUser: Boolean = false
)

@Entity(tableName = "support_messages")
data class SupportMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val timeFormatted: String
)

@Entity(tableName = "inbox_messages")
data class InboxMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val date: String,
    val isRead: Boolean = false
)
