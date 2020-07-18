package com.example.plantarinn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PlantingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planting)

        val pp: PlantPlan? = intent.getParcelableExtra(EXTRA_PLANTING)
        if (pp != null) {
            //println("Coming from PlantingActivity ${pp.totalTimeS}")
        }
    }
}