package com.msl.mynotification

import android.Manifest
import com.msl.mynotification.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
 import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private lateinit var requestPermissionLauncher : ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Initialize the launcher
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

                // Check if all permissions are granted
                val allPermissionsGranted = permissions.all { it.value }

                if (allPermissionsGranted) {
                    // All requested permissions are granted, perform your action
                    notificationShow()
                } else {
                    // Handle the case where one or more permissions are not granted
                }
            }
        val permissionsToRequest = arrayOf(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.WAKE_LOCK,
            android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            // Add more permissions as needed
        )
        requestPermissionLauncher.launch(permissionsToRequest)
    }

    fun notificationShow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.ic_notification) // Replace with your icon
            .setContentTitle("Notification Title")
            .setContentText("Notification Content")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(123, builder.build())
    }
}