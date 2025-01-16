package com.example.twse.view.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twse.R
import com.example.twse.view.componets.DrawerContent
import com.example.twse.viewmodel.StockListViewModel
import kotlinx.coroutines.launch

/**
 * MainScreen:
 * 主畫面組件，包含頂部工具列、股票清單畫面，以及底部抽屜用於篩選功能。
 *
 * @param stockListViewModel 用於獲取和操作股票數據的 ViewModel。
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(stockListViewModel: StockListViewModel = viewModel()) {
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier.fillMaxSize()
    ) {
        // Scaffold 用於提供工具列和主內容區域
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.title),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    actions = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            content = {
                StockListScreen()
            }
        )

        // 當抽屜打開時，顯示背景遮罩，點擊時可關閉抽屜
        if (drawerState.isOpen) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.32f))
                    .clickable {
                        coroutineScope.launch { drawerState.close() }
                    }
            )
        }

        // 底部抽屜，用於篩選選項
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
                        onApplyFilter = { selectedFilter ->
                            coroutineScope.launch {
                                stockListViewModel.applyStockFilter(selectedFilter.toString())
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
                            title = { Text(stringResource(R.string.title)) },
                            actions = {
                                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                                }
                            }
                        )
                    },
                    content = {
                        StockListScreen()
                    }
                )
            }
        )
    }
}
