package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BalanceRecordItem
import com.example.data.model.CurrencyType
import com.example.data.model.InboxMessage
import com.example.data.model.OrderStatus
import com.example.data.model.P2POrder
import com.example.data.model.SupportMessage
import com.example.data.model.UserProfile
import com.example.data.model.WalletAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Orders
    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAllOrders(): Flow<List<P2POrder>>

    @Query("SELECT * FROM orders WHERE currency = :currency ORDER BY id DESC")
    fun getOrdersByCurrency(currency: CurrencyType): Flow<List<P2POrder>>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: Long): P2POrder?

    @Query("SELECT * FROM orders WHERE orderNo = :orderNo")
    suspend fun getOrderByNo(orderNo: String): P2POrder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: P2POrder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<P2POrder>)

    @Update
    suspend fun updateOrder(order: P2POrder)

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus)

    @Query("UPDATE orders SET paymentRefNumber = :refNo, status = :status WHERE id = :orderId")
    suspend fun submitPaymentProof(orderId: Long, refNo: String, status: OrderStatus = OrderStatus.CHECKING)

    // User Profile
    @Query("SELECT * FROM user_account WHERE uid = :uid LIMIT 1")
    fun getUserProfile(uid: String = "78068"): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(user: UserProfile)

    @Update
    suspend fun updateUserProfile(user: UserProfile)

    // Wallets
    @Query("SELECT * FROM wallets ORDER BY id ASC")
    fun getAllWallets(): Flow<List<WalletAccount>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: WalletAccount): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallets(wallets: List<WalletAccount>)

    @Update
    suspend fun updateWallet(wallet: WalletAccount)

    @Query("DELETE FROM wallets WHERE id = :id")
    suspend fun deleteWallet(id: Long)

    // Balance Records
    @Query("SELECT * FROM balance_records ORDER BY id DESC")
    fun getAllBalanceRecords(): Flow<List<BalanceRecordItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalanceRecord(record: BalanceRecordItem)

    // Support
    @Query("SELECT * FROM support_messages ORDER BY timestamp ASC")
    fun getAllSupportMessages(): Flow<List<SupportMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupportMessage(message: SupportMessage)

    // Inbox
    @Query("SELECT * FROM inbox_messages ORDER BY id DESC")
    fun getAllInboxMessages(): Flow<List<InboxMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInboxMessage(msg: InboxMessage)

    @Query("UPDATE inbox_messages SET isRead = 1 WHERE id = :id")
    suspend fun markInboxAsRead(id: Long)
}
