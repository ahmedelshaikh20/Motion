package com.udacity

import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

  private var downloadID: Long = 0
lateinit var radioButton:RadioButton
  private lateinit var notificationManager: NotificationManager
  private lateinit var pendingIntent: PendingIntent
  private lateinit var action: NotificationCompat.Action
  private  var selected_url : String=""
  private var isSelected : Boolean = false
  private lateinit var  radioGroup: RadioGroup
private lateinit var selectedFileName : String
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    notificationManager = ContextCompat.getSystemService(
      applicationContext,
      NotificationManager::class.java
    ) as NotificationManager
    custom_button.buttonState = ButtonState.Completed
    registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    custom_button.setOnClickListener {
      if (custom_button.buttonState == ButtonState.Loading){
        custom_button.isClickable = false
      }
      getSelectedUrl()
      Log.d("tag" , (custom_button.buttonState==ButtonState.Loading).toString())
      if (isSelected == true) {
        isSelected = false
        custom_button.buttonState = ButtonState.Loading
        download()
        radioGroup.clearCheck()
        radioButton.isChecked = false
      }


    }
  }

  private val receiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
      val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

      val cursor = downloadManager.query(Query().setFilterById(downloadID))
      var status_string = ""
      if (cursor != null && cursor.moveToNext()) {
        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
        cursor.close()
        if (status == DownloadManager.STATUS_FAILED) {
          status_string = "Download Failed"
        } else if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED) {
          status_string = "Download Pending"
        } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
          status_string = "Download Successfully"
        } else if (status == DownloadManager.STATUS_RUNNING) {
          status_string = "Download Running"
        }

        createChannel(CHANNEL_ID , "Download")
        notificationManager.sendNotification(
          CHANNEL_ID,
          applicationContext,
          status_string,
          selectedFileName!!
        )

      } else {
        Toast.makeText(
          applicationContext,
          "Please Select one of the options Shown",
          Toast.LENGTH_SHORT
        ).show()
      }

      custom_button.buttonState = ButtonState.Completed
      custom_button.isClickable = true
      radioButton.isChecked = false

    }


  }

  private fun download() {
    notificationManager.cancelAll()

    Toast.makeText(applicationContext , selected_url, Toast.LENGTH_SHORT).show()

    if (selected_url != null) {
  val request =
    DownloadManager.Request(Uri.parse(selected_url))
      .setTitle(getString(R.string.app_name))
      .setDescription(getString(R.string.app_description))
      .setRequiresCharging(false)
      .setAllowedOverMetered(true)
      .setAllowedOverRoaming(true)
  val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
  downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.


  }}

  private fun getSelectedUrl() {
    radioGroup =  findViewById(R.id.radio);
    val selectedId = radioGroup.checkedRadioButtonId
    Log.d("Tag" , selectedId.toString())
    if(selectedId !=-1) {
      radioButton = findViewById<RadioButton>(selectedId);
      if (radioButton.text == "Retrofit")
        selected_url = RETROFIT_URL
      else if (radioButton.text == "Glide")
        selected_url = GLIDE_URL
      else if (radioButton.text == "Load App")
        selected_url = LOAD_APP_URL

      custom_button.isClickable=true

    isSelected = true
    selectedFileName = radioButton.text.toString()}

  }


  private fun createChannel(channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val notificationChannel = NotificationChannel(
        channelId,
        channelName,
        NotificationManager.IMPORTANCE_HIGH
      )

      notificationChannel.enableLights(true)

      notificationChannel.enableVibration(true)
      notificationChannel.description = getString(R.string.notification_description)

      val notificationManager = getSystemService(
        NotificationManager::class.java
      )
      notificationManager.createNotificationChannel(notificationChannel)
    }
  }


  companion object {
    private const val GLIDE_URL =
      "https://github.com/square/retrofit/archive/refs/heads/master.zip"
    private const val LOAD_APP_URL =
      "https://github.com/square/retrofit/archive/refs/heads/master.zip"
    private const val RETROFIT_URL =
      "https://github.com/square/retrofit/archive/refs/heads/master.zip"
     const val CHANNEL_ID = "channelId"
  }
}

