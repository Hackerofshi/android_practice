package com.shixin.koin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shixin.koin.datasource.RemoteDatasource
import com.shixin.koin.presenter.MySimplePresenter
import com.shixin.koin.reposity.HelloRepositoryImpl
import com.shixin.rxjava.R
import org.koin.android.ext.android.inject

class MySimpleKoinActivity : AppCompatActivity() {

    // Lazy injected MySimplePresenter
    val firstPresenter: MySimplePresenter by inject()
    val h: HelloRepositoryImpl by inject()
    val api:RemoteDatasource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_simple_koin)

    }
}