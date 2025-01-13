package com.example.twse.model.data

data class BwibbuAll(
    val Code: String, // 股票代號
    val Name: String, // 股票名稱
    val PEratio: String = "0.0", //本益比
    val DividendYield: String = "0.0", // 殖利率(%)
    val PBratio: String, // 股價淨值比
    val Change: String = "0.0", // 漲跌價差
    val MonthlyAveragePrice: String = "0.0", // 月平均價
    val ClosingPrice: String = "0.0" // 收盤價
) {
    val PEratioValue: Double
        get() = PEratio.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0

    val DividendYieldValue: Double
        get() = DividendYield.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0
}
