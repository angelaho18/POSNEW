package com.example.posnew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var timeout_splash = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed(
            {
                //Intent Eksplisit
                var homeIntent = Intent(this, Login::class.java)
                startActivity(homeIntent)
                Toast.makeText(this, "Welcome to KASIR KU", Toast.LENGTH_LONG).show()
                finish()
            }, timeout_splash)
        timeout_splash = 1000
    }
}