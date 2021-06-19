package com.shixin.ui.koin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shixin.ui.koin.datasource.RemoteDatasource
import com.shixin.ui.koin.presenter.MySimplePresenter
import com.shixin.ui.koin.reposity.HelloRepositoryImpl
import com.shixin.ui.rxjava.R
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