package com.example.parceltrack

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PinActivity : AppCompatActivity() {

    private val adminPin = "6698"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        val etPin = findViewById<EditText>(R.id.etPin)
        val btnPinLogin = findViewById<TextView>(R.id.btnPinLogin)

        btnPinLogin.setOnClickListener {
            val inputPin = etPin.text.toString()

            if (inputPin == adminPin) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "PIN 번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}