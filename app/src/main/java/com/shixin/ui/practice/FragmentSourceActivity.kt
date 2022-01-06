package com.shixin.ui.practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shixin.R
import com.shixin.ui.practice.fragment.ItemFragment

class FragmentSourceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_source)

        supportFragmentManager.beginTransaction()
            .add(R.id.fl, ItemFragment.newInstance(1))
            .commitAllowingStateLoss()
    }
}