package com.example.stopwatch

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.stopwatch.R.id
import com.example.stopwatch.R.id.*
import com.example.stopwatch.R.id.resetButton as idResetButton

class MainActivity : AppCompatActivity() {
    // variables
    private lateinit var chronometer: Chronometer // chronometer is a stopwatch type of thing
    private var running = false // stopwatch is running or not
    private var offset : Long = 0 // start time for the stopwatch

    // Key string for bundle
    private val OFFSET_KEY = "offset"
    private val RUNNING_KEY = "running"
    private val BASE_KEY = "base"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        chronometer = findViewById<Chronometer>(id.chronometer)

        // check on the running chronometer is it going -ve
        chronometer.setOnChronometerTickListener {

            if (isGoingNegative()) {
                chronometer.stop()
                running = false
                offset = 0
            }else if (offset > 0 && running) {

                offset-=2000
            }
        }

        // Restore from the previous bundle
        if(savedInstanceState !=null) {
          offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)

            if(running) {
                chronometer.base = savedInstanceState.getLong(BASE_KEY)
                chronometer.start()
            } else {
                setBaseTime()
            }
        }
        val addfiveSecButton = findViewById<Button>(addfiveButton)
        val addtenSecButton = findViewById<Button> (addtenButton)
        // add one minute button function
        addfiveSecButton.setOnClickListener {

            if(!running) {
                offset += 2000
                chronometer.base = SystemClock.elapsedRealtime() + offset
            }else{
                showWarningAlert(
                    "You can not increase the time when timer is running",
                    "alert!"
                )   

            }

        }
        // add five second button
        addtenSecButton.setOnClickListener {
            if (!running){
                offset += 6000
                chronometer.base = SystemClock.elapsedRealtime() + offset
            }else{
                showWarningAlert(
                    "You can not increase the time when timer is running",
                    "alert!"
                )
            }
        }
        // start button
        findViewById<Button>(startButton).setOnClickListener {

            if (!running) {
                if (isGoingNegative()){

                    chronometer.stop()

                }else {
                    //set base time
                    setBaseTime()
                    //start the stop watch
                    chronometer.start()
                    // set running true
                    running = true
                }
            }
        }
        //stop button function
        val stopButton = 0
        findViewById<Button>(stopButton).setOnClickListener {
            if (running) {
                //save offset
//                saveOffset()
                // stop the stopwatch
                chronometer.stop()
                //set running false
                running = false
            }
        }

        //reset button function
        val resetButton = 0
        findViewById<Button>(resetButton).setOnClickListener {
            //set offset to zero
            offset = 0
            //reset stop watch to 0
            setBaseTime()
            running = false
        }
    }





    private fun showWarningAlert(message: String, title: String) {
        val dialogue = AlertDialog.Builder(this)
        dialogue.setMessage(message)
        dialogue.setCancelable(true)
        val alert = dialogue.create()
        alert.setTitle(title)
        alert.show()
    }



    override fun onSaveInstanceState(savedInstanceState: Bundle){
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY,running)
        savedInstanceState.putLong(BASE_KEY,chronometer.base)
        super.onSaveInstanceState(savedInstanceState)
    }
    override fun onPause() {
        super.onPause()
        if(running) {
            saveOffset()
            chronometer.stop()
        }
    }
     override fun onResume() {
        super.onResume()
        if(running) {
            setBaseTime()
            chronometer.start()
            offset = 0

        }
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - chronometer.base
    }

    //allows us to set base time for any offset.
    private fun setBaseTime() {
        chronometer.base = SystemClock.elapsedRealtime() + offset
    }

    private fun isGoingNegative() : Boolean {
        val time = chronometer.text.toString()
        if (time == "00:00" || time == "00:00:00") {
            return true
        }
        if (time.contains("-")) {
            return true
        }
        return false
    }
}



