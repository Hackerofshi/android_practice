package com.shixin.ui.activityrecord

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TestActivityStandardActvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tv = TextView(this)
        tv.text = "TestActivityStandardActvity"
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(10, 10, 10, 10)
        tv.layoutParams = params


        val tv1 = TextView(this)
        tv1.text = "go to SingleTask"
        tv1.layoutParams = params



        val linearLayout = LinearLayout(this)
        linearLayout.orientation = 1

        linearLayout.addView(tv)
        linearLayout.addView(tv1)


        setContentView(linearLayout)

        tv.setOnClickListener {
            startActivity(Intent(this, TestActivityStandardActvity::class.java))
        }


        tv1.setOnClickListener {
            startActivity(Intent(this, TestActivitySingleTaskActvity::class.java))
        }

    }
}