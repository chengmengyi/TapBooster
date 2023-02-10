package com.demo.clear.activity.boost

import android.animation.ValueAnimator
import android.content.Intent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.demo.clear.R
import com.demo.clear.activity.HomeActivity
import com.demo.clear.activity.clean.CleanSuccessActivity
import com.demo.clear.admob.LoadAdmobManager
import com.demo.clear.admob.ShowFullAd
import com.demo.clear.base.BaseActivity
import com.demo.clear.conf.LocalConf
import kotlinx.android.synthetic.main.activity_boost_clean.*

class BoostCleanActivity:BaseActivity(R.layout.activity_boost_clean) {
    private var animator: ValueAnimator?=null
    private val showFullAd by lazy { ShowFullAd(LocalConf.CLEANING) }

    override fun viewCreated() {
        LoadAdmobManager.load(LocalConf.CLEANING)
        immersionBar.statusBarView(top).init()
        startAnimator()
        iv_back.setOnClickListener { onBackPressed() }
    }

    private fun startAnimator(){
        animator=ValueAnimator.ofInt(0, 100).apply {
            duration = 5000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                tv_percent.text = "$progress%"
            }
            doOnEnd {
                showFullAd.show(
                    this@BoostCleanActivity,
                    emptyAdCallback = true,
                    closeAd = {
                        startActivity(
                            Intent(this@BoostCleanActivity,
                                CleanSuccessActivity::class.java).apply {
                            putExtra("size",intent.getStringExtra("size"))
                        })
                        finish()
                    }
                )
            }
            start()
        }
    }

    private fun stopAnimator(){
        animator?.removeAllUpdateListeners()
        animator?.cancel()
        animator=null
    }

    override fun onPause() {
        super.onPause()
        animator?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (animator?.isPaused==true){
            animator?.resume()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnimator()
    }
}