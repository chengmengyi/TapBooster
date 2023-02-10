package com.demo.clear.activity.boost

import android.animation.ValueAnimator
import android.content.Intent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.demo.clear.R
import com.demo.clear.base.BaseActivity
import kotlinx.android.synthetic.main.activity_boost_scan.*

class BoostScanActivity:BaseActivity(R.layout.activity_boost_scan) {
    private var animator: ValueAnimator?=null

    override fun viewCreated() {
        immersionBar.statusBarView(top).init()
        startAnimator()
//        iv_back.setOnClickListener { finish() }
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
                startActivity(Intent(this@BoostScanActivity,BoostActivity::class.java))
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

    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnimator()
    }
}