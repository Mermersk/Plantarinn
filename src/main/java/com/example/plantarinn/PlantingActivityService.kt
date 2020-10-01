package com.example.plantarinn

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.SoundPool
import android.os.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.*

/*
This should be a foreground service
 */
class PlantingActivityService : Service() {

    private val binder = LocalBinder()
    private val channelID = "PlantingActivity Notification"
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private val notificationID = 500
    private val timer: Timer = Timer()
    var plantedTreesCount = -1
    private var notificationText = "${plantedTreesCount} / 0"
    var notificationManager: NotificationManager? = null

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notChannel: NotificationChannel =
            NotificationChannel(channelID, "PA Channel", NotificationManager.IMPORTANCE_DEFAULT)

        notificationManager!!.createNotificationChannel(notChannel)

        //putting in Intent into the notification so that user can return to the activity when notification is clicked
        val backToAppintent = Intent(this, PlantingActivity::class.java)

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

        startForeground(notificationID, notificationBuilder.build())

        val pp: PlantPlan? = intent?.getParcelableExtra(EXTRA_PLANTING)
        val plantIntervalms = (pp?.plantInterval?.times(1000))
        if (plantIntervalms != null) {
            oneCycle(plantIntervalms.toLong(), pp)
        }

        return START_STICKY
    }


    fun updateNotification(updatedText: String) {

        notificationText = updatedText
        notificationBuilder.setContentText(notificationText)

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(notificationID, notificationBuilder.build())
    }

    //Code that maintains one cycle(time to plant 1 tree)
    fun oneCycle(period: Long, pp: PlantPlan) {

        //Initializing SoundPool for playing audio
        val sp = SoundPool.Builder()
        sp.setMaxStreams(1)
        val readySP = sp.build()
        val oneLoopCompleteSoundID = readySP.load(this, R.raw.blipblop2, 1)

        //Initializing VibratorEffect
        val vibEffect = VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
        val v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        //extends TimerTask. Runs on its own thread. Feed This object to Timer.schedule function
        class PlaySoundTask() : TimerTask() {

            override fun run() {
                readySP.play(oneLoopCompleteSoundID, 1.0f, 1.0f, 1, 0, 1.0f)
                v.vibrate(vibEffect)

                plantedTreesCount += 1
                updateNotification("$plantedTreesCount / ${pp.numberOfPlants} Tré")
            }
        }
        //Creating a PlaySoundTask
        val pst = PlaySoundTask()

        //Scheduling the timer to execute run() inside of PlaySoundTask every period.
        //period here in this case is the plantInterval value that was calculated in PlantPlan
        timer.schedule(pst, 0, period)

    }

    override fun onUnbind(intent: Intent?): Boolean {
        notificationManager?.cancelAll()
        timer.cancel()
        stopSelf()

        return super.onUnbind(intent)
    }


}
