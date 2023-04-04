package com.example.umnlifeapp

import android.os.Handler
import java.util.*

class TimeService() {
    private var timer: Timer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    fun setTimeout(func: ()-> Unit, delay: Long) {
        resetTimeOut()
        handler = Handler()
        runnable = Runnable {
            func()
        }
        handler?.postDelayed(runnable!!, delay)
    }

    fun resetTimeOut(){
        handler?.removeCallbacks(runnable!!)
    }

    private class TimeTask(private var func: ()-> Unit): TimerTask() {
        override fun run() {
            func()
        }
    }
    fun setInterval(func: ()-> Unit,interval: Long) {
        timer = Timer()
        timer?.scheduleAtFixedRate(TimeTask(func), 0, interval)
    }
    fun resetInterval(){
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    companion object {
        private val timer = Timer()
        private var handler: Handler? = null
        private var runnable: Runnable? = null

        private class TimeTask(private var func: ()-> Unit): TimerTask() {
            override fun run() {
                func()
            }
        }

        fun outTask(func: Runnable): Runnable {
            return Runnable { func }
        }

        fun setInterval(func: ()-> Unit,interval: Long) {
            this.timer.scheduleAtFixedRate(TimeTask(func), 0, interval)
        }

        fun resetInterval(){
            timer.cancel()
        }

        fun setTimeout(func: ()-> Unit, delay: Long) {
            resetTimeOut()
            handler = Handler()
            runnable = Runnable {
                func()
                setTimeout(func,delay)
            }
            handler?.postDelayed(runnable!!, delay)
        }

        fun resetTimeOut(){
            handler?.removeCallbacks(runnable!!)
        }
    }
}