package com.senku.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.senku.musicplayer.ui.home.HomeScreen
import com.senku.musicplayer.ui.permission.PermissionScreen
import com.senku.musicplayer.ui.theme.NovaPlayerTheme

class MainActivity : ComponentActivity() {

    private val hasPermission = mutableStateOf(false)

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission.value = granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        hasPermission.value = ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED

        setContent {
            NovaPlayerTheme {
                if (hasPermission.value) {
                    HomeScreen()
                } else {
                    PermissionScreen(
                        onGrant = {
                            permissionLauncher.launch(permission)
                        }
                    )
                }
            }
        }
    }
}
