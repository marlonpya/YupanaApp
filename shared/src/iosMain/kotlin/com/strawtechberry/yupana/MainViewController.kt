package com.strawtechberry.yupana

import androidx.compose.ui.window.ComposeUIViewController

// Punto de entrada que consume el esqueleto iOS (iosApp) vía el framework `shared`.
fun MainViewController() = ComposeUIViewController { App() }
