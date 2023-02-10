package com.demo.clear.activity

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Paint
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.ActivityUtils
import com.demo.clear.R
import com.demo.clear.activity.boost.BoostScanActivity
import com.demo.clear.admob.LoadAdmobManager
import com.demo.clear.admob.ShowFullAd
import com.demo.clear.base.BaseActivity
import com.demo.clear.conf.LocalConf
import com.demo.clear.util.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main) {
    private var valueAnimator: ValueAnimator?=null
    private val showOpen by lazy { ShowFullAd(LocalConf.OPEN) }

    override fun viewCreated() {
        LimitManager.resetRefreshMap()
        LimitManager.readLocalNum()
        LoadAdmobManager.loadAllAd()
        checkFirstLoad()
        setClickListener()
    }

    private fun checkFirstLoad(){
        if(MMKVManager.firstLoad()){
            cl_start.show(true)
        }else{
            cl_start.show(false)
            startAnimator()
        }
    }

    private fun setClickListener(){
        tv_agree.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_agree.paint.isAntiAlias = true
        tv_terms.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_terms.paint.isAntiAlias = true
        tv_agree.setOnClickListener { toWeb(LocalConf.agree) }
        tv_terms.setOnClickListener { toWeb(LocalConf.terms) }
        tv_start.setOnClickListener {
            MMKVManager.setFirstLoad()
            startActivity(Intent(this,BoostScanActivity::class.java))
        }
    }

    private fun toWeb(url:String){
        startActivity(Intent(this,UrlActivity::class.java).apply {
            putExtra("url",url)
        })
    }

    private fun startAnimator(){
        valueAnimator=ValueAnimator.ofInt(0, 100).apply {
            duration = 8000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                launch_progress.progress = progress
                val pro = (10 * (progress / 100.0F)).toInt()
                if (pro in 2..7){
                    showOpen.show(
                        this@MainActivity,
                        showingAd = {
                            stopAnimator()
                            launch_progress.progress=100
                        },
                        closeAd = {
                            startHomeAc()
                        }
                    )
                }else if (pro>=8){
                    startHomeAc()
                }
            }
            start()
        }
    }

    private fun startHomeAc(){
        if (!ActivityUtils.isActivityExistsInStack(HomeActivity::class.java)){
            startActivity(Intent(this,HomeActivity::class.java))
        }
        finish()
    }

    private fun stopAnimator(){
        valueAnimator?.removeAllUpdateListeners()
        valueAnimator?.cancel()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        RegisterAc.banHotLoad=false
        if(valueAnimator?.isPaused==true){
            valueAnimator?.resume()
        }

    }

    override fun onPause() {
        super.onPause()
        valueAnimator?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnimator()
    }
}