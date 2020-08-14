package com.example.contentdownloader.ui.videoplayer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.contentdownloader.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_video_player.*

@AndroidEntryPoint
class VideoPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        hideStatusBar()
        val filePath = intent.getStringExtra(KEY_PATH)
        filePath?.let {
            initializePlayer(it)
        }
    }

    private fun hideStatusBar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun initializePlayer(filePath: String) {
        val uri = Uri.parse(filePath)
        videoPlayer.setVideoURI(uri)
        videoPlayer.start()
    }

    override fun onStop() {
        super.onStop()
        videoPlayer.stopPlayback()
    }

    companion object {
        private const val KEY_PATH = "KEY_PATH"

        fun newIntent(context: Context, filePath: String) =
            Intent(context, VideoPlayerActivity::class.java).apply {
                putExtra(KEY_PATH, filePath)
            }
    }
}