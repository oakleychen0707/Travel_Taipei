package com.example.travel_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        supportActionBar?.hide()

        val webView: WebView = findViewById(R.id.webView)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val url: String? = intent.getStringExtra("webview_url")

        if (url != null) {
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE // 載入完成後隱藏進度條
                }
            }
            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    progressBar.progress = newProgress // 更新進度條進度
                }
            }
            webView.loadUrl(url)
        } else {
            Toast.makeText(this, "URL not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}