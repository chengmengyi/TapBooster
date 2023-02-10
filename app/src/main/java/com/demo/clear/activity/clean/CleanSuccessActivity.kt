package com.demo.clear.activity.clean

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.demo.clear.R
import com.demo.clear.activity.HomeActivity
import com.demo.clear.admob.ShowNativeAd
import com.demo.clear.base.BaseActivity
import com.demo.clear.conf.LocalConf
import com.demo.clear.util.LimitManager
import com.demo.clear.util.MMKVManager
import kotlinx.android.synthetic.main.activity_clean_success.*
import org.greenrobot.eventbus.EventBus

class CleanSuccessActivity:BaseActivity(R.layout.activity_clean_success) {
    private val showResultNativeAd by lazy { ShowNativeAd(LocalConf.RESULT_NATIVE,this,true) }


    override fun viewCreated() {
        immersionBar.statusBarView(top).init()
        setInfo()
        iv_back.setOnClickListener { onBackPressed() }
    }

    private fun setInfo(){
        val s = intent.getStringExtra("size") ?: ""
        if (s.isEmpty()){
            tv_cleaned.text=intent.getStringExtra("info")
        }else{
            tv_cleaned.text="Cleaned $s"
        }
        if (intent.getStringExtra("type")=="clean"){
            MMKVManager.saveCleanTime()
        }
        EventBus.getDefault().post("clean_success")
    }

    override fun onResume() {
        super.onResume()
        if (LimitManager.refreshNativeAd(LocalConf.RESULT_NATIVE)){
            showResultNativeAd.loopCheck()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showResultNativeAd.stopCheck()
        EventBus.getDefault().unregister(this)
        LimitManager.setRefreshBool(LocalConf.RESULT_NATIVE,true)
    }

    override fun onBackPressed() {
        if (!ActivityUtils.isActivityExistsInStack(HomeActivity::class.java)){
            startActivity(Intent(this,HomeActivity::class.java))
        }
        finish()
    }
}