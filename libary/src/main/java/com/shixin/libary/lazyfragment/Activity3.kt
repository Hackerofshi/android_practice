package com.shixin.libary.lazyfragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jennifer.andy.androidxlazyload.*

import com.shixin.libary.R
import com.shixin.libary.baseKt.ext.*


/**
 * Author:  andy.xwt
 * Date:    2020-01-14 14:48
 * Description:
 */

class Activity3 : AppCompatActivity(), View.OnClickListener {

    private lateinit var fragmentsMap: Map<String, Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lazy_fragment)
        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.btn_1).setOnClickListener(this)
        findViewById<Button>(R.id.btn_2).setOnClickListener(this)
        findViewById<Button>(R.id.btn_3).setOnClickListener(this)
        fragmentsMap = generate123Fragments()
        loadFragments(R.id.fl_container, 0, *fragmentsMap.values.toTypedArray())
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_1 -> {
                showHideFragment(fragmentsMap.getValue(FRAGMENT_ONE))
            }
            R.id.btn_2 -> {
                showHideFragment(fragmentsMap.getValue(FRAGMENT_TWO))
            }
            R.id.btn_3 -> {
                showHideFragment(fragmentsMap.getValue(FRAGMENT_THREE))
            }
        }
    }
}