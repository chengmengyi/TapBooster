package com.demo.clear.activity.clean

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.demo.clear.R
import com.demo.clear.admob.LoadAdmobManager
import com.demo.clear.admob.ShowFullAd
import com.demo.clear.base.BaseActivity
import com.demo.clear.bean.CleanBean
import com.demo.clear.conf.LocalConf
import com.demo.clear.util.AppListManager
import kotlinx.android.synthetic.main.activity_cleaning.*
import kotlinx.coroutines.*
import java.io.File



class CleaningActivity:BaseActivity(R.layout.activity_cleaning) {
    private var job:Job?=null
    private var animator:ObjectAnimator?=null
    private var deleteAnimator:ValueAnimator?=null
    private val showFullAd by lazy { ShowFullAd(LocalConf.CLEANING) }

    override fun viewCreated() {
        LoadAdmobManager.load(LocalConf.CLEANING)
        deleteFile()
        immersionBar.statusBarView(top).init()

        startAnimator()
        iv_back.setOnClickListener { finish() }
    }

    private fun deleteFile(){
        GlobalScope.launch {
            val list = intent.getSerializableExtra("list") as ArrayList<CleanBean>
            var hasClearRam=false
            val path= arrayListOf<String>()
            list.forEach {
//                if(it.title=="RAM Used"){
//                    hasClearRam=true
//                }
                if(it.path.isNotEmpty()){
                    path.addAll(it.path)
                }
            }
            path.forEach { File(it).delete() }
//            if (hasClearRam){
//                clearRam()
//            }
        }
    }

//    private fun clearRam(){
//        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
//        val infoList = am.runningAppProcesses
//        if (infoList != null) {
//            for (i in infoList.indices) {
//                val appProcessInfo = infoList[i]
//                if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
//                    val pkgList = appProcessInfo.pkgList
//                    for (j in pkgList.indices) {
//                        am.killBackgroundProcesses(pkgList[j])
//                    }
//                }
//            }
//        }
//    }

    private fun startAnimator(){
        animator=ObjectAnimator.ofFloat(iv, "rotation", 0f, 360f).apply {
            duration=1000L
            repeatCount= ValueAnimator.INFINITE
            repeatMode= ObjectAnimator.RESTART
            start()
        }

        deleteAnimator=ValueAnimator.ofInt(0, 100).apply {
            duration = 5000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                tv_percent.text = "$progress%"
            }
            doOnEnd {
                job?.cancel()
                showFullAd.show(
                    this@CleaningActivity,
                    emptyAdCallback = true,
                    closeAd = {
                        startActivity(Intent(this@CleaningActivity,CleanSuccessActivity::class.java).apply {
                            putExtra("size",intent.getStringExtra("size"))
                            putExtra("type","clean")
                        })
                        finish()
                    }
                )
            }
            start()
        }

        job=GlobalScope.launch {
            while (true){
                withContext(Dispatchers.Main){
                    val randomAppInfo = AppListManager.randomAppInfo()
                    if (null!=randomAppInfo){
                        iv_app_icon.setImageDrawable(randomAppInfo.icon)
                    }
                }
                delay(1000L)
            }
        }
    }

    private fun stopAnimator(){
        animator?.cancel()
        animator=null
        deleteAnimator?.removeAllUpdateListeners()
        deleteAnimator?.cancel()
        deleteAnimator=null
    }

    override fun onPause() {
        super.onPause()
        deleteAnimator?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (deleteAnimator?.isPaused==true){
            deleteAnimator?.resume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        stopAnimator()
    }
}