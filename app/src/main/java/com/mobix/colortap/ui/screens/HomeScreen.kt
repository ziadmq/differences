package com.mobix.colortap.ui.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobix.colortap.viewmodel.GameViewModel

@Composable
fun HomeScreen(
    onStart: () -> Unit,
    vm: GameViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Color Tap", style = MaterialTheme.typography.displaySmall)
        Spacer(Modifier.height(10.dp))
        Text("Best Score: ${state.bestScore}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                // نبدأ اللعبة من شاشة GAME
                onStart()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Game")
        }
    }
}