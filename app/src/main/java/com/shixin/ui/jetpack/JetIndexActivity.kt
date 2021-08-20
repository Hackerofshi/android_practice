package com.shixin.ui.jetpack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shixin.R
import com.shixin.ui.jetpack.flow.FlowDemoActivity
import com.shixin.ui.jetpack.hilt.ui.main.HiltMainActivity
import kotlinx.android.synthetic.main.activity_jet_index.*
import kotlinx.coroutines.flow.flow


class JetIndexActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_index)

        btn1.setOnClickListener {
            startActivity(Intent(this@JetIndexActivity, FlowDemoActivity::class.java));
        }
        btn2.setOnClickListener {
            startActivity(Intent(this@JetIndexActivity, HiltMainActivity::class.java));
        }
    }
}