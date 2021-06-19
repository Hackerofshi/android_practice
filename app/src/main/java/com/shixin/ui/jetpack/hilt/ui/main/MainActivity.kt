package com.shixin.ui.jetpack.hilt.ui.main

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shixin.ui.jetpack.hilt.di.qualifiers.ActivityScope
import com.shixin.ui.jetpack.hilt.di.qualifiers.AppScope
import com.shixin.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main_kx) {

    private val TAG = this.javaClass.toString()

    @AppScope
    @Inject
    lateinit var appHash: String

    @ActivityScope
    @Inject
    lateinit var activityHash: String


    private val viewModel by viewModels<ActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        Log.v(TAG, "app : $appHash")
        Log.v(TAG, "activity : $activityHash")
        Log.v(TAG, "activity vm: $viewModel")
        Log.v(TAG, "activity vm repo: ${viewModel.repository}")
        viewModel.test()
    }

}