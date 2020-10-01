package com.example.plantarinn

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.Paint
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.util.*

class PlantingActivity : AppCompatActivity() {

    private val timer: Timer = Timer()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planting)

        val pp: PlantPlan? = intent.getParcelableExtra(EXTRA_PLANTING)

        //Calling the binding code to bind
        Intent(this, PlantingActivityService::class.java).also { intent ->
            intent.putExtra(EXTRA_PLANTING, pp)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)

            startForegroundService(intent)
        }

        val plantingGraphics = findViewById<PlantingMainGraphics>(R.id.plantingMainGraphics)
        //converting plantInterval from seconds til milliseconds
        val plantIntervalms = (pp?.plantInterval?.times(1000))

        plantingGraphics.setAnimationDuration(plantIntervalms!!.toLong())
        plantingGraphics.startAnimation()

        oneCycleActivity(plantIntervalms.toLong(), pp)

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

        cppInfo.text = "0 / ${pp.numberOfPlants}"

        //Setting a small tree icon
        val smallTree = getDrawable(R.drawable.tre)
        smallTree!!.setBounds(0, 0, 75, 88)
        cppInfo.setCompoundDrawables(null, null, smallTree, null)


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        super.onStart()
    }

    //Code that maintains one cycle(time to plant 1 tree)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun oneCycleActivity(period: Long, pp: PlantPlan) {

        //extends TimerTask. Runs on its own thread. Feed This object to Timer.schedule function
        class PlaySoundTask() : TimerTask() {

            val cppInfo = findViewById<TextView>(R.id.currentPlantingProgressInfo)
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                if (mBound == true) {
                    runOnUiThread {
                        cppInfo.text = "${plantingService.plantedTreesCount + 1} / ${pp.numberOfPlants}"
                    }
                }
            }
        }

        //Creating a PlaySoundTask
        val pst = PlaySoundTask()

        //Scheduling the timer to execute run() inside of PlaySoundTask every period.
        //period here in this case is the plantInterval value that was calculated in PlantPlan
        timer.schedule(pst, 0, period)


    }

    //default behaviour of onBackPressed is to finish the activity, aka it calls onDestroy()!
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDestroy() {

        unbindService(connection)
        timer.cancel()
        super.onDestroy()

    }


}