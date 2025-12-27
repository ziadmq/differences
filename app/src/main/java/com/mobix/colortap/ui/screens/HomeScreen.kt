package com.mobix.colortap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobix.colortap.viewmodel.GameViewModel
import com.mobix.colortap.ui.theme.BackgroundDark

@Composable
fun HomeScreen(onStart: () -> Unit, vm: GameViewModel = viewModel()) {
    val state by vm.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("COLOR TAP", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color.White)
            Text("FAST REFLEXES ONLY", color = Color.Gray, letterSpacing = 2.sp)

            Spacer(Modifier.height(50.dp))
            Text("BEST SCORE: ${state.bestScore}", color = Color(0xFFFFCC00), fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(40.dp))
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(65.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
            ) {
                Text("START GAME", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    }
}