package com.example.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.databinding.DataBindingUtil
import com.example.notification.NotificationSnooze.Companion.ACTION_SNOOZE
import com.example.notification.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var channel: NotificationChannel
    private lateinit var notificationManager: NotificationManager
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // notification click intent
        val parentStackIntent =  Intent(this,MainActivity::class.java)
        val secondIntent = Intent(this, Notification::class.java)
        val pendingStackIntent : PendingIntent? = TaskStackBuilder.create(this).run {
            addParentStack(MainActivity::class.java)
            addNextIntent(parentStackIntent)
            addNextIntent(secondIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        // snooze
        val intent2 = Intent(this, NotificationSnooze::class.java).apply {
            action = ACTION_SNOOZE
        }
        val snoozePending = PendingIntent.getBroadcast(this, 0, intent2, 0)

        // reply
        val remoteInput = RemoteInput.Builder("remoteKey").run {
            setLabel("REPLY")
            build()
        }
        val replyIntent = Intent(this, ReplyNotification::class.java)
        val replyPendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.img)

        // Important Notification
        val importantNotification  = Intent(this,ShowEmergencyNotification::class.java)
        val importantNotificationPendingIntent = PendingIntent.getActivity(this,0,importantNotification,PendingIntent.FLAG_UPDATE_CURRENT)


        // notification
        binding.notificationButton.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel =
                    NotificationChannel(
                        "channelId",
                        "channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            val builder = NotificationCompat.Builder(this, "channelId")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Title")
                .setContentText("its a notification")
                .setContentIntent(pendingStackIntent)
                .setLargeIcon(largeIcon)
                .setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(largeIcon).bigLargeIcon(null)
                )
                .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.bigText)))
                .setGroup("groupId")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(
                    R.drawable.ic_launcher_foreground,
                    getString(R.string.snooze),
                    snoozePending
                )
                .addAction(
                    NotificationCompat.Action.Builder(
                        R.drawable.ic_launcher_foreground,
                        getString(R.string.reply),
                        replyPendingIntent
                    ).addRemoteInput(remoteInput).build()
                )

            notificationManager.notify(0, builder.build())


//            // second notification
//            val builder2 = NotificationCompat.Builder(this, "channelId")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("Title2")
//                .setContentText("its a notification2")
//                .setContentIntent(backStack)
//                .setLargeIcon(largeIcon)
//                .setStyle(
//                    NotificationCompat.BigPictureStyle().bigPicture(largeIcon).bigLargeIcon(null)
//                )
//                .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.bigText)))
//                .setGroup("groupId")
//                .setFullScreenIntent(importantNotificationPendingIntent,true)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//            notificationManager.notify(1,builder2.build())
//
//            val builder3 = NotificationCompat.Builder(this, "channelId")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("Title Summary")
//                .setContentText("Title summary details")
//                .setContentIntent(backStack)
//                .setLargeIcon(largeIcon)
//                .setStyle(
//                    NotificationCompat.BigPictureStyle().bigPicture(largeIcon).bigLargeIcon(null)
//                )
//                .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.bigText)))
//                .setGroup("groupId")
//                .setGroupSummary(true)
//                .setFullScreenIntent(importantNotificationPendingIntent,true)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//            notificationManager.notify(2,builder3.build())

        }
        binding.getReplyButton.setOnClickListener {
            val message = RemoteInput.getResultsFromIntent(intent).getCharSequence("remoteKey").toString()
            binding.textView.text = message
        }

    }

}