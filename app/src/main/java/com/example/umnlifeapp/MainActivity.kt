package com.example.umnlifeapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_main)

        if(getSharedPreferences("dataGame", MODE_PRIVATE).contains("NAMA")){
            val Intent =  Intent(this, ActivityInGame::class.java);
            startActivity(Intent)
        }


        val charImage1: ImageView = findViewById(R.id.character1)
        val charImage2: ImageButton = findViewById(R.id.img_button1)
        val charImage3: ImageButton = findViewById(R.id.img_button2)
        val input: EditText = findViewById(R.id.input_name)

        charImage1.setImageResource(R.drawable.char1)
        charImage1.setTag(R.drawable.char1)
        charImage2.setImageResource(R.drawable.char2)
        charImage2.setTag(R.drawable.char2)
        charImage3.setImageResource(R.drawable.char3)
        charImage3.setTag(R.drawable.char3)

        charImage2.setOnClickListener{switchChar(R.id.img_button1)}
        charImage3.setOnClickListener{switchChar(R.id.img_button2)}

        val buttonSubmit: Button = findViewById(R.id.button)
        buttonSubmit.setOnClickListener { nextActifity(charImage1.getTag().toString(), input.text.toString()) }
    }

    private fun nextActifity(char: String,  name: String) {

        val inputName: EditText = findViewById(R.id.input_name)
        val stringInTextField: String = inputName.text.toString()
        if(stringInTextField.isEmpty()){
            val toast = Toast.makeText(this, "Please Input your name",
                Toast.LENGTH_SHORT)
            toast.show()
        }else{
            val Intent =  Intent(this, ActivityInGame::class.java);
            Intent.putExtra("char", char.toString())
            Intent.putExtra("name", name.toString())
            startActivity(Intent)
        }
    }


    private fun switchChar(index: Int) {
        val currImg: ImageButton = findViewById(index)
        val charImage1: ImageView = findViewById(R.id.character1)

        val newImage = when(currImg.getTag()){
            R.drawable.char1 -> R.drawable.char1
            R.drawable.char2 -> R.drawable.char2
            R.drawable.char3 -> R.drawable.char3
            else-> R.drawable.char4
        }

        val prevImage = when(charImage1.getTag()){
            R.drawable.char1 -> R.drawable.char1
            R.drawable.char2 -> R.drawable.char2
            R.drawable.char3 -> R.drawable.char3
            else-> R.drawable.char4
        }

        currImg.setImageResource(prevImage)
        currImg.setTag(prevImage)

        charImage1.setImageResource(newImage)
        charImage1.setTag(newImage)
    }
}


