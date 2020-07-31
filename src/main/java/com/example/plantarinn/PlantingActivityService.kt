package com.example.plantarinn

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Binder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat



/*
This should be a foreground service
 */
class PlantingActivityService : Service() {

    private val binder = LocalBinder()
    private val channelID = "PlantingActivity Notification"
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private var notificationText = "0 / 0"
    private val notificationID = 500

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService() : PlantingActivityService = this@PlantingActivityService
    }


    override fun onCreate() {
        super.onCreate()
        notificationBuilder = NotificationCompat.Builder(applicationContext, channelID)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        //val channelID = "PlantingActivity Notification"
        //val notChannel = NotificationChannel(channelID, "PA Channel", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //notificationManager.createNotificationChannel(notChannel)

        //var plantedTreesCount: Int = intent!!.getIntExtra("plantedTreesCount", 0)
        //val totalPlants: Int = intent!!.getIntExtra("totalPlants", -1)
        //putting in Intent into the notification so that user can return to the activity when notification is clicked
        val backToAppintent = Intent(this, PlantingActivity::class.java)


        //intent.addCategory(Intent.CATEGORY_LAUNCHER)
        backToAppintent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, backToAppintent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationBuilder
            .setContentTitle("Plöntun í gangi:")
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.tre)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .build()


        //notificationManager.notify(3, notification)
        startForeground(notificationID, notificationBuilder.build())

        println("onStartCommand was called!!!")

        return START_STICKY
        //return super.onStartCommand(intent, flags, startId)
    }


    //override fun startForegroundService(service: Intent?): ComponentName? {
        //return super.startForegroundService(service)
    //}

    fun updateNotification(updatedText: String) {

        notificationText = updatedText
        notificationBuilder.setContentText(notificationText)

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(notificationID, notificationBuilder.build())

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
