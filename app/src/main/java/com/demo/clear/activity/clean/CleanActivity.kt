package com.demo.clear.activity.clean

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.clear.R
import com.demo.clear.adapter.CleanAdapter
import com.demo.clear.base.BaseActivity
import com.demo.clear.bean.CleanBean
import com.demo.clear.util.*
import kotlinx.android.synthetic.main.activity_clean.*

class CleanActivity:BaseActivity(R.layout.activity_clean) {
    private val scanFile by lazy { ScanFileManager() }

    private val cleanList= arrayListOf(
        CleanBean(R.drawable.icon_cache,"App Cache"),
        CleanBean(R.drawable.icon_file,"Apk Files"),
        CleanBean(R.drawable.icon_log,"Log Files"),
        CleanBean(R.drawable.icon_junk,"AD Junk"),
        CleanBean(R.drawable.icon_temp,"Temp Files"),
        CleanBean(R.drawable.icon_residual,"APP Residual"),
        CleanBean(R.drawable.icon_used,"RAM Used"),
    )

    private val cleanAdapter by lazy { CleanAdapter(this,cleanList){ clickItem() } }

    override fun viewCreated() {
        immersionBar.statusBarView(top).init()
        setAdapter()
        startScan()
        iv_back.setOnClickListener { finish() }
        tv_clean_btn.setOnClickListener {
            val chooseList = cleanAdapter.getChooseList()
            if(chooseList.isNotEmpty()){
                startActivity(Intent(this,CleaningActivity::class.java).apply {
                    putExtra("list",chooseList)
                    putExtra("size",getMemoryStr(cleanAdapter.getChooseSize()))
                })
                finish()
            }

        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        startScan()
    }

    private fun clickItem(){
        tv_clean_btn.text="CLEAN JUNK ${getMemoryStr(cleanAdapter.getChooseSize())}"
    }

    private fun startScan(){
        scanFile.startScan(
            dirCallback = {
                runOnUiThread { tv_dir_path.text="Scan: $it" }
            },
            refresh = { title, file ->
                cleanList.forEach {  bean->
                    if(bean.title==title){
                        bean.size+=file.length()
                        bean.path.add(file.path)
                    }
                }
                runOnUiThread { cleanAdapter.notifyDataSetChanged() }
            },
            finish = {
                runOnUiThread {
                    var size=0L
                    cleanList.forEach {
                        if (it.title=="RAM Used"){
                            it.size=getUsedMemory()
                        }
                        if (it.size>0){
                            it.select=true
                        }
                        size+=it.size
                        it.rotate=false
                    }
                    iv_clean_bg.isSelected=true
                    cleanAdapter.notifyDataSetChanged()
                    tv_all_size.text=getMemoryStr(size)
                    tv_clean_btn.show(true)
                    clickItem()
                }
            }
        )
    }

    private fun setAdapter(){
        rv_clean.apply {
            layoutManager=LinearLayoutManager(this@CleanActivity)
            adapter=cleanAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scanFile.stopScan()
    }
}