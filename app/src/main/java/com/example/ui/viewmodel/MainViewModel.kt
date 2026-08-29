package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BalanceRecordItem
import com.example.data.model.CurrencyType
import com.example.data.model.InboxMessage
import com.example.data.model.OrderStatus
import com.example.data.model.P2POrder
import com.example.data.model.RankingItem
import com.example.data.model.SupportMessage
import com.example.data.model.TeamMemberItem
import com.example.data.model.TierCategory
import com.example.data.model.UserProfile
import com.example.data.model.WalletAccount
import com.example.data.repository.MMPayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavTab {
    HOME, ORDERS, TEAM, RANKING, MY
}

enum class SubScreen {
    NONE,
    BUY_MARKET,
    SELL_MARKET,
    WALLET_LIST,
    ORDER_DETAIL,
    BALANCE_RECORDS,
    SERVICE_CHAT,
    INBOX,
    TEAM_DETAILS
}

enum class SortDirection {
    HIGH_TO_LOW, LOW_TO_HIGH
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MMPayRepository

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = MMPayRepository(database.appDao())
    }

    val userProfile: StateFlow<UserProfile?> = repository.userProfile.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserProfile()
    )

    val allOrders: StateFlow<List<P2POrder>> = repository.allOrders.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val wallets: StateFlow<List<WalletAccount>> = repository.allWallets.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val balanceRecords: StateFlow<List<BalanceRecordItem>> = repository.balanceRecords.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val supportMessages: StateFlow<List<SupportMessage>> = repository.supportMessages.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val inboxMessages: StateFlow<List<InboxMessage>> = repository.inboxMessages.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Navigation state
    private val _currentTab = MutableStateFlow(AppNavTab.HOME)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    private val _subScreenStack = MutableStateFlow<List<SubScreen>>(emptyList())
    val subScreenStack: StateFlow<List<SubScreen>> = _subScreenStack.asStateFlow()

    val currentSubScreen: SubScreen
        get() = _subScreenStack.value.lastOrNull() ?: SubScreen.NONE

    // Buy Market State
    private val _selectedCurrency = MutableStateFlow(CurrencyType.INR)
    val selectedCurrency: StateFlow<CurrencyType> = _selectedCurrency.asStateFlow()

    private val _selectedTier = MutableStateFlow(TierCategory.SMALL)
    val selectedTier: StateFlow<TierCategory> = _selectedTier.asStateFlow()

    private val _sortDirection = MutableStateFlow(SortDirection.HIGH_TO_LOW)
    val sortDirection: StateFlow<SortDirection> = _sortDirection.asStateFlow()

    // Orders Filter Tab (All, Paying, Checking, Success, Failed)
    private val _selectedOrderStatusFilter = MutableStateFlow<OrderStatus?>(null)
    val selectedOrderStatusFilter: StateFlow<OrderStatus?> = _selectedOrderStatusFilter.asStateFlow()

    // Ranking Period Tab
    private val _selectedRankingPeriod = MutableStateFlow("Yesterday")
    val selectedRankingPeriod: StateFlow<String> = _selectedRankingPeriod.asStateFlow()

    // Selected Order for detail screen
    private val _activeOrderDetail = MutableStateFlow<P2POrder?>(null)
    val activeOrderDetail: StateFlow<P2POrder?> = _activeOrderDetail.asStateFlow()

    // Dialogs State
    private val _showDailyRewardDialog = MutableStateFlow(false)
    val showDailyRewardDialog: StateFlow<Boolean> = _showDailyRewardDialog.asStateFlow()

    private val _showNewbieDialog = MutableStateFlow(false)
    val showNewbieDialog: StateFlow<Boolean> = _showNewbieDialog.asStateFlow()

    private val _showAddWalletDialog = MutableStateFlow(false)
    val showAddWalletDialog: StateFlow<Boolean> = _showAddWalletDialog.asStateFlow()

    private val _showLanguageDialog = MutableStateFlow(false)
    val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

    private val _currentLanguage = MutableStateFlow("English")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _showCustomBuyDialog = MutableStateFlow(false)
    val showCustomBuyDialog: StateFlow<Boolean> = _showCustomBuyDialog.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Navigation Methods
    fun selectTab(tab: AppNavTab) {
        _currentTab.value = tab
        _subScreenStack.value = emptyList()
    }

    fun navigateTo(subScreen: SubScreen) {
        _subScreenStack.value = _subScreenStack.value + subScreen
    }

    fun popSubScreen(): Boolean {
        val current = _subScreenStack.value
        return if (current.isNotEmpty()) {
            _subScreenStack.value = current.dropLast(1)
            true
        } else {
            false
        }
    }

    fun setCurrency(currency: CurrencyType) {
        _selectedCurrency.value = currency
    }

    fun setTier(tier: TierCategory) {
        _selectedTier.value = tier
    }

    fun toggleSortDirection() {
        _sortDirection.value = if (_sortDirection.value == SortDirection.HIGH_TO_LOW) {
            SortDirection.LOW_TO_HIGH
        } else {
            SortDirection.HIGH_TO_LOW
        }
    }

    fun setOrderStatusFilter(status: OrderStatus?) {
        _selectedOrderStatusFilter.value = status
    }

    fun setRankingPeriod(period: String) {
        _selectedRankingPeriod.value = period
    }

    fun getRankingEntries(): List<RankingItem> {
        return repository.getRankingList(_selectedRankingPeriod.value)
    }

    fun getTeamMembers(): List<TeamMemberItem> {
        return repository.getTeamMembers()
    }

    fun getAvailableMarketOrders(): List<P2POrder> {
        val orders = repository.getAvailableMarketOrders(_selectedTier.value, _selectedCurrency.value)
        return if (_sortDirection.value == SortDirection.HIGH_TO_LOW) {
            orders.sortedByDescending { it.amount }
        } else {
            orders.sortedBy { it.amount }
        }
    }

    fun openOrderDetail(order: P2POrder) {
        _activeOrderDetail.value = order
        navigateTo(SubScreen.ORDER_DETAIL)
    }

    fun grabOrderAndProceed(order: P2POrder) {
        viewModelScope.launch {
            val insertedId = repository.createAndExecuteOrder(
                amount = order.amount,
                currency = order.currency,
                channel = order.channel,
                tier = _selectedTier.value
            )
            // fetch created order and open
            val newOrder = order.copy(id = insertedId, status = OrderStatus.PAYING)
            _activeOrderDetail.value = newOrder
            showToast("Order matched successfully! Please complete payment within 15 minutes.")
            navigateTo(SubScreen.ORDER_DETAIL)
        }
    }

    fun createCustomOrder(amount: Double, channel: String) {
        viewModelScope.launch {
            val tier = when {
                amount < 900 -> TierCategory.MINI
                amount in 900.0..1000.0 -> TierCategory.SMALL
                amount in 1001.0..2000.0 -> TierCategory.MEDIUM
                amount in 2001.0..4000.0 -> TierCategory.LARGE
                else -> TierCategory.MAX
            }
            val insertedId = repository.createAndExecuteOrder(amount, _selectedCurrency.value, channel, tier)
            _showCustomBuyDialog.value = false
            showToast("Custom Buy Order of ₹$amount initiated!")
            val newOrder = repository.getAvailableMarketOrders(tier, _selectedCurrency.value).first().copy(id = insertedId, amount = amount)
            _activeOrderDetail.value = newOrder
            navigateTo(SubScreen.ORDER_DETAIL)
        }
    }

    fun submitUtrAndConfirm(orderId: Long, utr: String) {
        viewModelScope.launch {
            repository.submitOrderProof(orderId, utr)
            _activeOrderDetail.value = _activeOrderDetail.value?.copy(
                paymentRefNumber = utr,
                status = OrderStatus.CHECKING
            )
            showToast("UTR submitted! Verifying transaction with merchant bank...")
        }
    }

    fun completeOrderDirectly(orderId: Long) {
        viewModelScope.launch {
            repository.completeOrder(orderId)
            _activeOrderDetail.value = _activeOrderDetail.value?.copy(status = OrderStatus.SUCCESS)
            showToast("Order Successful! Commission credited to your balance.")
        }
    }

    fun cancelActiveOrder(orderId: Long) {
        viewModelScope.launch {
            repository.cancelOrder(orderId)
            _activeOrderDetail.value = _activeOrderDetail.value?.copy(status = OrderStatus.FAILED)
            showToast("Order cancelled.")
        }
    }

    fun toggleWallet(walletId: Long, isBuy: Boolean, isSell: Boolean) {
        viewModelScope.launch {
            repository.toggleWalletStatus(walletId, isBuy, isSell)
            showToast("Wallet status updated!")
        }
    }

    fun addNewWallet(name: String, upiId: String, iconKey: String) {
        viewModelScope.launch {
            repository.addWallet(name, upiId, iconKey)
            _showAddWalletDialog.value = false
            showToast("UPI Wallet linked successfully!")
        }
    }

    fun claimDailyReward() {
        viewModelScope.launch {
            val amount = repository.claimDailyReward()
            _showDailyRewardDialog.value = false
            if (amount > 0) {
                showToast("Congratulations! ₹${amount.toInt()} Daily Check-in Bonus added to balance!")
            } else {
                showToast("Already claimed today's reward!")
            }
        }
    }

    fun claimNewbieBonus() {
        viewModelScope.launch {
            val amount = repository.claimNewbieBonus()
            _showNewbieDialog.value = false
            if (amount > 0) {
                showToast("Welcome Bonus of ₹${amount.toInt()} credited to your account!")
            } else {
                showToast("Newbie bonus already claimed!")
            }
        }
    }

    fun sendSupportMessage(msg: String) {
        if (msg.isBlank()) return
        viewModelScope.launch {
            repository.sendSupportMessage(msg)
        }
    }

    fun setDailyRewardDialog(show: Boolean) {
        _showDailyRewardDialog.value = show
    }

    fun setNewbieDialog(show: Boolean) {
        _showNewbieDialog.value = show
    }

    fun setAddWalletDialog(show: Boolean) {
        _showAddWalletDialog.value = show
    }

    fun setLanguageDialog(show: Boolean) {
        _showLanguageDialog.value = show
    }

    fun setLanguage(lang: String) {
        _currentLanguage.value = lang
        _showLanguageDialog.value = false
        showToast("Language changed to $lang")
    }

    fun setCustomBuyDialog(show: Boolean) {
        _showCustomBuyDialog.value = show
    }

    fun showToast(msg: String) {
        _snackbarMessage.value = msg
    }

    fun clearToast() {
        _snackbarMessage.value = null
    }
}
