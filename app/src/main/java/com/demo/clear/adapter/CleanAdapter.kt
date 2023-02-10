package com.demo.clear.adapter

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.clear.R
import com.demo.clear.bean.CleanBean
import com.demo.clear.util.getMemoryStr
import kotlinx.android.synthetic.main.item_clean.view.*

class CleanAdapter(
    private val context: Context,
    private val list:ArrayList<CleanBean>,
    private val click:()->Unit
):RecyclerView.Adapter<CleanAdapter.CleanView>() {

    fun getChooseList():ArrayList<CleanBean>{
        val choose= arrayListOf<CleanBean>()
        list.forEach {
            if (it.select){
                choose.add(it)
            }
        }
        return choose
    }

    fun getChooseSize():Long{
        var size=0L
        list.forEach {
            if (it.select){
                size+=it.size
            }
        }
        return size
    }

    inner class CleanView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener {
                val cleanBean = list[layoutPosition]
                if (cleanBean.size>0){
                    cleanBean.select=!cleanBean.select
                    click.invoke()
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleanView =
        CleanView(LayoutInflater.from(context).inflate(R.layout.item_clean,parent,false))

    override fun onBindViewHolder(holder: CleanView, position: Int) {
        with(holder.itemView){
            val cleanBean = list[position]
            iv_icon.setImageResource(cleanBean.icon)
            tv_title.text=cleanBean.title
            tv_size.text=context.getMemoryStr(cleanBean.size)
            if (cleanBean.rotate){
                iv_rotate.setImageResource(R.drawable.icon_refresh)
                if(null==cleanBean.objectAnimator){
                    cleanBean.objectAnimator= ObjectAnimator.ofFloat(iv_rotate, "rotation", 0f, 360f).apply {
                        duration=1000L
                        repeatCount= ValueAnimator.INFINITE
                        repeatMode= ObjectAnimator.RESTART
                        start()
                    }
                }else{
                    if (cleanBean.objectAnimator?.isPaused == true){
                        cleanBean.objectAnimator?.resume()
                    }
                }
            }else{
                cleanBean.objectAnimator?.cancel()
                cleanBean.objectAnimator=null
                iv_rotate.setImageResource(if (cleanBean.select) R.drawable.icon_sel else R.drawable.icon_uns)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}