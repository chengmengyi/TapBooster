package com.demo.clear.bean

class AdResultBean(
    val loadTime:Long=0,
    val ad:Any?=null
) {
    fun expired()=(System.currentTimeMillis() - loadTime) >=3600000L
}