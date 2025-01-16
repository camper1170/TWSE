package com.example.twse.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twse.R
import com.example.twse.model.data.AggregatedStockData
import com.example.twse.model.data.StockDividendInfo
import com.example.twse.view.componets.StockCard
import com.example.twse.viewmodel.StockListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ScreenState {
    LOADING,
    SUCCESS,
    EMPTY
}

/**
 * StockListScreen:
 * 顯示股票清單的畫面，包含 Loading 狀態、數據列表或無數據提示。
 *
 * @param stockListViewModel 用於獲取和操作股票數據的 ViewModel。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(stockListViewModel: StockListViewModel = viewModel()) {
    var screenState by remember { mutableStateOf(ScreenState.LOADING) }
    val combinedData by stockListViewModel.aggregatedStockList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        screenState = ScreenState.LOADING
        val timeoutJob = launch {
            delay(5000)
            if (combinedData.isEmpty()) {
                screenState = ScreenState.EMPTY
            } else {
                screenState = ScreenState.SUCCESS
            }
        }

        try {
            stockListViewModel.fetchAllStockData()
            if (combinedData.isNotEmpty()) {
                timeoutJob.cancel()
                screenState = ScreenState.SUCCESS
            }
        } catch (e: Exception) {
            handleError(e)
            screenState = ScreenState.EMPTY
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("股票證券", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { Log.d("Filter", "Filter button clicked!") }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        },
        content = { paddingValues ->
            StockListContent(
                combinedData = combinedData,
                screenState = screenState,
                paddingValues = paddingValues
            )
        }
    )
}

/**
 * StockListContent:
 * 根據不同的狀態顯示數據加載、無數據或數據列表。
 *
 * @param combinedData 數據列表
 * @param screenState 當前畫面狀態
 * @param paddingValues 外部填充
 */
@Composable
fun StockListContent(
    combinedData: List<AggregatedStockData>,
    screenState: ScreenState,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (screenState) {
            ScreenState.LOADING -> {
                item { LoadingView(R.string.loading_message) }
            }

            ScreenState.EMPTY -> {
                item { NoDataView(R.string.empty_name, R.drawable.ic_nodata) }
            }

            ScreenState.SUCCESS -> {
                if (combinedData.isEmpty()) {
                    item { NoDataView(R.string.temporary_no_data_message, R.drawable.ic_nodata) }
                } else {
                    items(combinedData) { combinedStock ->
                        StockCard(combinedStock)
                    }
                }
            }
        }
    }
}

/**
 * LoadingView:
 * 顯示 Loading 動畫和提示訊息。
 *
 * @param messageRes 資源 ID
 */
@Composable
fun LoadingView(messageRes: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
        Text(
            text = stringResource(id = messageRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

/**
 * NoDataView:
 * 顯示無數據提示與圖示。
 *
 * @param messageRes 資源 ID
 * @param iconRes 資源 ID
 */
@Composable
fun NoDataView(messageRes: Int, iconRes: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "No Data Icon",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(id = messageRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

/**
 * handleError:
 * 處理錯誤並記錄日誌。
 *
 * @param exception 發生的異常
 */
fun handleError(exception: Exception) {
    Log.e("StockListScreen", "Error occurred: ${exception.localizedMessage}")
}

/**
 * 預覽 Loading 狀態
 */
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewLoadingView() {
    StockListContent(
        combinedData = emptyList(),
        screenState = ScreenState.LOADING,
        paddingValues = PaddingValues(16.dp)
    )
}

/**
 * 預覽無數據狀態
 */
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewEmptyView() {
    StockListContent(
        combinedData = emptyList(),
        screenState = ScreenState.EMPTY,
        paddingValues = PaddingValues(16.dp)
    )
}

/**
 * 預覽數據列表狀態
 */
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewDataView() {
    StockListContent(
        combinedData = listOf(
            AggregatedStockData(
                stockDividendInfo = StockDividendInfo(
                    Code = "2330",
                    Name = "台積電",
                    DividendYield = "3.2",
                    PEratio = "15.5",
                    Change = "0.5",
                    MonthlyAveragePrice = "600.0",
                    ClosingPrice = "610.0",
                    PBratio = "38.8"
                ),
                dailyStockAverage = null,
                dailyStockData = null
            )
        ),
        screenState = ScreenState.SUCCESS,
        paddingValues = PaddingValues(16.dp)
    )
}
