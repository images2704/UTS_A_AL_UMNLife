package com.example.umnlifeapp
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.umnlifeapp.R
import com.example.umnlifeapp.databinding.ActivityMainBinding


class GameDashboard : AppCompatActivity() {
    private var currentSemester: Int = 1
    private lateinit var clock: WaktuGame
    private lateinit var semesterTextView: TextView
    private lateinit var mahasiswa: Mahasiswa
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mahasiswa = Mahasiswa("Andrew")
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.progressPengetahuan.progress = mahasiswa.pengetahuan
        semesterTextView = findViewById(R.id.textView)

        val textView = findViewById<TextView>(R.id.textView)
        val inputText = intent.getStringExtra("EXTRA_TEXT")
        textView.text = inputText

        val clockTextView = findViewById<TextView>(R.id.clock)
        clock = WaktuGame(clockTextView, binding.progressPengetahuan, binding.progressKenyang, binding.progressEnergi, binding.progressMotivasi,mahasiswa)

        //  tombol tambah waktu
        findViewById<Button>(R.id.buttonLearn).setOnClickListener {
            if(binding.progressPengetahuan.progress == 0)
            {
                clock.addTime()
                startProgressBar(binding.progressPengetahuan)
            }
        }
//
    }
    private fun startProgressBar(progressBar: ProgressBar) {
        Log.d(TAG, "startProgressBar: ${mahasiswa.pengetahuan}")
        val progressTimer = object : CountDownTimer(5000, 10) {

            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = ((5000 - millisUntilFinished) / 50).toInt()
                Log.d(TAG, "onTick: ${progressBar.progress}")
            }

            override fun onFinish() {
                currentSemester++
                mahasiswa.pengetahuan = progressBar.progress
                semesterTextView.text = "Semester: $currentSemester, Progress ${progressBar.progress}"
            }
        }

        progressTimer.start()
    }



    override fun onDestroy() {
        clock.stop()
        super.onDestroy()
    }
}