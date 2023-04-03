package com.example.umnlifeapp

import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.TextView
import com.example.umnlifeapp.MahasiswaOverall
import java.text.SimpleDateFormat
import java.util.*

class WaktuGame(private val clock: TextView, progressPengetahuan: ProgressBar, progressKenyang: ProgressBar, progressEnergi: ProgressBar, progressMotivasi: ProgressBar, private val mahasiswa: MahasiswaOverall) {
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val handler = Handler(Looper.getMainLooper())

    //Membuat kecepatan 1 menit = 60 detik
    private val speedRatio = 60
    //Variabel yang menunjukkan waktu
    private var currentTime = Calendar.getInstance()

    init {
        handler.post(object : Runnable {
            override fun run() {
                currentTime.add(Calendar.SECOND, speedRatio)

                progressPengetahuan.progress = mahasiswa.pengetahuan
                // Kondisi dimana suasana jam disesuaikan dengan text
                val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
                val timeOfDay = when (currentHour) {
                    in 6..11 -> "pagi"
                    in 12..17 -> "siang"
                    else -> "malam"
                }

                // format jam dan suasana ke dalam teks
                val clockText = dateFormat.format(currentTime.time)
                val timeOfDayText = "Selamat $timeOfDay, Pengetahuan: ${mahasiswa.pengetahuan}"
                val fullText = "$timeOfDayText\n$clockText"



                // tampilan jam dan suasana pada TextView
                clock.text = fullText

                handler.postDelayed(this, 1000) //  update setiap 1 detik
                mahasiswa.kurangiPengetahuan(33)
            }
        })
    }
    fun addTime(){
        //3600 = 1 jam jadi dia langsung skip time 1 jam
        currentTime.add(Calendar.SECOND, 7200)
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
    }
}
