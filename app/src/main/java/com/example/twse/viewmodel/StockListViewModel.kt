package com.example.twse.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.twse.model.constants.FilterOptions
import com.example.twse.model.data.StockDividendInfo
import com.example.twse.model.data.AggregatedStockData
import com.example.twse.model.data.DailyStockData
import com.example.twse.model.data.DailyStockAverage
import com.example.twse.model.network.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * StockListViewModel:
 * 負責管理股票數據的 ViewModel，包含數據獲取、整合及篩選的功能。
 */
class StockListViewModel : ViewModel() {

    companion object {
        private const val TAG = "StockListViewModel" // 錯誤日誌標籤
    }

    // LiveData，用於存放股票相關數據
    private val _stockDividendList = MutableLiveData<List<StockDividendInfo>>() // 股票配息資訊
    private val _dailyStockAverageList = MutableLiveData<List<DailyStockAverage>>() // 每日平均股票數據
    private val _dailyStockDetailList = MutableLiveData<List<DailyStockData>>() // 每日詳細股票數據
    private val _aggregatedStockList = MutableLiveData<List<AggregatedStockData>>() // 聚合後的股票數據

    val aggregatedStockList: LiveData<List<AggregatedStockData>> get() = _aggregatedStockList

    /**
     * fetchAllStockData:
     * 從 API 獲取股票的所有相關數據（配息資訊、每日平均數據、每日詳細數據），並進行整合。
     */
    fun fetchAllStockData() {
        viewModelScope.launch {
            try {
                // 並行加載數據
                val stockDividendData = async { fetchStockDividendInfo() }
                val dailyStockAverageData = async { fetchDailyStockAverage() }
                val dailyStockDetailData = async { fetchDailyStockDetails() }

                // 獲取數據結果
                val stockDividendResult = stockDividendData.await()
                val dailyStockAverageResult = dailyStockAverageData.await()
                val dailyStockDetailResult = dailyStockDetailData.await()

                // 檢查數據是否加載成功
                Log.d(TAG, "Stock Dividend Data Loaded: $stockDividendResult")
                Log.d(TAG, "Daily Stock Average Loaded: $dailyStockAverageResult")
                Log.d(TAG, "Daily Stock Details Loaded: $dailyStockDetailResult")

                // 更新 LiveData
                _stockDividendList.value = stockDividendResult
                _dailyStockAverageList.value = dailyStockAverageResult
                _dailyStockDetailList.value = dailyStockDetailResult

                // 整合數據
                aggregateStockData(
                    stockDividendResult,
                    dailyStockAverageResult,
                    dailyStockDetailResult
                )

            } catch (e: Exception) {
                Log.e(TAG, "Error fetching stock data: ${e.message}", e)
            }
        }
    }


    /**
     * aggregateStockData:
     * 將股票配息資訊、每日平均數據和每日詳細數據進行整合。
     */
    private fun aggregateStockData(
        stockDividendList: List<StockDividendInfo>,
        dailyAverageList: List<DailyStockAverage>,
        dailyDetailList: List<DailyStockData>
    ) {
        viewModelScope.launch {
            // 將三類數據整合為聚合數據
            val aggregatedData = stockDividendList.map { stock ->
                AggregatedStockData(
                    stockDividendInfo = stock,
                    dailyStockAverage = dailyAverageList.find { it.Code == stock.Code },
                    dailyStockData = dailyDetailList.find { it.Code == stock.Code }
                )
            }
            _aggregatedStockList.postValue(aggregatedData.sortedByDescending { it.dailyStockData?.Code }) // 更新聚合後的數據
        }
    }

