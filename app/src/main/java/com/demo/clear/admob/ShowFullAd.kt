package com.demo.clear.admob

import com.demo.clear.base.BaseActivity
import com.demo.clear.conf.LocalConf
import com.demo.clear.util.logClear
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd

class ShowFullAd(private val type:String) {

    fun show(
        baseActivity: BaseActivity,
        emptyAdCallback:Boolean=false,
        showingAd:()->Unit={},
        closeAd:()->Unit
    ){
        val adByType = LoadAdmobManager.getAdResByType(type)
        if (null!=adByType){
            if (LocalConf.fullAdShowing||!baseActivity.resume){
                return
            }
            logClear("start show $type ad")
            showingAd.invoke()
            when(adByType){
                is InterstitialAd ->{
                    adByType.fullScreenContentCallback= ShowFullAdCallback(type,baseActivity,closeAd)
                    adByType.show(baseActivity)
                }
                is AppOpenAd ->{
                    adByType.fullScreenContentCallback= ShowFullAdCallback(type,baseActivity,closeAd)
                    adByType.show(baseActivity)
                }
            }
        }else{
            if (emptyAdCallback){
                closeAd.invoke()
            }
        }
    }
}