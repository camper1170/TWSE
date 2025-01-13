package com.example.twse.service

import com.example.twse.dto.BwibbuAll
import com.example.twse.dto.StockDayAll
import com.example.twse.dto.StockDayAvgAll
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
