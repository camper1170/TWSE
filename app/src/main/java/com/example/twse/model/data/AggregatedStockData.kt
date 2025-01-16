package com.example.twse.model.data

/**
 * AggregatedStockData:
 * 聚合的股票數據類，包含股票配息信息、每日平均信息及每日股票數據。
 *
 * @property stockDividendInfo 股票配息信息
 * @property dailyStockAverage 每日平均股票信息（可選）
 * @property dailyStockData 每日詳細股票數據（可選）
 */
data class AggregatedStockData(
    val stockDividendInfo: StockDividendInfo,
    val dailyStockAverage: DailyStockAverage?,
    val dailyStockData: DailyStockData?
)

