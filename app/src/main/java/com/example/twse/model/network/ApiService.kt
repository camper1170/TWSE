package com.example.twse.model.network

import com.example.twse.model.data.StockDividendInfo
import com.example.twse.model.data.DailyStockData
import com.example.twse.model.data.DailyStockAverage
import retrofit2.Response
import retrofit2.http.GET

/**
 * ApiService:
 * 定義 API 請求的介面。
 */
interface ApiService {
    /**
     * 獲取所有股票配息資訊 (BWIBBU_ALL)
     */
    @GET("v1/exchangeReport/BWIBBU_ALL")
    suspend fun getStockInfo(): Response<List<StockDividendInfo>>

    /**
     * 獲取所有每日平均股票資訊 (STOCK_DAY_AVG_ALL)
     */
    @GET("v1/exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun getStockDayAvgAll(): Response<List<DailyStockAverage>>

    /**
     * 獲取所有每日股票數據 (STOCK_DAY_ALL)
     */
    @GET("v1/exchangeReport/STOCK_DAY_ALL")
    suspend fun getStockDayAll(): Response<List<DailyStockData>>
}
