package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


private val Notification_ID =0;



  fun NotificationManager.sendNotification(message : String , applicationContext: Context , download_status :String , file_name : String ){

   val  contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("FileName" , file_name)
    contentIntent.putExtra("Status" , download_status)

    val contentPendingIntent = PendingIntent.getActivity(
      applicationContext,
      Notification_ID,
      contentIntent,
      PendingIntent.FLAG_UPDATE_CURRENT
    )

    val Builder = NotificationCompat.Builder(applicationContext,MainActivity.CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_launcher_background)
      .setContentTitle(file_name)
      .setContentText(download_status)
      .setPriority(NotificationManager.IMPORTANCE_HIGH)
      .setContentIntent(contentPendingIntent)
      .setAutoCancel(true)



    notify(Notification_ID , Builder.build())
  }





