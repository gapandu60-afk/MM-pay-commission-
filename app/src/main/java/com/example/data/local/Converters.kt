package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.CurrencyType
import com.example.data.model.OrderStatus
import com.example.data.model.OrderType

class Converters {
    @TypeConverter
    fun fromOrderType(value: OrderType): String = value.name

    @TypeConverter
    fun toOrderType(value: String): OrderType = runCatching { OrderType.valueOf(value) }.getOrDefault(OrderType.BUY)

    @TypeConverter
    fun fromOrderStatus(value: OrderStatus): String = value.name

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus = runCatching { OrderStatus.valueOf(value) }.getOrDefault(OrderStatus.PAYING)

    @TypeConverter
    fun fromCurrencyType(value: CurrencyType): String = value.name

    @TypeConverter
    fun toCurrencyType(value: String): CurrencyType = runCatching { CurrencyType.valueOf(value) }.getOrDefault(CurrencyType.INR)
}
