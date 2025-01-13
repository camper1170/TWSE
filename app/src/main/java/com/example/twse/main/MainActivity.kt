package com.example.twse.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.twse.dto.BwibbuAll
import com.example.twse.ui.theme.TWSETheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twse.R
import com.example.twse.dto.CombinedStockData
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TWSETheme {
                FilterScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(stockViewModel: StockViewModel = viewModel()) {
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier.fillMaxSize()
    ) {
        // 内容布局
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "股票證券",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    actions = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            content = {
                StockScreen()
            }
        )

        // BottomDrawer 和 scrim 手动控制
        if (drawerState.isOpen) {
            // Scrim 背景
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.32f))
                    .clickable {
                        coroutineScope.launch { drawerState.close() }
                    }
            )
        }

        // 抽屉内容
        BottomDrawer(
            drawerState = drawerState,
            gesturesEnabled = false,
            drawerContent = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                ) {
                    DrawerContent(
                        onApplyFilter = { filterOption ->
                            coroutineScope.launch {
                                stockViewModel.applyFilter(filterOption)
                                drawerState.close()
                            }
                        },
                        onDismiss = { coroutineScope.launch { drawerState.close() } }
                    )
                }
            },
            content = {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("股票證券") },
                            actions = {
                                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                                }
                            }
                        )
                    },
                    content = {
                        StockScreen()
                    }
                )
            }
        )
    }
}



@Composable
fun DrawerContent(onApplyFilter: (String) -> Unit, onDismiss: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("選擇排序方式")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onApplyFilter("CodeAsc"); onDismiss() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("依股票代號升序")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onApplyFilter("CodeDesc"); onDismiss() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("依股票代號降序")
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreen(stockViewModel: StockViewModel= viewModel()) {
    val bwibbuAll by stockViewModel.bwibbuAll.observeAsState(emptyList())
    val stockDayAvgAll by stockViewModel.stockDayAvgAll.observeAsState(emptyList())
    val stockDayAll by stockViewModel.stockDayAll.observeAsState(emptyList())
    var isFilterVisible by remember { mutableStateOf(false) }

    // 合併3支api數據
    val combinedData = bwibbuAll.map { stock ->
        CombinedStockData(
            bwibbuAll = stock,
            stockDayAvgAll = stockDayAvgAll.find { it.Code == stock.Code },
            stockDayAll = stockDayAll.find { it.Code == stock.Code }
        )
    }

    LaunchedEffect(Unit) {
        stockViewModel.fetchStockInfo()
        stockViewModel.fetchStockDayAvg()
        stockViewModel.fetchStockDayInfo()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "股票證券",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                        },
                actions = {
                    IconButton(onClick = { isFilterVisible = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        },
        content = { innerPadding ->
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (combinedData.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_nodata),
                                    contentDescription = "No Data",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .padding(bottom = 16.dp)
                                )
                                Text(
                                    text = "暫無數據",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    } else {
                        items(combinedData) { combinedStock ->
                            StockCard(combinedStock)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun StockCard(combinedStock: CombinedStockData) {
    val bwibbuAll = combinedStock.bwibbuAll
    val stockDayAvgAll = combinedStock.stockDayAvgAll
    val stockDayAll = combinedStock.stockDayAll
    var isDialogVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { isDialogVisible = true },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 股票代號與名稱
            Text(
                text = bwibbuAll.Code,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = bwibbuAll.Name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))
            // 開盤價與收盤價
            RowContent(
                label1 = "開盤價",
                value1 = stockDayAll?.OpeningPrice ?: "0.0",
                label2 = "收盤價",
                value2 = stockDayAvgAll?.ClosingPrice ?: "0.0",
                value2Color = if ((stockDayAvgAll?.closingPriceValue
                        ?: 0.0) > (stockDayAvgAll?.monthlyAvgPriceValue ?: 0.0)
                )
                    MaterialTheme.colorScheme.error else Color(0xFF008000)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 最高價與最低價
            RowContent(
                label1 = "最高價",
                value1 = stockDayAll?.HighestPrice ?: "0.0",
                label2 = "最低價",
                value2 = stockDayAll?.LowestPrice ?: "0.0"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 漲跌價差與月平均價
            RowContent(
                label1 = "漲跌價差",
                value1 = stockDayAll?.Change ?: "N/A",
                value1Color = if ((stockDayAll?.Change?.toDoubleOrNull() ?: 0.0) > 0)
                    MaterialTheme.colorScheme.error else Color(0xFF008000),
                label2 = "月平均價",
                value2 = stockDayAvgAll?.MonthlyAveragePrice ?: "0.0"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 成交筆數、成交股數、成交金額
            RowContent(
                label1 = "成交筆數",
                value1 = stockDayAll?.transactionFormatted ?: "0",
                label2 = "成交股數",
                value2 = stockDayAll?.tradeVolumeFormatted ?: "0",
                label3 = "成交金額",
                value3 = stockDayAll?.tradeValueFormatted ?: "0"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 點擊卡牌顯示dialog
            if (isDialogVisible) {
                StockInfoDialog(
                    stock = bwibbuAll,
                    onDismiss = { isDialogVisible = false }
                )
            }
        }
    }
}

@Composable
fun RowContent(
    label1: String,
    value1: String,
    label2: String? = null,
    value2: String? = null,
    value1Color: Color = MaterialTheme.colorScheme.onSurface,
    value2Color: Color = MaterialTheme.colorScheme.onSurface,
    label3: String? = null,
    value3: String? = null,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val textStyle = TextStyle(fontSize = 12.sp)
        val textStyle2 = TextStyle(fontSize = 14.sp)

        if (label3 != null && value3 != null) {
            // 第三欄存在，所有元素使用 textStyle
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$label1: ", style = textStyle, color = MaterialTheme.colorScheme.onSurface)
                Text(text = value1, style = textStyle, color = value1Color)
                Spacer(modifier = Modifier.width(5.dp))

                Text(text = "$label2: ", style = textStyle, color = MaterialTheme.colorScheme.onSurface)
                Text(text = value2 ?: "", style = textStyle, color = value1Color)
                Spacer(modifier = Modifier.width(5.dp))

                Text(text = "$label3: ", style = textStyle, color = MaterialTheme.colorScheme.onSurface)
                Text(text = value3, style = textStyle, color = value1Color)
            }
        } else {
            // 第一欄
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$label1: ",
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = value1,
                    style = textStyle2,
                    color = if (label1 == "開盤價") Color(0xFF008000) else value1Color
                )
            }

            // 第二欄
            if (label2 != null && value2 != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$label2: ",
                        style = textStyle,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = value2,
                        style = textStyle2,
                        color = value2Color
                    )
                }
            }
        }
    }
}


@Composable
fun StockInfoDialog(stock: BwibbuAll, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("確定")
            }
        },
        text = {
            Column {
                Text("本益比: ${stock.PEratioValue}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("殖利率(%): ${stock.DividendYieldValue} %")
                Spacer(modifier = Modifier.height(8.dp))
                Text("股價淨值比: ${stock.PBratio}")
            }
        }
    )
}




