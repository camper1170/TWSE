package com.example.twse.model.data

/**
 * StockDividendInfo:
 * 股票配息信息類，記錄股票的本益比、殖利率、股價淨值比等。
 *
 * @property Code 股票代號
 * @property Name 股票名稱
 * @property PEratio 本益比（預設值為 "0.0"）
 * @property DividendYield 殖利率（百分比，預設值為 "0.0"）
 * @property PBratio 股價淨值比
 * @property Change 漲跌價差（預設值為 "0.0"）
 * @property MonthlyAveragePrice 月平均價（預設值為 "0.0"）
 * @property ClosingPrice 收盤價（預設值為 "0.0"）
 */
data class StockDividendInfo(
    val Code: String, // 股票代號
    val Name: String, // 股票名稱
    val PEratio: String = "0.0", //本益比
    val DividendYield: String = "0.0", // 殖利率(%)
    val PBratio: String, // 股價淨值比
    val Change: String = "0.0", // 漲跌價差
    val MonthlyAveragePrice: String = "0.0", // 月平均價
    val ClosingPrice: String = "0.0" // 收盤價
) {
    /**
     * 本益比的數值形式。如果無法轉換為數值，則回傳 0.0。
     */
    val PEratioValue: Double
        get() = PEratio.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0

    /**
     * 殖利率的數值形式。如果無法轉換為數值，則回傳 0.0。
     */
    val DividendYieldValue: Double
        get() = DividendYield.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0
}
