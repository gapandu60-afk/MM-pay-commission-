package com.example.data.repository

import com.example.data.local.AppDao
import com.example.data.model.BalanceRecordItem
import com.example.data.model.CurrencyType
import com.example.data.model.InboxMessage
import com.example.data.model.OrderStatus
import com.example.data.model.OrderType
import com.example.data.model.P2POrder
import com.example.data.model.RankingItem
import com.example.data.model.SupportMessage
import com.example.data.model.TeamMemberItem
import com.example.data.model.TierCategory
import com.example.data.model.UserProfile
import com.example.data.model.WalletAccount
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class MMPayRepository(private val appDao: AppDao) {

    val allOrders: Flow<List<P2POrder>> = appDao.getAllOrders()
    val allWallets: Flow<List<WalletAccount>> = appDao.getAllWallets()
    val userProfile: Flow<UserProfile?> = appDao.getUserProfile()
    val balanceRecords: Flow<List<BalanceRecordItem>> = appDao.getAllBalanceRecords()
    val supportMessages: Flow<List<SupportMessage>> = appDao.getAllSupportMessages()
    val inboxMessages: Flow<List<InboxMessage>> = appDao.getAllInboxMessages()

    // Live generated marketplace orders available to Buy
    fun getAvailableMarketOrders(tier: TierCategory, currency: CurrencyType): List<P2POrder> {
        val timeStr = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        val baseOrders = listOf(
            P2POrder(
                orderNo = "20260819" + Random.nextLong(100000000000L, 999999999999L),
                orderType = OrderType.BUY,
                currency = currency,
                amount = 576.0,
                incomeRatePercent = 2.9,
                flatBonus = 6.0,
                income = 22.70,
                balanceAfter = 598.70,
                channel = "Bank",
                status = OrderStatus.PAYING,
                orderTime = timeStr,
                sellerUid = "63179",
                sellerUpi = "paytm.merchant576@icici"
            ),
            P2POrder(
                orderNo = "20260819" + Random.nextLong(100000000000L, 999999999999L),
                orderType = OrderType.BUY,
                currency = currency,
                amount = 964.0,
                incomeRatePercent = 2.9,
                flatBonus = 6.0,
                income = 33.96,
                balanceAfter = 997.96,
                channel = "Bank",
                status = OrderStatus.PAYING,
                orderTime = timeStr,
                sellerUid = "88856",
                sellerUpi = "phonepe.pay964@ybl"
            ),
            P2POrder(
                orderNo = "20260819" + Random.nextLong(100000000000L, 999999999999L),
                orderType = OrderType.BUY,
                currency = currency,
                amount = 1250.0,
                incomeRatePercent = 3.0,
                flatBonus = 8.0,
                income = 45.50,
                balanceAfter = 1295.50,
                channel = "Paytm Business",
                status = OrderStatus.PAYING,
                orderTime = timeStr,
                sellerUid = "97871",
                sellerUpi = "merchant1250@paytm"
            ),
            P2POrder(
                orderNo = "20260819" + Random.nextLong(100000000000L, 999999999999L),
                orderType = OrderType.BUY,
                currency = currency,
                amount = 2500.0,
                incomeRatePercent = 3.1,
                flatBonus = 12.0,
                income = 89.50,
                balanceAfter = 2589.50,
                channel = "Phonepe",
                status = OrderStatus.PAYING,
                orderTime = timeStr,
                sellerUid = "65763",
                sellerUpi = "mmpay.phonepe2500@ibl"
            ),
            P2POrder(
                orderNo = "20260819" + Random.nextLong(100000000000L, 999999999999L),
                orderType = OrderType.BUY,
                currency = currency,
                amount = 5000.0,
                incomeRatePercent = 3.3,
                flatBonus = 20.0,
                income = 185.00,
                balanceAfter = 5185.00,
                channel = "Amazon",
                status = OrderStatus.PAYING,
                orderTime = timeStr,
                sellerUid = "30913",
                sellerUpi = "amazonpay5000@apl"
            ),
            P2POrder(
                orderNo = "20260819" + Random.nextLong(100000000000L, 999999999999L),
                orderType = OrderType.BUY,
                currency = currency,
                amount = 800.0,
                incomeRatePercent = 2.8,
                flatBonus = 5.0,
                income = 27.40,
                balanceAfter = 827.40,
                channel = "Mobikwik",
                status = OrderStatus.PAYING,
                orderTime = timeStr,
                sellerUid = "102390",
                sellerUpi = "mobikwik800@ikwik"
            )
        )

        return when (tier) {
            TierCategory.ALL -> baseOrders
            TierCategory.MINI -> baseOrders.filter { it.amount < 900 }
            TierCategory.SMALL -> baseOrders.filter { it.amount in 900.0..1000.0 }
            TierCategory.MEDIUM -> baseOrders.filter { it.amount in 1001.0..2000.0 }
            TierCategory.LARGE -> baseOrders.filter { it.amount in 2001.0..4000.0 }
            TierCategory.MAX -> baseOrders.filter { it.amount >= 4001.0 }
        }
    }

    suspend fun createAndExecuteOrder(
        amount: Double,
        currency: CurrencyType = CurrencyType.INR,
        channel: String = "Bank",
        tier: TierCategory = TierCategory.SMALL
    ): Long {
        val rate = tier.ratePercent
        val bonus = tier.bonusFlat
        val income = (amount * (rate / 100.0)) + bonus
        val roundedIncome = Math.round(income * 100.0) / 100.0
        val balanceAfter = Math.round((amount + roundedIncome) * 100.0) / 100.0
        val timeFormatted = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        val orderNo = "20260819" + Random.nextLong(100000000000L, 999999999999L)

        val newOrder = P2POrder(
            orderNo = orderNo,
            orderType = OrderType.BUY,
            currency = currency,
            amount = amount,
            incomeRatePercent = rate,
            flatBonus = bonus,
            income = roundedIncome,
            balanceAfter = balanceAfter,
            channel = channel,
            status = OrderStatus.PAYING,
            orderTime = timeFormatted,
            sellerUid = (10000 + Random.nextInt(90000)).toString(),
            sellerUpi = "merchant.${Random.nextInt(1000, 9999)}@icici"
        )
        return appDao.insertOrder(newOrder)
    }

    suspend fun submitOrderProof(orderId: Long, utrReference: String) {
        appDao.submitPaymentProof(orderId, utrReference, OrderStatus.CHECKING)
    }

    suspend fun completeOrder(orderId: Long) {
        val order = appDao.getOrderById(orderId) ?: return
        appDao.updateOrderStatus(orderId, OrderStatus.SUCCESS)

        // Update user balances
        val currentProfile = appDao.getUserProfile().firstOrNull() ?: UserProfile()
        val newBalance = currentProfile.balance + order.balanceAfter
        val newTodayEarn = currentProfile.todayEarn + order.income
        val newTotalEarn = currentProfile.totalEarn + order.income

        val updatedProfile = currentProfile.copy(
            balance = Math.round(newBalance * 100.0) / 100.0,
            todayEarn = Math.round(newTodayEarn * 100.0) / 100.0,
            totalEarn = Math.round(newTotalEarn * 100.0) / 100.0
        )
        appDao.updateUserProfile(updatedProfile)

        // Add to Balance record
        val timeFormatted = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        appDao.insertBalanceRecord(
            BalanceRecordItem(
                timeFormatted = timeFormatted,
                title = "Order Commission (${order.incomeRatePercent}%+${order.flatBonus.toInt()})",
                amount = order.income,
                balanceAfter = updatedProfile.balance,
                isPositive = true,
                orderNo = order.orderNo
            )
        )
    }

    suspend fun cancelOrder(orderId: Long) {
        appDao.updateOrderStatus(orderId, OrderStatus.FAILED)
    }

    suspend fun addWallet(name: String, upiId: String, iconKey: String) {
        val newWallet = WalletAccount(
            name = name,
            iconKey = iconKey,
            upiId = upiId,
            isBuyActive = true,
            isSellActive = true
        )
        appDao.insertWallet(newWallet)
        updateActiveSellUpiCount()
    }

    suspend fun toggleWalletStatus(walletId: Long, isBuy: Boolean, isSell: Boolean) {
        val wallets = appDao.getAllWallets().firstOrNull() ?: return
        val wallet = wallets.find { it.id == walletId } ?: return
        val updated = wallet.copy(isBuyActive = isBuy, isSellActive = isSell)
        appDao.updateWallet(updated)
        updateActiveSellUpiCount()
    }

    private suspend fun updateActiveSellUpiCount() {
        val wallets = appDao.getAllWallets().firstOrNull() ?: emptyList()
        val activeCount = wallets.count { it.isSellActive }
        val currentProfile = appDao.getUserProfile().firstOrNull() ?: UserProfile()
        appDao.updateUserProfile(currentProfile.copy(inSellUpiCount = activeCount))
    }

    suspend fun claimDailyReward(): Double {
        val rewardAmount = 50.0
        val currentProfile = appDao.getUserProfile().firstOrNull() ?: UserProfile()
        if (currentProfile.dailyCheckInDone) return 0.0

        val updatedProfile = currentProfile.copy(
            balance = currentProfile.balance + rewardAmount,
            todayEarn = currentProfile.todayEarn + rewardAmount,
            totalEarn = currentProfile.totalEarn + rewardAmount,
            dailyCheckInDone = true
        )
        appDao.updateUserProfile(updatedProfile)

        val timeFormatted = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        appDao.insertBalanceRecord(
            BalanceRecordItem(
                timeFormatted = timeFormatted,
                title = "Daily Check-in Bonus",
                amount = rewardAmount,
                balanceAfter = updatedProfile.balance,
                isPositive = true
            )
        )
        return rewardAmount
    }

    suspend fun claimNewbieBonus(): Double {
        val bonus = 100.0
        val currentProfile = appDao.getUserProfile().firstOrNull() ?: UserProfile()
        if (currentProfile.newbieRewardsClaimed) return 0.0

        val updatedProfile = currentProfile.copy(
            balance = currentProfile.balance + bonus,
            todayEarn = currentProfile.todayEarn + bonus,
            totalEarn = currentProfile.totalEarn + bonus,
            newbieRewardsClaimed = true
        )
        appDao.updateUserProfile(updatedProfile)

        val timeFormatted = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        appDao.insertBalanceRecord(
            BalanceRecordItem(
                timeFormatted = timeFormatted,
                title = "Newbie Starter Bonus",
                amount = bonus,
                balanceAfter = updatedProfile.balance,
                isPositive = true
            )
        )
        return bonus
    }

    suspend fun sendSupportMessage(text: String) {
        val timeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        appDao.insertSupportMessage(
            SupportMessage(
                message = text,
                isUser = true,
                timeFormatted = timeFormatted
            )
        )

        // Automated helpful reply
        delay(600)
        val autoReply = when {
            text.contains("order", ignoreCase = true) || text.contains("buy", ignoreCase = true) ->
                "For active orders, please ensure you submit the exact 12-digit UTR/UPI reference number after completing payment. Verification is completed in under 2 minutes."
            text.contains("commission", ignoreCase = true) || text.contains("income", ignoreCase = true) ->
                "Order commissions are automatically calculated (2.8% to 3.3% + bonus flat amount) and credited to your wallet immediately upon order confirmation."
            text.contains("team", ignoreCase = true) || text.contains("referral", ignoreCase = true) ->
                "You earn 0.4% team commission on every purchase made by your invited members, settled automatically every 24 hours."
            text.contains("upi", ignoreCase = true) || text.contains("wallet", ignoreCase = true) ->
                "You can link multiple UPI wallets in the '+ UPI' section (Paytm, PhonePe, Mobikwik, Amazon Pay) to receive automatic sell orders."
            else ->
                "Thank you for contacting MM Pay Support. An agent has verified your account (UID: 78068). We are actively monitoring your transactions."
        }
        appDao.insertSupportMessage(
            SupportMessage(
                message = autoReply,
                isUser = false,
                timeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            )
        )
    }

    fun getRankingList(period: String): List<RankingItem> {
        val multiplier = when (period) {
            "Yesterday" -> 1.0
            "Today" -> 0.85
            "Last week" -> 5.2
            "This week" -> 3.6
            else -> 1.0
        }
        return listOf(
            RankingItem(rank = 1, uid = "63179", amount = 72082.00 * multiplier),
            RankingItem(rank = 2, uid = "88856", amount = 59844.00 * multiplier),
            RankingItem(rank = 3, uid = "97871", amount = 55000.00 * multiplier),
            RankingItem(rank = 4, uid = "65763", amount = 54838.00 * multiplier),
            RankingItem(rank = 5, uid = "30913", amount = 40100.00 * multiplier),
            RankingItem(rank = 6, uid = "102390", amount = 40000.00 * multiplier),
            RankingItem(rank = 7, uid = "31114", amount = 39500.00 * multiplier),
            RankingItem(rank = 8, uid = "92415", amount = 36800.00 * multiplier),
            RankingItem(rank = 9, uid = "45120", amount = 31200.00 * multiplier),
            RankingItem(rank = 10, uid = "78068", amount = 12699.89 * (if (period == "Yesterday") 0.0 else 1.0), isCurrentUser = true)
        )
    }

    fun getTeamMembers(): List<TeamMemberItem> {
        return listOf(
            TeamMemberItem(uid = "92415", phoneMasked = "98****1234", joinDate = "2026-08-10", todayPurchases = 25000.0, totalPurchases = 180000.0, commissionEarned = 720.0),
            TeamMemberItem(uid = "63179", phoneMasked = "91****5521", joinDate = "2026-08-12", todayPurchases = 50000.0, totalPurchases = 320000.0, commissionEarned = 1280.0),
            TeamMemberItem(uid = "88856", phoneMasked = "87****9032", joinDate = "2026-08-14", todayPurchases = 15000.0, totalPurchases = 95000.0, commissionEarned = 380.0),
            TeamMemberItem(uid = "65763", phoneMasked = "70****4419", joinDate = "2026-08-15", todayPurchases = 30000.0, totalPurchases = 140000.0, commissionEarned = 560.0),
            TeamMemberItem(uid = "30913", phoneMasked = "99****8110", joinDate = "2026-08-18", todayPurchases = 10000.0, totalPurchases = 45000.0, commissionEarned = 180.0)
        )
    }
}
