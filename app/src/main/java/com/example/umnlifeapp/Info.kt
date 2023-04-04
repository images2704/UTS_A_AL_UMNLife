package com.example.umnlifeapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.umnlifeapp.databinding.InfoGameBinding

@Suppress("DEPRECATION")
class Info: AppCompatActivity() {
    private lateinit var binding: InfoGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = InfoGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.restartButton.setOnClickListener{
            val saveData = getSharedPreferences("dataGame", MODE_PRIVATE).edit()
            saveData?.clear()
            saveData?.apply()
            val intent =  Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.backButton.setOnClickListener{
            val intent =  Intent(this, ActivityInGame::class.java)
            startActivity(intent)
        }
    }
}