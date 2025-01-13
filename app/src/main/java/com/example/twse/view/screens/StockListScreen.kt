package com.example.twse.view.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twse.R
import com.example.twse.view.componets.StockCard
import com.example.twse.viewmodel.StockViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(stockViewModel: StockViewModel = viewModel()) {

    var isFilterVisible by remember { mutableStateOf(false) }
    val combinedData by stockViewModel.combinedData.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        stockViewModel.fetchAllData()
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
