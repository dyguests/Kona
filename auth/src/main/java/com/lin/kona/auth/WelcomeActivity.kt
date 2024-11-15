package com.lin.kona.auth

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lin.framework.base.BaseActivity
import com.lin.kona.auth.ui.theme.KonaTheme

class WelcomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WelcomeScreen()
        }
    }

}

@Composable
private fun WelcomeScreen() {
    KonaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Image(
                painter = painterResource(id = R.drawable.splash),
                contentDescription = "Splash",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    WelcomeScreen()
}