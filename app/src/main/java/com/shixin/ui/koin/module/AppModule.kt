package com.shixin.ui.koin.module

import com.shixin.ui.koin.datasource.RemoteDatasource
import com.shixin.ui.koin.presenter.MySimplePresenter
import com.shixin.ui.koin.reposity.HelloRepository
import com.shixin.ui.koin.reposity.HelloRepositoryImpl
import com.win.lib_net.net.RetrofitClient
import org.koin.dsl.module

/**
 * 声明module
 */
object AppModule {

    val appModule = module {


        //datasource实例化，单例
        single { RetrofitClient.instance.create(RemoteDatasource::class.java) }

        // single instance of HelloRepository
        single<HelloRepository> { HelloRepositoryImpl() }

        // Simple Presenter Factory
        factory { MySimplePresenter(get()) }
    }
}


