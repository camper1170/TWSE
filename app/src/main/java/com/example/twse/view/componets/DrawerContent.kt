package com.example.twse.view.componets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twse.model.constants.FilterOptions

/**
 * DrawerContent:
 * 用於顯示篩選或排序選項的側邊抽屜內容。
 *
 * @param onApplyFilter 當用戶選擇排序方式時回調函數，參數為選定的篩選條件（String 類型）。
 * @param onDismiss 當抽屜被關閉時的回調函數。
 */
@Composable
fun DrawerContent(onApplyFilter: (FilterOptions) -> Unit, onDismiss: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("選擇排序方式")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onApplyFilter(FilterOptions.CODE_ASC)
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("依股票代號升序")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onApplyFilter(FilterOptions.CODE_DESC)
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("依股票代號降序")
        }
    }
}



