package com.example.plantarinn

import android.app.Service
import android.content.ComponentName
import kotlin.math.round
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.alpha
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

class PlantingActivity : AppCompatActivity() {

    val timer: Timer = Timer()
    //private var cppInfo: TextView? = null
    var plantedTreesCount = -1
    private var mBound: Boolean = false
    private lateinit var plantingService : PlantingActivityService

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as PlantingActivityService.LocalBinder

            plantingService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planting)

        val pp: PlantPlan? = intent.getParcelableExtra(EXTRA_PLANTING)

        val plantingGraphics = findViewById<PlantingMainGraphics>(R.id.plantingMainGraphics)
        //converting plantInterval from seconds til milliseconds
        val plantIntervalms = (pp?.plantInterval?.times(1000))

        plantingGraphics.setAnimationDuration(plantIntervalms!!.toLong())
        plantingGraphics.startAnimation()

        oneCycle(plantIntervalms.toLong(), pp)

        val textBackgroundColor: Paint = Paint()
        textBackgroundColor.color = Color.parseColor("#9F6420")
        textBackgroundColor.alpha = 100

        val plantSpeedInfo = findViewById<TextView>(R.id.plantSpeedInfo)
        plantSpeedInfo.text = "Tími per plöntu: %.1fs".format(pp.plantInterval)
        plantSpeedInfo.setBackgroundColor(textBackgroundColor.color)

        val totalPlantingTimeInfo = findViewById<TextView>(R.id.totalPlantingTimeInfo)
        //Correct language edge case
        var hours = "tímar"
        if (pp.hours == 1) {
            hours = "tími"
        }
        totalPlantingTimeInfo.text = "${pp.hours} $hours & ${pp.minutes} minútur"
        totalPlantingTimeInfo.setBackgroundColor(textBackgroundColor.color)


        val cppInfo = findViewById<TextView>(R.id.currentPlantingProgressInfo)
        cppInfo.text = "$plantedTreesCount / ${pp.numberOfPlants}"

        //Setting a small tree icon
        val smallTree = getDrawable(R.drawable.tre)
        smallTree!!.setBounds(0, 0, 75, 88)
        cppInfo.setCompoundDrawables(null, null, smallTree, null)

        //val startPlantingIntent = Intent(this, PlantingActivityService::class.java).apply {
            //putExtra("plantedTreesCount", plantedTreesCount)
            //putExtra("totalPlants", pp.numberOfPlants)
        //}

        //val plantingService = PlantingActivityService().LocalBinder()

        //val startPlantingIntent = Intent(this, PlantingActivityService::class.java)
        //bindService(startPlantingIntent, plantingService, Context.BIND_AUTO_CREATE)

        //this.startService(startPlantingIntent)

        //plantingService.startService(startPlantingIntent)



    }

    override fun onStart() {
        super.onStart()

        Intent(this, PlantingActivityService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }
        }
    }

    //Code that maintains one cycle(time to plant 1 tree)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun oneCycle(period: Long, pp: PlantPlan) {

        //Initializing SoundPool for playing audio
        val sp = SoundPool.Builder()
        sp.setMaxStreams(1)
        val readySP = sp.build()
        val oneLoopCompleteSoundID = readySP.load(this, R.raw.blipblop2, 1)


        //extends TimerTask. Runs on its own thread. Feed This object to Timer.schedule function
        class PlaySoundTask() : TimerTask() {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                readySP.play(oneLoopCompleteSoundID, 1.0f, 1.0f, 1, 0, 1.0f)
                plantedTreesCount += 1
                runOnUiThread {
                    val cppInfo = findViewById<TextView>(R.id.currentPlantingProgressInfo)
                    cppInfo.text = "$plantedTreesCount / ${pp.numberOfPlants}"

                }
                if (mBound == true) {
                    plantingService.updateNotification("$plantedTreesCount / ${pp.numberOfPlants} Tré")
                }


            }
        }

        //Creating a PlaySoundTask
        val pst = PlaySoundTask()

        //Scheduling the timer to execute run() inside of PlaySoundTask every period.
        //period here in this case is the plantInterval value that was calculated in PlantPlan
        timer.schedule(pst, 0, period)



    }

    //@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    //fun pauseThings() {
        //timer.cancel()
    //}

    //override fun onPause() {
        //super.onPause()
        //cancel all tasks in the timer her in onPause.
        //onPause is triggered when user leaves the planting activity, f.ex: to go back to main menu
        //timer.cancel()

    //}

    //override fun onBackPressed() {
        //super.onBackPressed()
        //super.onDestroy()
    //}

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        unbindService(connection)
        plantingService.stopSelf()
        //val stopPlantingIntent = Intent(this, PlantingActivityService::class.java)
        //this.stopService(stopPlantingIntent)
    }
}