package com.demo.clear.admob

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.blankj.utilcode.util.SizeUtils
import com.demo.clear.R
import com.demo.clear.base.BaseActivity
import com.demo.clear.util.LimitManager
import com.demo.clear.util.logClear
import com.demo.clear.util.show
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.*

class ShowNativeAd(
    private val type:String,
    private val baseActivity: BaseActivity,
    private val showDesc:Boolean
) {

    private var loopJob:Job?=null
    private var lastNativeAd:NativeAd?=null


    fun loopCheck(){
        LoadAdmobManager.load(type)
        stopCheck()
        loopJob= GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if (!baseActivity.resume){
                return@launch
            }
            while (true) {
                if (!isActive) {
                    break
                }

                val adResByType = LoadAdmobManager.getAdResByType(type)
                if(baseActivity.resume && null!=adResByType && adResByType is NativeAd){
                    cancel()
                    lastNativeAd?.destroy()
                    lastNativeAd=adResByType
                    starShowNativeAd(adResByType)
                }

                delay(1000L)
            }
        }
    }

    private fun starShowNativeAd(nativeAd:NativeAd){
        logClear("start show $type ad")
        val viewNative = baseActivity.findViewById<NativeAdView>(R.id.native_ad_view)
        viewNative.iconView=baseActivity.findViewById(R.id.native_ad_logo)
        (viewNative.iconView as ImageFilterView).setImageDrawable(nativeAd.icon?.drawable)

        viewNative.callToActionView=baseActivity.findViewById(R.id.native_ad_install)
        (viewNative.callToActionView as AppCompatTextView).text=nativeAd.callToAction

        viewNative.mediaView=baseActivity.findViewById(R.id.native_ad_media)
        nativeAd.mediaContent?.let {
            viewNative.mediaView?.apply {
                setMediaContent(it)
                setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View?, outline: Outline?) {
                        if (view == null || outline == null) return
                        outline.setRoundRect(
                            0,
                            0,
                            view.width,
                            view.height,
                            SizeUtils.dp2px(8F).toFloat()
                        )
                        view.clipToOutline = true
                    }
                }
            }
        }


        if (showDesc){
            viewNative.bodyView=baseActivity.findViewById(R.id.native_ad_desc)
            (viewNative.bodyView as AppCompatTextView).text=nativeAd.body
        }

        viewNative.headlineView=baseActivity.findViewById(R.id.native_ad_title)
        (viewNative.headlineView as AppCompatTextView).text=nativeAd.headline

        viewNative.setNativeAd(nativeAd)
        baseActivity.findViewById<AppCompatImageView>(R.id.native_ad_cover).show(false)

        LimitManager.updateShow()
        LoadAdmobManager.removeAd(type)
        LoadAdmobManager.load(type)
        LimitManager.setRefreshBool(type,false)
    }


    fun stopCheck(){
        loopJob?.cancel()
        loopJob=null
    }
}