package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.Converters
import com.example.data.model.CurrencyType
import com.example.data.model.OrderStatus
import com.example.data.model.OrderType
import com.example.data.model.TierCategory
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `verify app name resource`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("MM Pay", appName)
  }

  @Test
  fun `verify tier calculation logic`() {
    val smallAmount = 964.0
    val smallIncome = (smallAmount * (TierCategory.SMALL.ratePercent / 100.0)) + TierCategory.SMALL.bonusFlat
    assertEquals(33.956, smallIncome, 0.01)

    val mediumAmount = 1000.0
    val mediumIncome = (mediumAmount * (TierCategory.MEDIUM.ratePercent / 100.0)) + TierCategory.MEDIUM.bonusFlat
    assertEquals(38.0, mediumIncome, 0.01)
  }

  @Test
  fun `verify room converters`() {
    val converters = Converters()
    assertEquals(OrderStatus.SUCCESS, converters.toOrderStatus("SUCCESS"))
    assertEquals(OrderType.BUY, converters.toOrderType("BUY"))
    assertEquals(CurrencyType.INR, converters.toCurrencyType("INR"))
  }
}
