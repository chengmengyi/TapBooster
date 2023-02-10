package com.demo.clear.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.clear.util.displayMetrics
import com.gyf.immersionbar.ImmersionBar

abstract class BaseActivity(private val layoutId:Int):AppCompatActivity() {
    var resume=false
    lateinit var immersionBar: ImmersionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayMetrics()
        setContentView(layoutId)
        immersionBar= ImmersionBar.with(this).apply {
            statusBarAlpha(0f)
            autoDarkModeEnable(true)
            statusBarDarkFont(true)
            init()
        }
        viewCreated()
    }

    abstract fun viewCreated()

    override fun onResume() {
        super.onResume()
        resume=true
    }

    override fun onPause() {
        super.onPause()
        resume=false
    }

    override fun onStop() {
        super.onStop()
        resume=false
    }
}