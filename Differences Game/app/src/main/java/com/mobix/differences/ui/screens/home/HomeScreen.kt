package com.mobix.differences.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobix.differences.data.repo.LevelsRepository
import com.mobix.differences.ui.theme.*

@Composable
fun HomeScreen(onStartLevel: (String) -> Unit) {
    val levels = LevelsRepository.getAllLevels()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgGradientStart, BgGradientEnd)))
    ) {
        Column(Modifier.padding(horizontal = 24.dp)) {
            // Header
            Spacer(Modifier.height(60.dp))
            Text(
                "اكتشف الفرق",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                ),
                color = Color.White
            )
            Text(
                "اختر مرحلة للبدء بالتحدي",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.6f)
            )

            Spacer(Modifier.height(32.dp))

            // Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(levels) { level ->
                    LevelCard(
                        id = level.id,
                        title = level.title,
                        difficulty = "سهل", // يمكنك جلبها من الكلاس لاحقاً
                        onClick = { onStartLevel(level.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun LevelCard(id: String, title: String, difficulty: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(28.dp))
            .clickable { onClick() }
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(28.dp)),
        colors = CardDefaults.cardColors(containerColor = SoftCrystal)
    ) {
        Box(Modifier.fillMaxSize()) {
            // رقم المرحلة في الخلفية كعنصر تصميمي
            Text(
                text = id,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
                color = Color.White.copy(alpha = 0.05f)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // أيقونة اللعب الصغيرة
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Brush.linearGradient(listOf(NeonCyan, NeonPurple)),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                }

                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        difficulty,
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan
                    )
                }
            }
        }
    }
}