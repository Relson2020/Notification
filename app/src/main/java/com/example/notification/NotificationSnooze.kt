package com.example.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NotificationSnooze : AppCompatActivity() {
    companion object {

        const val ACTION_SNOOZE = "com.example.notification.NotificationSnooze.ACTION_SNOOZE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_snooze)
    }
}