package com.demo.clear.conf

import com.demo.clear.util.LimitManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.tencent.mmkv.MMKV
import org.json.JSONObject

object OnlineConf {

    fun readOnlineConf(){
//        val remoteConfig = Firebase.remoteConfig
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            if (it.isSuccessful){
//                parseAdJson(remoteConfig.getString("tapo_avd"))
//            }
//        }
    }

    private fun parseAdJson(json:String){
        try {
            val jsonObject = JSONObject(json)
            LimitManager.setMaxNum(jsonObject.optInt("tapo_click"),jsonObject.optInt("tapo_show"))
            MMKV.defaultMMKV().encode("tapo_avd",json)
        }catch (e:Exception){

        }
    }

    fun getAdJson():String{
        val s = MMKV.defaultMMKV().decodeString("tapo_avd") ?: ""
        if (s.isEmpty()) return LocalConf.localAd
        return s
    }
}