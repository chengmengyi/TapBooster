package com.demo.clear.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.clear.R
import com.demo.clear.bean.HomeFuncBean
import kotlinx.android.synthetic.main.item_home_func.view.*

class HomeFuncAdapter(
    private val context: Context,
    private val click:(type:Int)->Unit
):RecyclerView.Adapter<HomeFuncAdapter.FuncView>() {
    //1Boost 2Clean 3Battery Saver  4Security
    private val funcList= arrayListOf(
        HomeFuncBean(R.drawable.home_clean,"Clean",2)
    )

    inner class FuncView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener { click.invoke(funcList[layoutPosition].type) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuncView =
        FuncView(LayoutInflater.from(context).inflate(R.layout.item_home_func,parent,false))

    override fun onBindViewHolder(holder: FuncView, position: Int) {
        with(holder.itemView){
            val homeFuncBean = funcList[position]
            iv_title.text=homeFuncBean.title
            iv_icon.setImageResource(homeFuncBean.icon)
        }
    }

    override fun getItemCount(): Int = funcList.size
}