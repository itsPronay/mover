package com.pronaycoding.mover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pronaycoding.mover.ui.theme.MoverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MoverTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MoverScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
