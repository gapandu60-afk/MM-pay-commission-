package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.CurrencyType
import com.example.data.model.OrderStatus
import com.example.data.model.OrderType
import com.example.data.model.P2POrder
import com.example.data.model.UserProfile
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.MMPayTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun home_screen_screenshot() {
    val sampleUser = UserProfile(
      uid = "78068",
      balance = 0.00,
      todayEarn = 0.00,
      totalEarn = 12699.89
    )
    val sampleOrders = listOf(
      P2POrder(
        orderNo = "20260729233751130900252",
        orderType = OrderType.BUY,
        currency = CurrencyType.INR,
        amount = 950.0,
        status = OrderStatus.SUCCESS
      )
    )

    composeTestRule.setContent {
      MMPayTheme {
        HomeScreen(
          userProfile = sampleUser,
          orders = sampleOrders,
          currentLanguage = "English",
          onNavigate = {},
          onOpenDailyReward = {},
          onOpenNewbieReward = {},
          onOpenLanguage = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/home_screen.png")
  }
}
