package com.gxx.networksdkkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.Gson
import com.gxx.networksdkkotlin.R
import com.gxx.networksdkkotlin.viewmodel.MainViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val mMainViewModel:MainViewModel  by  viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.findViewById<Button>(R.id.bt_net_work_start).setOnClickListener {
            lifecycleScope.launch{
                //合并请求
                val deffer1 =  async {
                    mMainViewModel.methodReturn()
                }
                val deffer2 =  async {
                    mMainViewModel.methodReturn()
                }
                val startTime = System.currentTimeMillis()
                Log.d(TAG,"合并请求开始")
                Log.d(TAG,"deffer1->${Gson().toJson(deffer1.await().first())}，耗时->${System.currentTimeMillis() - startTime}")
                Log.d(TAG,"deffer2->${Gson().toJson(deffer2.await().first())}，耗时->${System.currentTimeMillis() - startTime}")
                Log.d(TAG,"合并请求结束")
            }

        }

        this.findViewById<Button>(R.id.bt_start_second).setOnClickListener {
            startActivity(Intent(this,SecondActivity::class.java))
        }

        lifecycleScope.launch {
            mMainViewModel.bannerShareFlow.collect{
                Log.d(TAG,"拿到的，banner数据=${Gson().toJson(it)}")
                findViewById<TextView>(R.id.tv_show_data).text = GsonUtils.toJson(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mMainViewModel.readBanner();
    }
}