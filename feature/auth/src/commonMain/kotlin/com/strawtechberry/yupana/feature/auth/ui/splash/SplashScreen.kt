package com.strawtechberry.yupana.feature.auth.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.component.YupanaBrandLogo
import com.strawtechberry.yupana.core.designsystem.component.YupanaKhipuMotif
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Splash de marca mientras se resuelve la sesión. */
@Composable
fun SplashScreen() {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    Box(
        modifier = Modifier.fillMaxSize().background(colors.fondo),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.xxl),
        ) {
            YupanaKhipuMotif()
            YupanaBrandLogo()
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = colors.marca,
                strokeWidth = 2.dp,
            )
        }
    }
}
