package com.example.cinemacrazy.detailscreen

import android.os.Bundle
import android.webkit.WebViewClient
import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.R
import kotlinx.android.synthetic.main.cinema_webview.*

const val HOME_PAGE = "homepage"
const val TITLE = "title"

class WebView : BaseActivity() {

    override fun getLayoutRes(): Int {
        return R.layout.cinema_webview
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cinema_web_toolbar.title = intent.getStringExtra(TITLE)
        cinema_web_homepage.webViewClient = WebViewClient()
        cinema_web_homepage.settings.javaScriptEnabled = true
        cinema_web_homepage.loadUrl(intent.getStringExtra(HOME_PAGE))

    }

    override fun onBackPressed() {
        if (cinema_web_homepage.canGoBack())
            cinema_web_homepage.goBack()
        else
            super.onBackPressed()
    }
}