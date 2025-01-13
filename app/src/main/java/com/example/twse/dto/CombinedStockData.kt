package com.example.twse.dto

data class CombinedStockData(
    val bwibbuAll: BwibbuAll,
    val stockDayAvgAll: StockDayAvgAll?,
    val stockDayAll: StockDayAll?
)

