package com.example.plantarinn

import kotlin.math.round
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

class PlantingActivity : AppCompatActivity() {

    val timer: Timer = Timer()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planting)

        val pp: PlantPlan? = intent.getParcelableExtra(EXTRA_PLANTING)

        val plantingGraphics = findViewById<PlantingMainGraphics>(R.id.plantingMainGraphics)
        //converting plantInterval from seconds til milliseconds
        val plantIntervalms = (pp?.plantInterval?.times(1000))
        //Using elvis operator for pp.plantInterval, if plantInterval is null then pass in 1.0
        plantingGraphics.setAnimationDuration(plantIntervalms!!.toLong())
        plantingGraphics.startAnimation()
        playSound(plantIntervalms.toLong())

        val plantSpeedInfo = findViewById<TextView>(R.id.plantSpeedInfo)
        plantSpeedInfo.text = "Time each plant: ${pp.plantInterval}s"
        plantSpeedInfo.setBackgroundColor(Color.LTGRAY)

        val totalPlantingTimeInfo = findViewById<TextView>(R.id.totalPlantingTimeInfo)
        totalPlantingTimeInfo.text = "${pp.hours} hours & ${pp.minutes} minutes"
        totalPlantingTimeInfo.setBackgroundColor(Color.LTGRAY)

    }
    
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun playSound(period: Long) {

        //Initializing SoundPool for playing audio
        val sp = SoundPool.Builder()
        sp.setMaxStreams(1)
        val readySP = sp.build()
        val oneLoopCompleteSoundID = readySP.load(this, R.raw.blipblop2, 1)


        //extends TimerTask. Runs on its own thread. Feed This object to Timer.schedule function
        class PlaySoundTask() : TimerTask() {

            override fun run() {
                println("Hopefully gonn aplay sound soon")
                readySP.play(oneLoopCompleteSoundID, 1.0f, 1.0f, 1, 0, 1.0f)
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

    override fun onPause() {
        super.onPause()
        //cancel all tasks in the timer her in onPause.
        //onPause is triggered when user leaves the planting activity, f.ex: to go back to main menu
        timer.cancel()
    }
}