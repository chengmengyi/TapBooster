package com.demo.clear.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import com.demo.clear.bean.AppInfoBean
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AppListManager {
    private val appList= arrayListOf<AppInfoBean>()

    fun loadApp(context: Context){
        val packageName = context.packageName
        GlobalScope.launch {
            val packageManager: PackageManager = context.packageManager
            val list = packageManager.getInstalledPackages(0)
            appList.clear()
            for (p in list) {
                val bean = AppInfoBean()
                bean.icon = p.applicationInfo.loadIcon(packageManager)
                bean.name=packageManager.getApplicationLabel(p.applicationInfo).toString()
                bean.packageName=p.applicationInfo.packageName
                val flags = p.applicationInfo.flags
                if (flags and ApplicationInfo.FLAG_SYSTEM != 0) {

                } else {
                    if (bean.packageName!=packageName){
                        appList.add(bean)
                    }
                }
            }

        }
    }

    fun randomAppInfo():AppInfoBean?{
        if(appList.isNotEmpty()){
            return appList.random()
        }
        return null
    }
}