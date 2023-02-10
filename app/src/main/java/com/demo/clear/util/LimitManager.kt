package com.demo.clear.util

import com.tencent.mmkv.MMKV
import java.text.SimpleDateFormat
import java.util.*

object LimitManager {
    private var click=0
    private var show=0

    private var maxClick=15
    private var maxShow=50

    private val refreshMap= hashMapOf<String,Boolean>()

    fun refreshNativeAd(type:String)=refreshMap[type]?:true

    fun setRefreshBool(type: String,boolean: Boolean){
        refreshMap[type]=boolean
    }

    fun resetRefreshMap(){
        refreshMap.clear()
    }

    fun setMaxNum(maxClick:Int,maxShow:Int){
        this.maxClick=maxClick
        this.maxShow=maxShow
    }

    fun hasLimit()= click >= maxClick||show >= maxShow

    fun updateClick(){
        click++
        MMKV.defaultMMKV().encode(key("tapo_click"), click)
    }

    fun updateShow(){
        show++
        MMKV.defaultMMKV().encode(key("tapo_show"), show)
    }

    fun readLocalNum(){
        click= MMKV.defaultMMKV().decodeInt(key("tapo_click"),0)
        show= MMKV.defaultMMKV().decodeInt(key("tapo_show"),0)
    }

    private fun key(key:String)="${SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))}_$key"
}