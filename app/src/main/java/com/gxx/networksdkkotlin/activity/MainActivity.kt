package com.gxx.networksdkkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.gxx.networksdkkotlin.R
import com.gxx.networksdkkotlin.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val mMainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMainViewModel.readBanner()

        this.findViewById<Button>(R.id.bt_start_second).setOnClickListener {
            startActivity(Intent(this,SecondActivity::class.java))
        }
    }
}