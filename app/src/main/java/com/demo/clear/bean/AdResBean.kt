package com.demo.clear.bean

class AdResBean(
    val tapo_plat:String,
    val tapo_id:String,
    val tapo_type:String,
    val tapo_prio:Int,
) {
    override fun toString(): String {
        return "AdResBean(clear_admob='$tapo_plat', clear_id='$tapo_id', clear_type='$tapo_type', clear_sort=$tapo_prio)"
    }
}