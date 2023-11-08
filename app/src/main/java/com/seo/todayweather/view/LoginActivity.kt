package com.seo.todayweather.view

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.seo.todayweather.databinding.ActivityLoginBinding
import com.seo.todayweather.util.extension.setOnAvoidDuplicateClickFlow

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        installSplashScreen()
        initView()

    }

    private fun initView () {
        with(binding) {
            btnMain.setOnAvoidDuplicateClickFlow {
                intent = Intent(this@LoginActivity, InitActivity::class.java)
                startActivity(intent)
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}