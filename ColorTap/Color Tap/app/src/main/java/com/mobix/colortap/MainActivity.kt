package com.mobix.colortap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobix.colortap.ui.navigation.AppNavGraph
import com.mobix.colortap.ui.theme.ColortapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColortapTheme { // التأكد من تغليف التطبيق بالثيم المصلح
                Surface(color = MaterialTheme.colorScheme.background) {
//                    AppLogoPreview()
                    AppNavGraph()
                }
            }
        }
    }
    @Composable
    fun AppLogoPreview() {
        Box(
            modifier = Modifier
                .size(120.dp)
                .shadow(20.dp, CircleShape, spotColor = Color(0xFF007AFF))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF007AFF), Color(0xFFAF52DE))
                    ),
                    shape = CircleShape
                )
                .border(4.dp, Color.White.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "CT",
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}