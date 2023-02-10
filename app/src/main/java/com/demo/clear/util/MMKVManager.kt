package com.demo.clear.util

import com.tencent.mmkv.MMKV

object MMKVManager {

    fun saveCleanTime(){
        MMKV.defaultMMKV().encode("cleanTime",System.currentTimeMillis())
    }

    fun canClean():Boolean{
        val lastTime = MMKV.defaultMMKV().decodeLong("cleanTime", 0L)
        val l = System.currentTimeMillis() - lastTime
        return l>2*60*1000
    }

    fun firstLoad()=MMKV.defaultMMKV().decodeBool("first",true)

    fun setFirstLoad(){
        MMKV.defaultMMKV().encode("first",false)
    }
}