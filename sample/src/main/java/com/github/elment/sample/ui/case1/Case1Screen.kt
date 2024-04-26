package com.github.elment.sample.ui.case1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.elment.android.acceptDebounce
import com.github.elment.android.acceptDropOldest
import com.github.elment.android.acceptThrottle
import com.github.elment.core.store.AcceptMode
import com.github.elment.sample.ui.case1.Case1Event.Decrease
import com.github.elment.sample.ui.case1.Case1Event.Increase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Case1Screen() {
    val viewModel: Case1ViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
//        containerColor = MaterialTheme.colorScheme.primary
        topBar = {
            TopAppBar(title = { Text(text = state.counterText) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Default Accept Mode")
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                IconButton(onClick = { viewModel.accept(Increase) }) {
                    Icon(Icons.Outlined.Add, contentDescription = "")
                }
                IconButton(onClick = { viewModel.accept(Decrease) }) {
                    Icon(Icons.Outlined.Remove, contentDescription = "")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Debounce Accept Mode")
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                IconButton(onClick = { viewModel.accept(Increase, AcceptMode.DEBOUNCE) }) {
                    Icon(Icons.Outlined.Add, contentDescription = "")
                }
                IconButton(onClick = { viewModel.acceptDebounce(Decrease) }) {
                    Icon(Icons.Outlined.Remove, contentDescription = "")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Throttle Accept Mode")
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                IconButton(onClick = { viewModel.accept(Increase, AcceptMode.THROTTLE) }) {
                    Icon(Icons.Outlined.Add, contentDescription = "")
                }
                IconButton(onClick = { viewModel.acceptThrottle(Decrease) }) {
                    Icon(Icons.Outlined.Remove, contentDescription = "")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Drop Oldest Accept Mode")
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                IconButton(onClick = { viewModel.accept(Increase, AcceptMode.DROP_OLDEST) }) {
                    Icon(Icons.Outlined.Add, contentDescription = "")
                }
                IconButton(onClick = { viewModel.acceptDropOldest(Decrease) }) {
                    Icon(Icons.Outlined.Remove, contentDescription = "")
                }
            }
        }
    }
}