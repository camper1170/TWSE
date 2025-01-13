package com.example.twse.dto

data class StockDayAll(
    val Code: String, // 證券代號
    val Name: String, // 證券名稱
    val TradeVolume: String, // 成交股數
    val TradeValue: String, // 成交金額
    val OpeningPrice: String, // 開盤價
    val HighestPrice: String, // 最高價
    val LowestPrice: String, // 最低價
    val ClosingPrice: String, // 收盤
    val Change: String, // 漲跌差價
    val Transaction: String // 成交筆數
) {
    // 轉換千分位
    val tradeVolumeFormatted: String
        get() = TradeVolume.toLongOrNull()?.let { "%,d".format(it) } ?: "N/A"

    val tradeValueFormatted: String
        get() = TradeValue.toLongOrNull()?.let { "%,d".format(it) } ?: "N/A"

    val transactionFormatted: String
        get() = Transaction.toLongOrNull()?.let { "%,d".format(it) } ?: "N/A"

    val changeValue: Double
        get() = Change.toDoubleOrNull() ?: 0.0
}