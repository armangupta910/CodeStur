package com.example.codestur



import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.media.MediaController2
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.regex.Matcher
import java.util.regex.Pattern


class Video_Player : AppCompatActivity() {

    private lateinit var youtubePlayerView: YouTubePlayerView

    fun extractYouTubeVideoId(videoUrl: String): String? {
        val prefix = "v="
        val startIndex = videoUrl.indexOf(prefix) + 2
        return videoUrl.substring(startIndex)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        supportActionBar?.hide()

        var link:String = intent.getStringExtra("Link") as String
        link = extractYouTubeVideoId(link).toString()

        youtubePlayerView = findViewById(R.id.youtube_player_view)

        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                val videoId = link // Replace with your YouTube video ID
                Log.d(TAG,"Link :- ${link}")
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })

        youtubePlayerView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                youtubePlayerView.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }

            override fun onYouTubePlayerExitFullScreen() {
                youtubePlayerView.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        youtubePlayerView.release()
    }
}