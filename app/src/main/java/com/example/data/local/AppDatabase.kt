package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.BalanceRecordItem
import com.example.data.model.CurrencyType
import com.example.data.model.InboxMessage
import com.example.data.model.OrderStatus
import com.example.data.model.OrderType
import com.example.data.model.P2POrder
import com.example.data.model.SupportMessage
import com.example.data.model.UserProfile
import com.example.data.model.WalletAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        P2POrder::class,
        WalletAccount::class,
        UserProfile::class,
        BalanceRecordItem::class,
        SupportMessage::class,
        InboxMessage::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mmpay_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.appDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: AppDao) {
            // Initial User Profile matching screenshots
            val initialUser = UserProfile(
                uid = "78068",
                phone = "6304754340",
                balance = 0.00,
                todayEarn = 0.00,
                totalEarn = 12699.89,
                inSellUpiCount = 0,
                referralCode = "https://ynwww.goldensizzle.com/register/in78068",
                teamMembersCount = 0,
                teamCommissionsToday = 0.00,
                totalTeamCommissions = 0.00
            )
            dao.insertUserProfile(initialUser)

            // Initial Wallets matching Screenshot 6
            val initialWallets = listOf(
                WalletAccount(
                    name = "Amazon",
                    iconKey = "amazon",
                    upiId = "amazonpay.6304754340@apl",
                    isBuyActive = true,
                    isSellActive = true
                ),
                WalletAccount(
                    name = "Paytm Business",
                    iconKey = "paytm",
                    upiId = "merchant9872@paytm",
                    isBuyActive = true,
                    isSellActive = true
                ),
                WalletAccount(
                    name = "Mobikwik",
                    iconKey = "mobikwik",
                    upiId = "6304754340@ikwik",
                    isBuyActive = true,
                    isSellActive = false
                ),
                WalletAccount(
                    name = "Mobikwik in MMPay",
                    iconKey = "mobikwik_mm",
                    upiId = "mmpay.mobikwik@axis",
                    isBuyActive = true,
                    isSellActive = true
                ),
                WalletAccount(
                    name = "Phonepe",
                    iconKey = "phonepe",
                    upiId = "6304754340@ybl",
                    isBuyActive = true,
                    isSellActive = true
                ),
                WalletAccount(
                    name = "Phonepe in MMPay",
                    iconKey = "phonepe_mm",
                    upiId = "mmpay.phonepe@ibl",
                    isBuyActive = true,
                    isSellActive = true
                ),
                WalletAccount(
                    name = "Freecharge",
                    iconKey = "freecharge",
                    upiId = "6304754340@freecharge",
                    isBuyActive = true,
                    isSellActive = false
                )
            )
            dao.insertWallets(initialWallets)

            // Initial Orders matching Screenshot 3 & Screenshot 1
            val initialOrders = listOf(
                P2POrder(
                    orderNo = "20260729233751130900252",
                    orderType = OrderType.BUY,
                    currency = CurrencyType.INR,
                    amount = 950.0,
                    incomeRatePercent = 2.9,
                    flatBonus = 6.0,
                    income = 33.55,
                    balanceAfter = 983.55,
                    channel = "Bank",
                    status = OrderStatus.SUCCESS,
                    orderTime = "30/07/2026 07:28:10",
                    paymentRefNumber = "UPI/202607292337/8912",
                    sellerUid = "88856"
                ),
                P2POrder(
                    orderNo = "20260724134724240144519",
                    orderType = OrderType.BUY,
                    currency = CurrencyType.INR,
                    amount = 964.0,
                    incomeRatePercent = 2.9,
                    flatBonus = 6.0,
                    income = 33.96,
                    balanceAfter = 997.96,
                    channel = "Bank",
                    status = OrderStatus.SUCCESS,
                    orderTime = "24/07/2026 14:02:36",
                    paymentRefNumber = "UPI/202607241347/6321",
                    sellerUid = "97871"
                ),
                P2POrder(
                    orderNo = "20260724115445847228691",
                    orderType = OrderType.BUY,
                    currency = CurrencyType.INR,
                    amount = 1000.0,
                    incomeRatePercent = 3.0,
                    flatBonus = 8.0,
                    income = 38.00,
                    balanceAfter = 1038.00,
                    channel = "PhonePe",
                    status = OrderStatus.SUCCESS,
                    orderTime = "24/07/2026 12:03:43",
                    paymentRefNumber = "UPI/202607241154/7743",
                    sellerUid = "63179"
                ),
                P2POrder(
                    orderNo = "20260721160103891001367",
                    orderType = OrderType.BUY,
                    currency = CurrencyType.INR,
                    amount = 1000.0,
                    incomeRatePercent = 3.0,
                    flatBonus = 8.0,
                    income = 38.00,
                    balanceAfter = 1000.00,
                    channel = "Paytm Business",
                    status = OrderStatus.FAILED,
                    orderTime = "21/07/2026 16:46:50",
                    paymentRefNumber = "",
                    sellerUid = "65763"
                )
            )
            dao.insertOrders(initialOrders)

            // Balance Records
            dao.insertBalanceRecord(
                BalanceRecordItem(
                    timeFormatted = "30/07/2026 07:28:10",
                    title = "Order Buy Reward (2.9%+6)",
                    amount = 33.55,
                    balanceAfter = 983.55,
                    isPositive = true,
                    orderNo = "20260729233751130900252"
                )
            )
            dao.insertBalanceRecord(
                BalanceRecordItem(
                    timeFormatted = "24/07/2026 14:02:36",
                    title = "Order Buy Reward (2.9%+6)",
                    amount = 33.96,
                    balanceAfter = 997.96,
                    isPositive = true,
                    orderNo = "20260724134724240144519"
                )
            )
            dao.insertBalanceRecord(
                BalanceRecordItem(
                    timeFormatted = "24/07/2026 12:03:43",
                    title = "Order Buy Reward (3.0%+8)",
                    amount = 38.00,
                    balanceAfter = 1038.00,
                    isPositive = true,
                    orderNo = "20260724115445847228691"
                )
            )

            // Inbox announcements
            dao.insertInboxMessage(
                InboxMessage(
                    title = "Welcome to MM Pay Task Platform",
                    content = "Start buying and selling to earn 2.8% to 3.3% commission per order! Invite friends to get 0.4% team dividends daily.",
                    date = "2026-08-19"
                )
            )
            dao.insertInboxMessage(
                InboxMessage(
                    title = "UPI Payment Channel Upgrade",
                    content = "Paytm Business and PhonePe MM Pay channels are now operating at 0 delay instant verification speed.",
                    date = "2026-08-18"
                )
            )

            // Initial Support chat greeting
            dao.insertSupportMessage(
                SupportMessage(
                    message = "Hello! Welcome to MM Pay Customer Support. How can we assist your P2P transactions and commission earnings today?",
                    isUser = false,
                    timeFormatted = "21:58"
                )
            )
        }
    }
}
