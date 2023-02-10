package com.demo.clear.activity

import android.content.Intent
import android.view.Gravity
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.demo.clear.R
import com.demo.clear.activity.clean.CleanActivity
import com.demo.clear.activity.clean.CleanGouActivity
import com.demo.clear.activity.clean.CleanSuccessActivity
import com.demo.clear.adapter.HomeFuncAdapter
import com.demo.clear.admob.ShowNativeAd
import com.demo.clear.base.BaseActivity
import com.demo.clear.conf.LocalConf
import com.demo.clear.util.*
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home_content.*
import kotlinx.android.synthetic.main.home_drawer.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class HomeActivity:BaseActivity(R.layout.activity_home) {

    private val showHomeNativeAd by lazy { ShowNativeAd(LocalConf.HOME_NATIVE,this,false) }

    override fun viewCreated() {
        immersionBar.statusBarView(top).init()
        EventBus.getDefault().register(this)
        setFuncAdapter()
        readMemory()
        setClickListener()
        fromNotification(intent.getStringExtra("type") ?: "")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        fromNotification(intent?.getStringExtra("type") ?: "")
    }

    private fun fromNotification(fromNotificationType:String){
        if (fromNotificationType.isNotEmpty()){
            when(fromNotificationType){
                "clean"->{
                    toCleanAc()
                }
            }
        }
    }

    private fun setClickListener(){
        iv_set.setOnClickListener { if (!drawerOpen()) draw_layout.openDrawer(Gravity.LEFT) }
        iv_center.setOnClickListener {
            if (drawerOpen()) return@setOnClickListener
            toCleanAc()
        }
        llc_contact.setOnClickListener { contact() }
        llc_update.setOnClickListener { update() }
        llc_agree.setOnClickListener {
            startActivity(Intent(this,UrlActivity::class.java).apply {
                putExtra("url",LocalConf.agree)
            })
        }
    }

    private fun readMemory(){
        if (MMKVManager.canClean()){
            tv_percent.show(true)
            iv_scan.show(false)
            iv_center.isSelected=true
            tv_ram.isSelected=true
        }else{
            tv_percent.show(false)
            iv_scan.show(true)
            iv_center.isSelected=false
            tv_ram.isSelected=false
            iv_center.setImageResource(R.drawable.home2)
        }
        val totalMemory = getTotalMemory()
        val usedMemory = getUsedMemory()
        tv_percent.text="${usedMemory*100/totalMemory}%"
        tv_ram.text="${getMemoryStr(usedMemory)}/${getMemoryStr(totalMemory)}"
    }

    private fun setFuncAdapter(){
        rv.apply {
            layoutManager=GridLayoutManager(this@HomeActivity,2)
            adapter=HomeFuncAdapter(this@HomeActivity){ clickFuncItem(it) }
        }
    }

    private fun clickFuncItem(type:Int){
        //1Boost 2Clean 3Battery Saver  4Security
        if (drawerOpen()) return
        when(type){
            2->toCleanAc()
        }
    }

    private fun toCleanAc(){
        requestWriteExternalPermission{
            if (MMKVManager.canClean()){
                startActivity(Intent(this,CleanActivity::class.java))
            }else{
                startActivity(Intent(this, CleanGouActivity::class.java))
            }
        }
    }

    private fun requestWriteExternalPermission(granted:()->Unit) {
        RegisterAc.banHotLoad=true
        XXPermissions.with(this)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    RegisterAc.banHotLoad=false
                    if (!allGranted) {
                        toast("Please agree with the authority")
                        return
                    }
                    granted.invoke()
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    RegisterAc.banHotLoad=false
                    if (doNotAskAgain) {
                        toast("Please agree with the authority")
                        XXPermissions.startPermissionActivity(this@HomeActivity, permissions)
                    } else {
                        toast("Please agree with the authority")
                    }
                }
            })
    }


    private fun drawerOpen()=draw_layout.isOpen

    @Subscribe
    fun onEvent(s: String) {
        if (s=="clean_success"){
            readMemory()
        }
    }


    override fun onResume() {
        super.onResume()
        RegisterAc.banHotLoad=false
        if (LimitManager.refreshNativeAd(LocalConf.HOME_NATIVE)){
            showHomeNativeAd.loopCheck()
        }

    }

    override fun onBackPressed() {
        ActivityUtils.finishAllActivities()
    }

    override fun onDestroy() {
        super.onDestroy()
        showHomeNativeAd.stopCheck()
        EventBus.getDefault().unregister(this)
        LimitManager.setRefreshBool(LocalConf.HOME_NATIVE,true)
    }
}