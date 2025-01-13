package com.example.twse.model.network

import com.example.twse.model.data.BwibbuAll
import com.example.twse.model.data.StockDayAll
import com.example.twse.model.data.StockDayAvgAll
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("v1/exchangeReport/BWIBBU_ALL")
    suspend fun getStockInfo(): Response<List<BwibbuAll>>

    @GET("v1/exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun getStockDayAvgAll(): Response<List<StockDayAvgAll>>

    @GET("v1/exchangeReport/STOCK_DAY_ALL")
    suspend fun getStockDayAll(): Response<List<StockDayAll>>
}
