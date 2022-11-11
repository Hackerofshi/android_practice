package com.shixin.ui.activityrecord

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TestActivitySingleTaskActvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.text = "test"
        setContentView(tv)

        tv.setOnClickListener {
            startActivity(Intent(this, TestActivityStandardActvity::class.java))
        }
    }
}