package com.demo.clear.activity

import com.demo.clear.R
import com.demo.clear.base.BaseActivity
import com.demo.clear.conf.LocalConf
import kotlinx.android.synthetic.main.activity_url.*

class UrlActivity:BaseActivity(R.layout.activity_url) {

    override fun viewCreated() {
        immersionBar.statusBarView(top).init()
        iv_back.setOnClickListener { finish() }

        web_view.apply {
            settings.javaScriptEnabled=true
            loadUrl(intent.getStringExtra("url")?:"")
        }
    }
}