package com.example.noticekangwon.Activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.noticekangwon.R

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        var handler = Handler()
        handler.postDelayed(Runnable {
            finish()
        }, 1000)
    }
}