    /**
     * fetchStockDividendInfo:
     * 獲取股票配息資訊數據，並處理可能的空值情況。
     */
    suspend fun fetchStockDividendInfo(): List<StockDividendInfo> {
        return try {
            val response = RetrofitClient.apiService.getStockInfo()
            if (response.isSuccessful) {
                val data = response.body()?.map {
                    it.copy(
                        PEratio = it.PEratio.takeIf { !it.isNullOrBlank() } ?: "0.0",
                        DividendYield = it.DividendYield.takeIf { !it.isNullOrBlank() } ?: "0.0",
                        Change = it.Change.takeIf { !it.isNullOrBlank() } ?: "0.0",
                        MonthlyAveragePrice = it.MonthlyAveragePrice.takeIf { !it.isNullOrBlank() }
                            ?: "0.0",
                        ClosingPrice = it.ClosingPrice.takeIf { !it.isNullOrBlank() } ?: "0.0"
                    )
                } ?: emptyList()
                Log.d(TAG, "fetchStockDividendInfo Success: $data") // 日誌檢查數據
                data
            } else {
                Log.e(TAG, "Failed to fetch stock dividend info: ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching stock dividend info: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * fetchDailyStockAverage:
     * 獲取每日股票平均數據，並處理可能的空值情況。
     */
    private suspend fun fetchDailyStockAverage(): List<DailyStockAverage> {
        return try {
            val response = RetrofitClient.apiService.getStockDayAvgAll()
            if (response.isSuccessful) {
                response.body()?.map {
                    it.copy(
                        ClosingPrice = it.ClosingPrice.takeIf { !it.isNullOrBlank() } ?: "0.0",
                        MonthlyAveragePrice = it.MonthlyAveragePrice.takeIf { !it.isNullOrBlank() }
                            ?: "0.0"
                    )
                } ?: emptyList()
            } else {
                Log.e(TAG, "Failed to fetch daily stock average: ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching daily stock average: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * fetchDailyStockDetails:
     * 獲取每日股票詳細數據。
     */
    private suspend fun fetchDailyStockDetails(): List<DailyStockData> {
        return try {
            val response = RetrofitClient.apiService.getStockDayAll()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e(TAG, "Failed to fetch daily stock details: ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching daily stock details: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * applyStockFilter:
     * 根據指定的篩選條件對股票數據進行排序，並更新整合數據。
     *
     * @param filterOption 篩選條件（FilterOptions 枚舉類型）。
     */
    fun applyStockFilter(filterOption: String) {
        Log.d(TAG, "applyStockFilter called with option: $filterOption")

        if (_stockDividendList.value == null) {
            Log.e(TAG, "Cannot apply filter: _stockDividendList is null")
            return
        }

        val selectedFilter = FilterOptions.fromValue(filterOption)
        if (selectedFilter != null) {
            val sortedStockDividendList = when (selectedFilter) {
                FilterOptions.CODE_ASC -> _stockDividendList.value?.filter { it.Code != null }
                    ?.sortedBy { it.Code }

                FilterOptions.CODE_DESC -> _stockDividendList.value?.filter { it.Code != null }
                    ?.sortedByDescending { it.Code }
            } ?: emptyList()

            if (sortedStockDividendList.isEmpty()) {
                Log.w(TAG, "No data after applying filter: $filterOption")
            } else {
                Log.d(TAG, "Filtered Data: $sortedStockDividendList")
            }

            val dailyAverageList = _dailyStockAverageList.value ?: emptyList()
            val dailyDetailList = _dailyStockDetailList.value ?: emptyList()

            val aggregatedData = sortedStockDividendList.map { stock ->
                AggregatedStockData(
                    stockDividendInfo = stock,
                    dailyStockAverage = dailyAverageList.find { it.Code == stock.Code },
                    dailyStockData = dailyDetailList.find { it.Code == stock.Code }
                )
            }

            // 強制觸發 LiveData 更新
            _aggregatedStockList.value = null
            _aggregatedStockList.value = aggregatedData

            Log.d(TAG, "Updated Aggregated Data: $aggregatedData")
        } else {
            Log.e(TAG, "Invalid filter option: $filterOption")
        }
    }
}
