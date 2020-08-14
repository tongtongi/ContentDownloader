package com.example.contentdownloader.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.contentdownloader.R
import com.example.contentdownloader.ui.download.DownloadActivity
import dagger.hilt.android.AndroidEntryPoint

private const val ANIMATION_DURATION = 4000L // means 4 seconds

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed(
            { viewModel.onSplashAnimationEnd() },
            ANIMATION_DURATION
        )
    }

    private fun setupObservers() {
        viewModel.navigateToNextScreen.observe(this, Observer {
            startActivity(DownloadActivity.newIntent(this))
            finish()
        })
    }
}