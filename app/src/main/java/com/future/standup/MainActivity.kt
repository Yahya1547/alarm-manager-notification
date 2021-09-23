package com.future.standup

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
  companion object {
    private const val NOTIFICATION_ID = 0
    private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
  }

  private lateinit var mNotificationManager: NotificationManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val alarmToggle: ToggleButton = findViewById(R.id.alarm_toggle)

    val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
    mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val notifyIntent = Intent(this, AlarmReceiver::class.java)

    val alarmUp = (PendingIntent.getBroadcast(
      this, NOTIFICATION_ID, notifyIntent,
      PendingIntent.FLAG_NO_CREATE
    ) != null)

    alarmToggle.isChecked = alarmUp

    val notifyPendingIntent = PendingIntent.getBroadcast(
      this,
      NOTIFICATION_ID,
      notifyIntent,
      PendingIntent.FLAG_UPDATE_CURRENT
    )


    alarmToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      var toastMessage = ""
      if (isChecked) {
        val triggerTime = SystemClock.elapsedRealtime()
        if(alarmManager != null){
          alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerTime, notifyPendingIntent
          )
        }
        toastMessage = "Stand Up Alarm On!"
      } else {
        mNotificationManager.cancelAll()
        if (alarmManager != null) {
          alarmManager.cancel(notifyPendingIntent);
        }
        toastMessage = "Stand Up Alarm Off!"
      }

      Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
    }

    createNotificationChannel()
  }

  private fun createNotificationChannel() {
    mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val notificationChannel = NotificationChannel(
        PRIMARY_CHANNEL_ID,
        "Stand up notification",
        NotificationManager.IMPORTANCE_HIGH
      )

      notificationChannel.enableLights(true)
      notificationChannel.lightColor = Color.RED
      notificationChannel.enableVibration(true)
      notificationChannel.description = "Notifies every 15 minutes to stand up and walk"
      mNotificationManager.createNotificationChannel(notificationChannel)

    }
  }

}