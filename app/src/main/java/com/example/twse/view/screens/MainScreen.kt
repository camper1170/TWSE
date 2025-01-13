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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twse.view.componets.DrawerContent
import com.example.twse.viewmodel.StockViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(stockViewModel: StockViewModel = viewModel()) {
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier.fillMaxSize()
    ) {
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
                        StockListScreen()
                    }
                )
            }
        )
    }
}
