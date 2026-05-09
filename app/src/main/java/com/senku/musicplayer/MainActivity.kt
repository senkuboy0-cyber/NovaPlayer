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
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermission.value = permissions.entries.all { it.value }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        hasPermission.value = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        setContent {
            NovaPlayerTheme {
                if (hasPermission.value) {
                    HomeScreen()
                } else {
                    PermissionScreen(
                        onGrant = {
                            permissionLauncher.launch(permissions)
                        }
                    )
                }
            }
        }
    }
}
