package com.example.codestur



import android.app.Activity
import android.media.MediaController2
import android.os.Build
import android.os.Bundle
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


class Video_Player : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        supportActionBar?.hide()
        val web=findViewById<WebView>(R.id.webview)
        val frame=findViewById<FrameLayout>(R.id.customview)
        web.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/OVQuuPX-_hE?si=rUCHg8M6onwsFjeI\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; picture-in-picture; web-share\" allowfullscreen></iframe>","text/html","utf-8")
        web.settings.javaScriptEnabled = true
//        web.settings.loadWithOverviewMode = true
        web.settings.useWideViewPort = true
//        web.settings.loadWithOverviewMode = true
        web.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
//        web.settings.setSupportZoom(true)


        web.webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                web.visibility= View.GONE
                frame.visibility= View.VISIBLE
                frame.addView(view)
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                web.visibility= View.VISIBLE
                frame.visibility= View.GONE
                // Hande exiting fullscreen mode here
            }
        }


    }
}