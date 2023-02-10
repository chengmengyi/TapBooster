package com.demo.clear.admob

import com.demo.clear.base.BaseActivity
import com.demo.clear.conf.LocalConf
import com.demo.clear.util.LimitManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowFullAdCallback(
    private val type:String,
    private val baseActivity: BaseActivity,
    private val closeAd:()->Unit
): FullScreenContentCallback() {
    override fun onAdDismissedFullScreenContent() {
        super.onAdDismissedFullScreenContent()
        LocalConf.fullAdShowing=false
        clickCloseAd()
    }

    override fun onAdShowedFullScreenContent() {
        super.onAdShowedFullScreenContent()
        LocalConf.fullAdShowing=true
        LimitManager.updateShow()
        LoadAdmobManager.removeAd(type)
    }

    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
        super.onAdFailedToShowFullScreenContent(p0)
        LocalConf.fullAdShowing=false
        LoadAdmobManager.removeAd(type)
        clickCloseAd()
    }


    override fun onAdClicked() {
        super.onAdClicked()
        LimitManager.updateClick()
    }

    private fun clickCloseAd(){
        LoadAdmobManager.load(type)
        GlobalScope.launch(Dispatchers.Main) {
            delay(200L)
            if (baseActivity.resume){
                closeAd.invoke()
            }
        }
    }
}