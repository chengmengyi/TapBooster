package com.demo.clear.util

import android.content.Context
import android.text.format.Formatter
import android.util.DisplayMetrics
import android.util.Log
import java.io.IOException

import java.io.BufferedReader

import java.io.FileReader
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.demo.clear.conf.LocalConf
import java.lang.Exception


fun logClear(string: String){
    Log.e("qwer",string)
}

fun Context.toast(string: String){
    Toast.makeText(this,string,Toast.LENGTH_SHORT).show()
}

fun View.show(show:Boolean){
    visibility=if (show)View.VISIBLE else View.GONE
}

fun Context.displayMetrics(){
    val metrics: DisplayMetrics = resources.displayMetrics
    val td = metrics.heightPixels / 760f
    val dpi = (160 * td).toInt()
    metrics.density = td
    metrics.scaledDensity = td
    metrics.densityDpi = dpi
}

//总运行内存
fun getTotalMemory(): Long {
    val str1 = "/proc/meminfo" // 系统内存信息文件
    val str2: String
    val arrayOfString: Array<String>
    var initial_memory: Long = 0
    try {
        val localFileReader = FileReader(str1)
        val localBufferedReader = BufferedReader(localFileReader, 8192)
        str2 = localBufferedReader.readLine() // 读取meminfo第一行，系统总内存大小
        arrayOfString = str2.split("\\s+".toRegex()).toTypedArray()
        for (num in arrayOfString) {
            Log.i(str2, num + "\t")
        }
        // 获得系统总内存，单位是KB
        val i = Integer.valueOf(arrayOfString[1]).toInt()
        //int值乘以1024转换为long类型
        initial_memory = i.toLong() * 1024
        localBufferedReader.close()
    } catch (e: IOException) {
    }
    return initial_memory
}

//可用运行内存
fun Context.getAvailMemory(): Long {
    val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val mi = ActivityManager.MemoryInfo()
    am.getMemoryInfo(mi)
    // mi.availMem; 当前系统的可用内存
    return mi.availMem
}

fun Context.getMemoryStr(size:Long):String=Formatter.formatFileSize(this, size)

fun Context.getUsedMemory():Long{
    val totalMemory = getTotalMemory()
    val availMemory = getAvailMemory()
    val l = totalMemory - availMemory
    if (l in 0..totalMemory){
        return l
    }
    return 100
}

fun Context.contact(){
    try {
        val uri = Uri.parse("mailto:${LocalConf.email}")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(intent)
    }catch (e: Exception){
        toast("Contact us by email：${LocalConf.email}")
    }
}

fun Context.update(){
    val packName = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).packageName
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://play.google.com/store/apps/details?id=$packName")
    }
    startActivity(intent)
}




