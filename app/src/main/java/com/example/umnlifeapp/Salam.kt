package com.example.umnlifeapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast

class Salam(context: Context?) : Toast(context) {
    @SuppressLint("MissingInflatedId")
    fun showCustomToast(message: String, activity: ActivityInGame)
    {
        val layout = activity.layoutInflater.inflate (
            R.layout.salam,
            activity.findViewById(R.id.toast_container)
        )

        // set the text of the TextView of the message
        val textView = layout.findViewById<TextView>(R.id.toast_text)

        // use the application extension function
        textView.text = message
        this.apply {
            setGravity(Gravity.BOTTOM, 0, 40)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }

    }
}