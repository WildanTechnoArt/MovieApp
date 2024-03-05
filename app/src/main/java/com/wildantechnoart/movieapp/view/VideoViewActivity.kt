package com.wildantechnoart.movieapp.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.wildantechnoart.movieapp.R
import com.wildantechnoart.movieapp.databinding.ActivityVideoViewBinding
import com.wildantechnoart.movieapp.utils.Constant
import com.wildantechnoart.movieapp.utils.ViewBindingExt.viewBinding


class VideoViewActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityVideoViewBinding::inflate)
    private var getVideoFile: String? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        getVideoFile = intent.getStringExtra(Constant.TRAILER_KEY).toString()

        binding.webVideo.settings.javaScriptEnabled = true
        binding.swipeRefresh.setOnRefreshListener {
            loadWebView()
        }

        loadWebView()
    }

    private fun loadWebView() = with(binding) {
        val videoUrl = "<iframe id=\"player\" type=\"text/html\" width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/$getVideoFile?enablejsapi=1\"frameborder=\"0\"></iframe>"

        swipeRefresh.isRefreshing = true
        webVideo.loadData(videoUrl, "text/html", "utf-8")
        webVideo.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                webVideo.loadData(request?.url.toString(), "text/html", "utf-8")
                swipeRefresh.isRefreshing = true
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webVideo.canGoBack()) {
            binding.webVideo.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}