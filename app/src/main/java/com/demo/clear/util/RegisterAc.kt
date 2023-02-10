package com.demo.clear.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.demo.clear.activity.HomeActivity
import com.demo.clear.activity.MainActivity
import com.demo.clear.activity.clean.CleanGouActivity
import com.demo.clear.activity.clean.CleaningActivity
import com.google.android.gms.ads.AdActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object RegisterAc {
    var front=true
    var banHotLoad=false
    private var reloadAc=false
    private var cleanJob: Job?=null
    
    fun register(application: Application){
        application.registerActivityLifecycleCallbacks(callback)
    }

    private val callback=object : Application.ActivityLifecycleCallbacks{
        private var pages=0
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {
            pages++
            cleanJob?.cancel()
            cleanJob=null
            if (pages==1){
                front=true
                if (reloadAc&&!banHotLoad){
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                }
                reloadAc=false
            }
        }

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            pages--
            if (pages<=0){
                front=false
                cleanJob= GlobalScope.launch {
                    delay(3000L)
                    reloadAc=true
                    ActivityUtils.finishActivity(MainActivity::class.java)
                    ActivityUtils.finishActivity(CleaningActivity::class.java)
                    ActivityUtils.finishActivity(CleanGouActivity::class.java)
                    ActivityUtils.finishActivity(AdActivity::class.java)
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}
    }
}