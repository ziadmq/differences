package com.mobix.colortap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobix.colortap.viewmodel.GameViewModel

@Composable
fun ResultScreen(
    score: Int,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
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
        Text("Game Over", style = MaterialTheme.typography.displaySmall)
        Spacer(Modifier.height(12.dp))
        Text("Your Score: $score", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Best Score: ${state.bestScore}", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onPlayAgain,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Play Again")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back Home")
        }
    }
}