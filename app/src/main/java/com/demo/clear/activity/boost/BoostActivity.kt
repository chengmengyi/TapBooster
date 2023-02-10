package com.demo.clear.activity.boost

import android.content.Intent
import com.demo.clear.R
import com.demo.clear.activity.HomeActivity
import com.demo.clear.base.BaseActivity
import com.demo.clear.util.*
import kotlinx.android.synthetic.main.activity_boost.*

class BoostActivity:BaseActivity(R.layout.activity_boost) {
    override fun viewCreated() {
        immersionBar.statusBarView(top).init()
        getMemory()
        iv_back.setOnClickListener { onBackPressed() }
    }

    private fun getMemory(){
        val totalMemory = getTotalMemory()
        val usedMemory = getUsedMemory()
        val availMemory = getAvailMemory()
        val used = usedMemory * 100 / totalMemory
        val avail = availMemory * 100 / totalMemory
        pie.initSrc(floatArrayOf(used.toFloat(), avail.toFloat()), arrayOf(
            "#0C762D",
            "#4D0C762D",
        ), null)
        tv_used_percent.text="$used%"
        tv_free_ram.text=getMemoryStr(availMemory)
        tv_used_ram.text=getMemoryStr(usedMemory)
        llc_tips.show(used>=35)
        tv_clean.setOnClickListener {
            startActivity(Intent(this,BoostCleanActivity::class.java).apply {
                putExtra("size",tv_used_ram.text.toString())
            })
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this,HomeActivity::class.java))
    }
}