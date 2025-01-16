package com.example.twse.model.data

/**
 * DailyStockAverage:
 * 每日股票平均數據類，記錄某股票的收盤價與月平均價等。
 *
 * @property Code 股票代號
 * @property Name 股票名稱
 * @property ClosingPrice 收盤價（預設值為 "0.0"）
 * @property MonthlyAveragePrice 月平均價（預設值為 "0.0"）
 */
data class DailyStockAverage(
    val Code: String, // 股票代號
    val Name: String, // 股票名稱
    val ClosingPrice: String = "0.0", // 收盤價
    val MonthlyAveragePrice: String = "0.0" // 月平均價
) {
    /**
     * 收盤價的數值形式。如果無法轉換為數值，則回傳 0.0。
     */
    val closingPriceValue: Double
        get() = ClosingPrice.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0

    /**
     * 月平均價的數值形式。如果無法轉換為數值，則回傳 0.0。
     */
    val monthlyAvgPriceValue: Double
        get() = MonthlyAveragePrice.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0
}
