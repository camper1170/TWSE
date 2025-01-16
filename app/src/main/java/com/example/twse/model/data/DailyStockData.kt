package com.example.twse.model.data

/**
 * DailyStockData:
 * 每日股票數據類，記錄股票的交易詳情，包括成交量、開盤價、收盤價等。
 *
 * @property Code 證券代號
 * @property Name 證券名稱
 * @property TradeVolume 成交股數
 * @property TradeValue 成交金額
 * @property OpeningPrice 開盤價
 * @property HighestPrice 最高價
 * @property LowestPrice 最低價
 * @property ClosingPrice 收盤價
 * @property Change 漲跌差價
 * @property Transaction 成交筆數
 */
data class DailyStockData(
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
    /**
     * 格式化的成交股數（帶千分位）。如果無法轉換為數值，則回傳 "N/A"。
     */
    val tradeVolumeFormatted: String
        get() = TradeVolume.toLongOrNull()?.let { "%,d".format(it) } ?: "N/A"

    /**
     * 格式化的成交金額（帶千分位）。如果無法轉換為數值，則回傳 "N/A"。
     */
    val tradeValueFormatted: String
        get() = TradeValue.toLongOrNull()?.let { "%,d".format(it) } ?: "N/A"

    /**
     * 格式化的成交筆數（帶千分位）。如果無法轉換為數值，則回傳 "N/A"。
     */
    val transactionFormatted: String
        get() = Transaction.toLongOrNull()?.let { "%,d".format(it) } ?: "N/A"
}