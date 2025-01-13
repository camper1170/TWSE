package com.example.twse.view.componets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


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
