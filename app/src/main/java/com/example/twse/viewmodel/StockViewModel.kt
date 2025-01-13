package com.example.twse.viewmodel

import androidx.lifecycle.*
import com.example.twse.model.data.BwibbuAll
import com.example.twse.model.data.CombinedStockData
import com.example.twse.model.data.StockDayAll
import com.example.twse.model.data.StockDayAvgAll
import com.example.twse.model.network.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {

    private val _bwibbuAll = MutableLiveData<List<BwibbuAll>>()
    private val _stockDayAvgAll = MutableLiveData<List<StockDayAvgAll>>()
    private val _stockDayAll = MutableLiveData<List<StockDayAll>>()
    private val _combinedData = MutableLiveData<List<CombinedStockData>>()
    val combinedData: LiveData<List<CombinedStockData>> get() = _combinedData

    fun fetchAllData() {
        viewModelScope.launch {
            val bwibbuAllData = async { fetchStockInfo() }
            val stockDayAvgData = async { fetchStockDayAvg() }
            val stockDayAllData = async { fetchStockDayInfo() }

            val bwibbuAllResult = bwibbuAllData.await()
            val stockDayAvgResult = stockDayAvgData.await()
            val stockDayAllResult = stockDayAllData.await()

            combineData(bwibbuAllResult, stockDayAvgResult, stockDayAllResult)
        }
    }

    private fun combineData(
        bwibbuAll: List<BwibbuAll>,
        stockDayAvgAll: List<StockDayAvgAll>,
        stockDayAll: List<StockDayAll>
    ) {
        val combined = bwibbuAll.map { stock ->
            CombinedStockData(
                bwibbuAll = stock,
                stockDayAvgAll = stockDayAvgAll.find { it.Code == stock.Code },
                stockDayAll = stockDayAll.find { it.Code == stock.Code }
            )
        }
        _combinedData.postValue(combined)
    }

    suspend fun fetchStockInfo(): List<BwibbuAll> {
        return try {
            val response = RetrofitClient.apiService.getStockInfo()
            if (response.isSuccessful) {
                val data = response.body()?.map {
                    it.copy(
                        PEratio = it.PEratio.takeIf { !it.isNullOrBlank() } ?: "0.0",
                        DividendYield = it.DividendYield.takeIf { !it.isNullOrBlank() } ?: "0.0",
                        Change = it.Change.takeIf { !it.isNullOrBlank() } ?: "0.0",
                        MonthlyAveragePrice = it.MonthlyAveragePrice.takeIf { !it.isNullOrBlank() } ?: "0.0",
                        ClosingPrice = it.ClosingPrice.takeIf { !it.isNullOrBlank() } ?: "0.0"
                    )
                } ?: emptyList()
                _bwibbuAll.postValue(data)
                data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun fetchStockDayAvg(): List<StockDayAvgAll> {
        return try {
            val response = RetrofitClient.apiService.getStockDayAvgAll()
            if (response.isSuccessful) {
                val data = response.body()?.map {
                    it.copy(
                        ClosingPrice = it.ClosingPrice.takeIf { !it.isNullOrBlank() } ?: "0.0",
                        MonthlyAveragePrice = it.MonthlyAveragePrice.takeIf { !it.isNullOrBlank() } ?: "0.0"
                    )
                } ?: emptyList()
                _stockDayAvgAll.postValue(data)
                data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun fetchStockDayInfo(): List<StockDayAll> {
        return try {
            val response = RetrofitClient.apiService.getStockDayAll()
            if (response.isSuccessful) {
                val data = response.body() ?: emptyList()
                _stockDayAll.postValue(data)
                data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun applyFilter(filterOption: String) {
        val sortedBwibbuAll = when (filterOption) {
            "CodeAsc" -> _bwibbuAll.value?.sortedBy { it.Code }
            "CodeDesc" -> _bwibbuAll.value?.sortedByDescending { it.Code }
            else -> _bwibbuAll.value
        } ?: emptyList()

        // 更新 _bwibbuAll
        _bwibbuAll.value = sortedBwibbuAll

        val stockDayAvgAll = _stockDayAvgAll.value ?: emptyList()
        val stockDayAll = _stockDayAll.value ?: emptyList()

        val combined = sortedBwibbuAll.map { stock ->
            CombinedStockData(
                bwibbuAll = stock,
                stockDayAvgAll = stockDayAvgAll.find { it.Code == stock.Code },
                stockDayAll = stockDayAll.find { it.Code == stock.Code }
            )
        }

        _combinedData.value = combined
    }

}

