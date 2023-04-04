package com.example.umnlifeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.umnlifeapp.databinding.RulesBinding

class playRules: AppCompatActivity()  {
    private lateinit var binding : RulesBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = RulesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener{
            var mMediaPlayer = MediaPlayer.create(this, R.raw.close_book)
            mMediaPlayer!!.start()

            val Intent =  Intent(this, ActivityInGame::class.java);
            startActivity(Intent)
        }
    }
}