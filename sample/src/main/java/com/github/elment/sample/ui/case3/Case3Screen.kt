package com.github.elment.sample.ui.case3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Case3Screen() {
    val viewModel: Case3ViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
//        containerColor = MaterialTheme.colorScheme.primary
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
            Text(text = "Timer 1 Value")
            Text(text = state.timer1Value)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { viewModel.accept(Case3Event.StartTimer1) }) {
                    Text(text = "Start Timer 1")
                }
                Button(onClick = { viewModel.accept(Case3Event.CancelTimer1) }) {
                    Text(text = "Cancel Timer 1")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Timer 2 Value")
            Text(text = state.timer2Value)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { viewModel.accept(Case3Event.StartTimer2) }) {
                    Text(text = "Start Timer 2")
                }
                Button(onClick = { viewModel.accept(Case3Event.CancelTimer2) }) {
                    Text(text = "Cancel Timer 2")
                }
            }
        }
    }
}