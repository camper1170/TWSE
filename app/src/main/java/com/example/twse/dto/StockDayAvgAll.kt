package com.example.twse.dto

data class StockDayAvgAll(
    val Code: String, // 股票代號
    val Name: String, // 股票名稱
    val ClosingPrice: String = "0.0", // 收盤價
    val MonthlyAveragePrice: String = "0.0" // 月平均價
) {
    val closingPriceValue: Double
        get() = ClosingPrice.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0

    val monthlyAvgPriceValue: Double
        get() = MonthlyAveragePrice.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0
}
