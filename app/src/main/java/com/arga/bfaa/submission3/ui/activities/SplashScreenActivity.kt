package com.arga.bfaa.submission3.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arga.bfaa.submission3.R
import android.os.Handler
import android.os.Looper

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(
                this@SplashScreenActivity,
                HomeActivity::class.java
            )
            startActivity(intent)
            finish()
        }, 100)
    }
}