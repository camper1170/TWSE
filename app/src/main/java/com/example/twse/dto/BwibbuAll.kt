package com.example.twse.dto

data class BwibbuAll(
    val Code: String,                // 股票代號
    val Name: String,                // 股票名稱
    val PEratio: String = "0.0",     // 本益比，默認值為 "0.0"
    val DividendYield: String = "0.0", // 殖利率(%)，默認值為 "0.0"
    val PBratio: String,             // 股價淨值比
    val Change: String = "0.0",      // 漲跌價差，默認值為 "0.0"
    val MonthlyAveragePrice: String = "0.0", // 月平均價，默認值為 "0.0"
    val ClosingPrice: String = "0.0"         // 收盤價，默認值為 "0.0"
) {
    // 計算屬性，安全處理空值或無效值
    val PEratioValue: Double
        get() = PEratio.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0

    val DividendYieldValue: Double
        get() = DividendYield.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0

    val changeValue: Double
        get() = Change.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0

    val monthlyAvgPriceValue: Double
        get() = MonthlyAveragePrice.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0

    val closingPriceValue: Double
        get() = ClosingPrice.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0
}
