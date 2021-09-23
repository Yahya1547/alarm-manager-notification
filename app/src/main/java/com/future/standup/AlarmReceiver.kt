package com.future.standup

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat


class AlarmReceiver : BroadcastReceiver() {
  private var mNotificationManager: NotificationManager? = null
  override fun onReceive(context: Context?, intent: Intent?) {
    mNotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    deliverNotification(context)
  }

  private fun deliverNotification(context: Context) {
    Log.d("Alarm Receiver", "Notification delivered")
    val contentIntent = Intent(context, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
      context,
      NOTIFICATION_ID,
      contentIntent,
      PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_stand_up)
      .setContentTitle("Stand Up Alert")
      .setContentText("You should stand up and walk around now!")
      .setContentIntent(contentPendingIntent)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setAutoCancel(true)
      .setDefaults(NotificationCompat.DEFAULT_ALL)

    mNotificationManager?.notify(NOTIFICATION_ID, builder.build())
  }

  companion object {
    private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    private const val NOTIFICATION_ID = 0
  }


}