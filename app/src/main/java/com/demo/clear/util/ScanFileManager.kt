package com.demo.clear.util

import android.content.pm.PackageManager
import android.os.Environment
import com.demo.clear.R
import com.demo.clear.bean.CleanBean
import com.demo.clear.myApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ScanFileManager {
    private var scanJob:Job?=null
    private val filters = ArrayList<String>()

    init {
        val folders=listOf("Logs","logs","temp","Temporary","temporary","supersonicads","Cache","Analytics","thumbnails?",
            "mobvista","UnityAdsVideoCache","albumthumbs?","LOST.DIR",".Trash","desktop.ini","leakcanary",".DS_Store",
        ".spotlight-V100","fseventsd","Bugreport","bugreports","cache","splashad")
        val files=listOf(".tmp",".log",".exo","thumbs?.db",".thumb[0-9]","splashad")
        filters.clear()
        for (folder in folders) filters.add(getRegexForFolder(folder))
        for (file in files) filters.add(getRegexForFile(file))
        filters.add(getRegexForFile(".apk"))
    }

    fun startScan(
        dirCallback:(path:String)->Unit,
        refresh:(title:String,file:File)->Unit,
        finish:()->Unit
    ){
        scanJob=GlobalScope.launch {
            val externalStorageDirectory = Environment.getExternalStorageDirectory()
            val listFiles = getListFiles(externalStorageDirectory)
            for (file in listFiles){
                delay(10L)
                dirCallback.invoke(file.parent)
                if (filter(file)){
                    when {
                        file.isAppCacheFile -> {
                            refresh.invoke("App Cache",file)
                        }
                        file.isApkFile -> {
                            refresh.invoke("Apk Files",file)
                        }
                        file.isLogFile -> {
                            refresh.invoke("Log Files",file)
                        }
                        file.isTempFile -> {
                            refresh.invoke("Temp Files",file)
                        }
                        file.isAppResidualFile() -> {
                            refresh.invoke("APP Residual",file)
                        }
                        else -> {
                            refresh.invoke("AD Junk",file)
                        }
                    }
                }
            }
            finish.invoke()
        }
    }

    private fun filter(file: File?): Boolean {
        if (file != null) {
            try {
                val filterIterator = filters.iterator()
                while (filterIterator.hasNext()) {
                    val filter = filterIterator.next()
                    if (file.absolutePath.lowercase(Locale.getDefault()).matches(filter.lowercase(Locale.getDefault()).toRegex()))
                        return true
                }
            } catch (e: NullPointerException) {
                return false
            }
        }
        return false
    }

    private fun getListFiles(parentDirectory: File): List<File> {
        val inFiles = ArrayList<File>()
        val files = parentDirectory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file != null) {
                    if (file.isDirectory) {
//                        inFiles.add(file)
                        inFiles.addAll(getListFiles(file))
                    } else inFiles.add(file)
                }
            }
        }
        return inFiles
    }

    private fun getRegexForFolder(folder: String): String {
        return ".*(\\\\|/)$folder(\\\\|/|$).*"
    }

    private fun getRegexForFile(file: String): String {
        return ".+" + file.replace(".", "\\.") + "$"
    }

    val File.isAppCacheFile: Boolean get() = absolutePath.contains("cache", true)
    val File.isApkFile: Boolean get() = extension.lowercase() == "apk"
    val File.isLogFile: Boolean get() = extension.lowercase() == "log"
    val File.isAdFile: Boolean get() = false
    val File.isTempFile: Boolean get() = extension.lowercase() == "tmp"
    suspend fun File.isAppResidualFile(): Boolean {
        return parentFile != null
                && parentFile?.parentFile != null
                && "data" == parentFile?.name
                && "Android" == parentFile?.parentFile?.name
                && ".nomedia" != name
                && !installedPackages.contains(name)
    }

    private val installedPackages: List<String>
        get() {
            val pm = myApp.packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val packagesString: MutableList<String> = ArrayList()
            for (packageInfo in packages) {
                packagesString.add(packageInfo.packageName)
            }
            return packagesString
        }


    fun stopScan(){
        scanJob?.cancel()
        scanJob=null
    }
}