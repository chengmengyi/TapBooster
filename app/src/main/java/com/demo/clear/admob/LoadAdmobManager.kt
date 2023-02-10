package com.demo.clear.admob

import com.demo.clear.bean.AdResBean
import com.demo.clear.bean.AdResultBean
import com.demo.clear.conf.LocalConf
import com.demo.clear.conf.OnlineConf
import com.demo.clear.myApp
import com.demo.clear.util.LimitManager
import com.demo.clear.util.logClear
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions
import org.json.JSONObject

object LoadAdmobManager {
    private val loadingAdList= arrayListOf<String>()
    private val adResultMap= hashMapOf<String,AdResultBean>()

    fun load(type:String,openAgain:Boolean=true){
        if (LimitManager.hasLimit()){
            logClear("limit")
            return
        }
        if (loadingAdList.contains(type)){
            logClear("$type loading")
            return
        }

        if (adResultMap.containsKey(type)){
            val adResultBean = adResultMap[type]
            if (null!=adResultBean?.ad){
                if(adResultBean.expired()){
                    removeAd(type)
                }else{
                    logClear("$type has cache")
                    return
                }
            }
        }

        val adListByType = getAdListByType(type)
        if(adListByType.isEmpty()){
            logClear("$type ad list empty")
            return
        }
        loadingAdList.add(type)
        loopLoad(type,adListByType.iterator(),openAgain)
    }

    private fun loopLoad(type: String, iterator: Iterator<AdResBean>, openAgain: Boolean){
        loadAdByType(type,iterator.next()){
            if (it==null){
                if (iterator.hasNext()){
                    loopLoad(type,iterator,openAgain)
                }else{
                    loadingAdList.remove(type)
                    if (type== LocalConf.OPEN&&openAgain){
                        load(type,openAgain = false)
                    }
                }
            }else{
                loadingAdList.remove(type)
                adResultMap[type]=it
            }
        }
    }

    private fun loadAdByType(type:String,adResBean: AdResBean,result:(bean:AdResultBean?)->Unit){
        logClear("start load $type ad,${adResBean.toString()}")
        when(adResBean.tapo_type){
            "open"-> loadOpen(type, adResBean, result)
            "itn"-> loadInterstitialAd(type, adResBean, result)
            "nti"-> loadNativeAd(type, adResBean, result)
        }
    }

    private fun loadOpen(type:String,adResBean: AdResBean,result:(bean:AdResultBean?)->Unit){
        AppOpenAd.load(
            myApp,
            adResBean.tapo_id,
            AdRequest.Builder().build(),
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback(){
                override fun onAdLoaded(p0: AppOpenAd) {
                    logClear("load $type ad success")
                    result.invoke(AdResultBean(ad = p0, loadTime = System.currentTimeMillis()))
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    logClear("load $type fail,${p0.message}")
                    result.invoke(null)
                }
            }
        )
    }

    private fun loadInterstitialAd(type:String,adResBean: AdResBean,result:(bean:AdResultBean?)->Unit){
        InterstitialAd.load(
            myApp,
            adResBean.tapo_id,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    logClear("load $type fail,${p0.message}")
                    result.invoke(null)
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    logClear("load $type ad success")
                    result.invoke(AdResultBean(ad = p0, loadTime = System.currentTimeMillis()))
                }
            }
        )
    }

    private fun loadNativeAd(type:String,adResBean: AdResBean,result:(bean:AdResultBean?)->Unit){
        AdLoader.Builder(
            myApp,
            adResBean.tapo_id,
        ).forNativeAd {
            logClear("load $type ad success")
            result.invoke(AdResultBean(ad = it, loadTime = System.currentTimeMillis()))
        }
            .withAdListener(object : AdListener(){
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    logClear("load $type fail,${p0.message},${p0.code}")
                    result.invoke(null)
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    LimitManager.updateClick()
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(
                        NativeAdOptions.ADCHOICES_BOTTOM_LEFT
                    )
                    .build()
            )
            .build()
            .loadAd(AdRequest.Builder().build())
    }



    private fun getAdListByType(type: String):List<AdResBean>{
        val list= arrayListOf<AdResBean>()
        try {
            val jsonArray = JSONObject(OnlineConf.getAdJson()).getJSONArray(type)
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                list.add(
                    AdResBean(
                        jsonObject.optString("tapo_plat"),
                        jsonObject.optString("tapo_id"),
                        jsonObject.optString("tapo_type"),
                        jsonObject.optInt("tapo_prio"),
                    )
                )
            }
        }catch (e:Exception){
        }
        return list.filter { it.tapo_plat == "admob" }.sortedByDescending { it.tapo_prio }
    }

    fun getAdResByType(type: String)= adResultMap[type]?.ad

    fun removeAd(type: String){
        adResultMap.remove(type)
    }

    fun loadAllAd(){
        load(LocalConf.OPEN)
        load(LocalConf.CLEANING)
        load(LocalConf.HOME_NATIVE)
        load(LocalConf.RESULT_NATIVE)
    }
}