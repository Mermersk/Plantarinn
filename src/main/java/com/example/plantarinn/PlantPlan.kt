package com.example.plantarinn

//One time unit, eg: 60 minutes in 1 hour, 60 seconds in 1 minute....
private const val ONE_UNIT_TIME = 60

//Doing the "default parameter" way of achieving constructor overloading
class PlantPlan constructor(private val hours: Int, private val minutes: Int, private var numberOfPlants: Int? = null, _numberOfBakkar: Int? = null, _bakkaSize: Int? = null) {

    //Should be the total planting time measured in seconds
    private val totalTimeS: Int

    init {
        if (_numberOfBakkar != null && _bakkaSize != null) {
            numberOfPlants = _numberOfBakkar * _bakkaSize
            println("Constrctor type where fjoldibakka and bakkaSize was called. Calculated numberofPlants is: $numberOfPlants")
        }

        totalTimeS = (hours * ONE_UNIT_TIME * ONE_UNIT_TIME) + (minutes * ONE_UNIT_TIME)
        val plantInterval: Double = (totalTimeS.toDouble() / numberOfPlants?.toDouble()!!)

        println("totalTime in seconds $totalTimeS")
        println("plantInterval: $plantInterval")
    }
    //val tt = "First $numberOfPlants".also(::println)

    //init {
    //val plantInterval: Double = (totalTimeS / numberOfPlants).toDouble()
    //}

    /* Late night edit: All codo below achieved instead with optional default parameters!!!
    //Instance variables
    //private val numberOfPlants: Int
    //private val hours: Int
    //private val minutes: Int

    //Multiple secondary constructors, defined which to use based on parameters supplied. Constructor overloading!
    //constructor(_numberOfPlants: Int, _hours: Int, _minutes: Int) {
        //Now these 3 values are properties of the class, if they come just through the constructor then they are not properties of the class
        //numberOfPlants = _numberOfPlants
        //hours = _hours
        //minutes = _minutes

        //totalTimeS = (hours * ONE_UNIT_TIME * ONE_UNIT_TIME) + (minutes * ONE_UNIT_TIME)
        //println("totalTime in seconds $totalTimeS (1 constructor)")
    //}

    //constructor(_numberOfBakkar: Int, _bakkaSize: Int, _hours: Int, _minutes: Int) {
        //numberOfPlants = _numberOfBakkar * _bakkaSize
        //hours = _hours
        //minutes = _minutes

        //totalTimeS = (hours * ONE_UNIT_TIME * ONE_UNIT_TIME) + (minutes * ONE_UNIT_TIME)
        //println("totalTime in seconds $totalTimeS (2 constructor)")
    //}

    */


}