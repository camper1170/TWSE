package com.example.twse.view.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.twse.model.data.StockDividendInfo
import com.example.twse.model.data.AggregatedStockData

/**
 * StockCard:
 * 用於顯示股票相關數據的卡片組件，支持點擊顯示詳細資訊。
 *
 * @param aggregatedStockData 聚合的股票數據，包括配息資訊、平均資訊、每日數據。
 */
@Composable
fun StockCard(aggregatedStockData: AggregatedStockData) {
    val bwibbuAll = aggregatedStockData.stockDividendInfo
    val stockDayAvgAll = aggregatedStockData.dailyStockAverage
    val stockDayAll = aggregatedStockData.dailyStockData
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


/**
 * RowContent:
 * 用於顯示數據的行組件，支持最多三列數據。
 */
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
                Text(
                    text = "$label1: ",
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(text = value1, style = textStyle, color = value1Color)
                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "$label2: ",
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(text = value2 ?: "", style = textStyle, color = value1Color)
                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "$label3: ",
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onSurface
                )
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



/**
 * StockInfoDialog:
 * 顯示股票詳細資訊的對話框。
 *
 * @param stock 股票配息資訊
 * @param onDismiss 當對話框關閉時的回調函數。
 */
@Composable
fun StockInfoDialog(stock: StockDividendInfo, onDismiss: () -> Unit) {
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


