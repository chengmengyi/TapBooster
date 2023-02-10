package com.demo.clear.activity.clean

import android.content.Intent
import com.demo.clear.R
import com.demo.clear.admob.LoadAdmobManager
import com.demo.clear.admob.ShowFullAd
import com.demo.clear.base.BaseActivity
import com.demo.clear.conf.LocalConf
import kotlinx.android.synthetic.main.activity_clean_gou.*
import kotlinx.coroutines.*

class CleanGouActivity:BaseActivity(R.layout.activity_clean_gou) {
    private var canBack=false
    private var job:Job?=null
    private val showFullAd by lazy { ShowFullAd(LocalConf.CLEANING) }

    override fun viewCreated() {
        LoadAdmobManager.load(LocalConf.CLEANING)
        immersionBar.statusBarView(top).init()
        iv_back.setOnClickListener { onBackPressed() }

        job=GlobalScope.launch {
            delay(500)
            if(resume){
                withContext(Dispatchers.Main){
                    showFullAd.show(
                        this@CleanGouActivity,
                        emptyAdCallback = true,
                        closeAd = {
                            startActivity(Intent(this@CleanGouActivity,CleanSuccessActivity::class.java).apply {
                                putExtra("info","Your phone is very clean")
                            })
                            finish()
                        }
                    )
                }
            }else{
                canBack=true
            }
        }
    }

    override fun onPause() {
        super.onPause()
        canBack=true
    }

    override fun onBackPressed() {
        if (canBack) finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        job=null
    }
}