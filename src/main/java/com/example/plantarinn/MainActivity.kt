package com.example.plantarinn

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.Toast

const val EXTRA_MESSAGE = "com.example.plantarinn.MESSAGE"
//ID for the extra info going to PlantingActivity
const val EXTRA_PLANTING = "com.example.plantarinn.PLANTPLAN"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //called when send button is pressed
    fun sendMessage(view: View) {
        val textField = findViewById<EditText>(R.id.inputFjoldiPlantna)
        view.alpha = 0.5f
        textField.alpha = 0.5f
        var typedText: String = textField.text.toString()

        println(typedText)
        println("Will it type.....")

        //creating an intent, first param is context, second is the class that the system delivers the intent to.
        //In this case its the activity to start.
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            //The putExtra() method adds the value of EditText to the intent. An Intent can carry data types as key-value pairs called extras.
            putExtra(EXTRA_MESSAGE, typedText)
            //Your key is a public constant EXTRA_MESSAGE because the next activity uses the key to retrieve the text value. It's a good practice to define keys for intent extras with your app's package name as a prefix. This ensures that the keys are unique, in case your app interacts with other apps.
        }
        //The startActivity() method starts an instance of the DisplayMessageActivity that's specified by the Intent
        startActivity(intent)

    }

    //called when play button is pressed
    fun playSound(view: View) {
        var mp: MediaPlayer? = MediaPlayer.create(this, R.raw.duck1)
        mp?.start()
    }

    fun byrja(view: View) {
        val numberOfPlants = findViewById<EditText>(R.id.inputFjoldiPlantna)
        val hours = findViewById<EditText>(R.id.inputKlukkutimar)
        val minutes = findViewById<EditText>(R.id.inputMinutur)

        val numberOfPlantsStr = numberOfPlants.text.toString()
        val hoursStr = hours.text.toString()
        val minutesStr = minutes.text.toString()

        if (numberOfPlantsStr.isEmpty() || hoursStr.isEmpty() || minutesStr.isEmpty()) {
            //This is an android Toast object. Its a simple text popup.
            val missingFieldAlert = Toast.makeText(this, "Fylltu inn í öll þrjú svæðin", Toast.LENGTH_SHORT)
            missingFieldAlert.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 270)
            missingFieldAlert.show()
            //Return out of byrja if these conditions are not met
            return
        }

        val pp = PlantPlan(hoursStr.toInt(), minutesStr.toInt(), numberOfPlantsStr.toInt())

        val byrjaIntent = Intent(this, PlantingActivity::class.java).apply {
            putExtra(EXTRA_PLANTING, pp)
        }
        startActivity(byrjaIntent)
        //In order to "skip" a parameter in the list, In this case numberOfPlants, I need to supply
        //named parameters like this one:
        //val ss = PlantPlan(hoursStr.toInt(), minutesStr.toInt(), _numberOfBakkar = 20, _bakkaSize = 40)



        //println(numberOfPlantsStr)

    }


}