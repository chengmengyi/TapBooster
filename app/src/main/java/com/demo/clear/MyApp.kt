package com.demo.clear

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import com.demo.clear.conf.OnlineConf
import com.demo.clear.service.NotifiService
import com.demo.clear.util.AppListManager
import com.demo.clear.util.RegisterAc
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.tencent.mmkv.MMKV

lateinit var myApp: MyApp
class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        myApp=this
        Firebase.initialize(this)
        MobileAds.initialize(this)
        MMKV.initialize(this)
        RegisterAc.register(this)
        OnlineConf.readOnlineConf()
        AppListManager.loadApp(this)

        ContextCompat.startForegroundService(this, Intent(this,NotifiService::class.java))
    }
}