package com.demo.clear.bean

import android.animation.ObjectAnimator
import java.io.Serializable

class CleanBean(
    val icon:Int,
    val title:String,
    var size:Long=0L,
    var path:ArrayList<String> = arrayListOf(),
    var rotate:Boolean=true,
    var select:Boolean=false,
    var objectAnimator: ObjectAnimator?=null
) :Serializable{
}