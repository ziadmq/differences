package com.mobix.differences.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobix.differences.data.repo.LevelsRepository

@Composable
fun HomeScreen(onStartLevel: (String) -> Unit) {
    val levels = LevelsRepository.getAllLevels()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("لعبة اكتشف الخطأ", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        levels.forEach { level ->
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Row(
                    Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(level.title, style = MaterialTheme.typography.titleMedium)
                        Text("عدد الفروقات: ${level.regions.size}")
                        Text("الوقت: ${level.timeLimitSec} ثانية")
                    }
                    Button(onClick = { onStartLevel(level.id) }) {
                        Text("ابدأ")
                    }
                }
            }
        }
    }
}
