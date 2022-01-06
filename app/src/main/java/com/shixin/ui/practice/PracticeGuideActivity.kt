package com.shixin.ui.practice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.shixin.R
import kotlinx.android.synthetic.main.activity_practice_guide.*

class PracticeGuideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_guide)

        btn_lambda.setOnClickListener {
            startActivity(Intent(this, LambdaActivity::class.java))
        }

        btn_notify.setOnClickListener {
            startActivity(Intent(this, NotifyActivity::class.java))
        }
        btn_fragment.setOnClickListener {
            startActivity(Intent(this, FragmentSourceActivity::class.java))
        }
    }
}


