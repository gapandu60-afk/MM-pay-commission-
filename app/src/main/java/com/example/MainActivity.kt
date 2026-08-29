package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.components.AddWalletDialog
import com.example.ui.components.AppBottomBar
import com.example.ui.components.CustomBuyDialog
import com.example.ui.components.DailyRewardDialog
import com.example.ui.components.LanguageDialog
import com.example.ui.components.NewbieRewardDialog
import com.example.ui.screens.BalanceRecordsScreen
import com.example.ui.screens.BuyMarketScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InboxScreen
import com.example.ui.screens.OrderDetailScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RankingScreen
import com.example.ui.screens.SellMarketScreen
import com.example.ui.screens.ServiceScreen
import com.example.ui.screens.TeamScreen
import com.example.ui.screens.WalletListScreen
import com.example.ui.theme.MMPayTheme
import com.example.ui.viewmodel.AppNavTab
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.SubScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MMPayTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    val orders by viewModel.allOrders.collectAsState()
    val wallets by viewModel.wallets.collectAsState()
    val balanceRecords by viewModel.balanceRecords.collectAsState()
    val supportMessages by viewModel.supportMessages.collectAsState()
    val inboxMessages by viewModel.inboxMessages.collectAsState()

    val currentTab by viewModel.currentTab.collectAsState()
    val subScreenStack by viewModel.subScreenStack.collectAsState()
    val currentSubScreen = subScreenStack.lastOrNull() ?: SubScreen.NONE

    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val selectedTier by viewModel.selectedTier.collectAsState()
    val sortDirection by viewModel.sortDirection.collectAsState()
    val orderStatusFilter by viewModel.selectedOrderStatusFilter.collectAsState()
    val selectedPeriod by viewModel.selectedRankingPeriod.collectAsState()
    val activeOrderDetail by viewModel.activeOrderDetail.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    val showDailyRewardDialog by viewModel.showDailyRewardDialog.collectAsState()
    val showNewbieDialog by viewModel.showNewbieDialog.collectAsState()
    val showAddWalletDialog by viewModel.showAddWalletDialog.collectAsState()
    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
    val showCustomBuyDialog by viewModel.showCustomBuyDialog.collectAsState()
    val snackbarMsg by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    // Handle Android system back button
    BackHandler(enabled = currentSubScreen != SubScreen.NONE) {
        viewModel.popSubScreen()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (currentSubScreen == SubScreen.NONE) {
                AppBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            AnimatedContent(
                targetState = currentSubScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { subScreen ->
                when (subScreen) {
                    SubScreen.NONE -> {
                        when (currentTab) {
                            AppNavTab.HOME -> HomeScreen(
                                userProfile = userProfile,
                                orders = orders,
                                currentLanguage = currentLanguage,
                                onNavigate = { viewModel.navigateTo(it) },
                                onOpenDailyReward = { viewModel.setDailyRewardDialog(true) },
                                onOpenNewbieReward = { viewModel.setNewbieDialog(true) },
                                onOpenLanguage = { viewModel.setLanguageDialog(true) }
                            )
                            AppNavTab.ORDERS -> OrdersScreen(
                                orders = orders,
                                selectedCurrency = selectedCurrency,
                                selectedFilter = orderStatusFilter,
                                onSelectCurrency = { viewModel.setCurrency(it) },
                                onSelectFilter = { viewModel.setOrderStatusFilter(it) },
                                onOpenOrderDetail = { viewModel.openOrderDetail(it) },
                                onToast = { viewModel.showToast(it) }
                            )
                            AppNavTab.TEAM -> TeamScreen(
                                userProfile = userProfile,
                                teamMembers = viewModel.getTeamMembers(),
                                onViewRewardsDetail = { viewModel.showToast("Team commissions are settled automatically at 00:00 every day.") },
                                onToast = { viewModel.showToast(it) }
                            )
                            AppNavTab.RANKING -> RankingScreen(
                                userProfile = userProfile,
                                rankingItems = viewModel.getRankingEntries(),
                                selectedPeriod = selectedPeriod,
                                onSelectPeriod = { viewModel.setRankingPeriod(it) },
                                onOpenSupport = { viewModel.navigateTo(SubScreen.SERVICE_CHAT) }
                            )
                            AppNavTab.MY -> ProfileScreen(
                                userProfile = userProfile,
                                onNavigate = { viewModel.navigateTo(it) },
                                onOpenLanguage = { viewModel.setLanguageDialog(true) },
                                onToast = { viewModel.showToast(it) }
                            )
                        }
                    }
                    SubScreen.BUY_MARKET -> BuyMarketScreen(
                        orders = viewModel.getAvailableMarketOrders(),
                        selectedTier = selectedTier,
                        selectedCurrency = selectedCurrency,
                        sortDirection = sortDirection,
                        onBack = { viewModel.popSubScreen() },
                        onSelectTier = { viewModel.setTier(it) },
                        onSelectCurrency = { viewModel.setCurrency(it) },
                        onToggleSort = { viewModel.toggleSortDirection() },
                        onBuyOrder = { viewModel.grabOrderAndProceed(it) },
                        onOpenCustomBuy = { viewModel.setCustomBuyDialog(true) }
                    )
                    SubScreen.SELL_MARKET -> SellMarketScreen(
                        userProfile = userProfile,
                        wallets = wallets,
                        onBack = { viewModel.popSubScreen() },
                        onNavigateWallets = { viewModel.navigateTo(SubScreen.WALLET_LIST) },
                        onToast = { viewModel.showToast(it) }
                    )
                    SubScreen.WALLET_LIST -> WalletListScreen(
                        wallets = wallets,
                        onBack = { viewModel.popSubScreen() },
                        onToggleWallet = { id, buy, sell -> viewModel.toggleWallet(id, buy, sell) },
                        onOpenAddWallet = { viewModel.setAddWalletDialog(true) },
                        onToast = { viewModel.showToast(it) }
                    )
                    SubScreen.ORDER_DETAIL -> OrderDetailScreen(
                        order = activeOrderDetail,
                        onBack = { viewModel.popSubScreen() },
                        onSubmitUtr = { id, utr -> viewModel.submitUtrAndConfirm(id, utr) },
                        onCompleteOrder = { id -> viewModel.completeOrderDirectly(id) },
                        onCancelOrder = { id -> viewModel.cancelActiveOrder(id) },
                        onToast = { viewModel.showToast(it) }
                    )
                    SubScreen.BALANCE_RECORDS -> BalanceRecordsScreen(
                        userProfile = userProfile,
                        records = balanceRecords,
                        onBack = { viewModel.popSubScreen() }
                    )
                    SubScreen.SERVICE_CHAT -> ServiceScreen(
                        messages = supportMessages,
                        onBack = { viewModel.popSubScreen() },
                        onSendMessage = { viewModel.sendSupportMessage(it) }
                    )
                    SubScreen.INBOX -> InboxScreen(
                        messages = inboxMessages,
                        onBack = { viewModel.popSubScreen() }
                    )
                    SubScreen.TEAM_DETAILS -> TeamScreen(
                        userProfile = userProfile,
                        teamMembers = viewModel.getTeamMembers(),
                        onViewRewardsDetail = { viewModel.showToast("Team commissions are settled automatically at 00:00 every day.") },
                        onToast = { viewModel.showToast(it) }
                    )
                }
            }

            // Dialogs
            if (showDailyRewardDialog) {
                DailyRewardDialog(
                    onDismiss = { viewModel.setDailyRewardDialog(false) },
                    onClaim = { viewModel.claimDailyReward() },
                    alreadyClaimed = userProfile?.dailyCheckInDone == true
                )
            }

            if (showNewbieDialog) {
                NewbieRewardDialog(
                    onDismiss = { viewModel.setNewbieDialog(false) },
                    onClaim = { viewModel.claimNewbieBonus() },
                    alreadyClaimed = userProfile?.newbieRewardsClaimed == true
                )
            }

            if (showAddWalletDialog) {
                AddWalletDialog(
                    onDismiss = { viewModel.setAddWalletDialog(false) },
                    onAdd = { name, upi, icon -> viewModel.addNewWallet(name, upi, icon) }
                )
            }

            if (showCustomBuyDialog) {
                CustomBuyDialog(
                    onDismiss = { viewModel.setCustomBuyDialog(false) },
                    onCreate = { amt, ch -> viewModel.createCustomOrder(amt, ch) }
                )
            }

            if (showLanguageDialog) {
                LanguageDialog(
                    currentLanguage = currentLanguage,
                    onSelectLanguage = { viewModel.setLanguage(it) },
                    onDismiss = { viewModel.setLanguageDialog(false) }
                )
            }
        }
    }
}
