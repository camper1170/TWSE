package com.example.twse.main

import androidx.lifecycle.*
import com.example.twse.dto.BwibbuAll
import com.example.twse.dto.CombinedStockData
import com.example.twse.dto.StockDayAll
import com.example.twse.dto.StockDayAvgAll
import com.example.twse.service.RetrofitClient
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {

    private val _sortedStocks = MutableLiveData<List<CombinedStockData>>()
    val sortedStocks: LiveData<List<CombinedStockData>> = _sortedStocks


    private val _bwibbuAll = MutableLiveData<List<BwibbuAll>>()
    val bwibbuAll: LiveData<List<BwibbuAll>> get() = _bwibbuAll

    private val _stockDayAvgAll = MutableLiveData<List<StockDayAvgAll>>()
    val stockDayAvgAll: LiveData<List<StockDayAvgAll>> get() = _stockDayAvgAll

    private val _stockDayAll = MutableLiveData<List<StockDayAll>>()
    val stockDayAll: LiveData<List<StockDayAll>> get() = _stockDayAll

    fun fetchStockInfo() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getStockInfo()
                if (response.isSuccessful) {
                    _bwibbuAll.postValue(response.body()?.map {
                        it.copy(
                            PEratio = it.PEratio.takeIf { !it.isNullOrBlank() } ?: "0.0",
                            DividendYield = it.DividendYield.takeIf { !it.isNullOrBlank() } ?: "0.0",
                            Change = it.Change.takeIf { !it.isNullOrBlank() } ?: "0.0",
                            MonthlyAveragePrice = it.MonthlyAveragePrice.takeIf { !it.isNullOrBlank() } ?: "0.0",
                            ClosingPrice = it.ClosingPrice.takeIf { !it.isNullOrBlank() } ?: "0.0"
                        )
                    } ?: emptyList())
                } else {
                    _bwibbuAll.postValue(emptyList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _bwibbuAll.postValue(emptyList())
            }
        }
    }

    fun applyFilter(filterOption: String) {
        val sortedList = when (filterOption) {
            "CodeAsc" -> _bwibbuAll.value?.sortedBy { it.Code }
            "CodeDesc" -> _bwibbuAll.value?.sortedByDescending { it.Code }
            else -> _bwibbuAll.value
        }
        _bwibbuAll.value = sortedList
    }

    fun fetchStockDayAvg() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getStockDayAvgAll()
                if (response.isSuccessful) {
                    _stockDayAvgAll.postValue(response.body()?.map {
                        it.copy(
                            ClosingPrice = it.ClosingPrice.takeIf { !it.isNullOrBlank() } ?: "0.0",
                            MonthlyAveragePrice = it.MonthlyAveragePrice.takeIf { !it.isNullOrBlank() } ?: "0.0"
                        )
                    } ?: emptyList())
                } else {
                    _stockDayAvgAll.postValue(emptyList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _stockDayAvgAll.postValue(emptyList())
            }
        }
    }

    fun fetchStockDayInfo() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getStockDayAll()
                if (response.isSuccessful) {
                    _stockDayAll.postValue(response.body())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

