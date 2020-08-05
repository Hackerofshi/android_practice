package com.shixin.koin.module

import com.shixin.koin.datasource.RemoteDatasource
import com.shixin.koin.presenter.MySimplePresenter
import com.shixin.koin.reposity.HelloRepository
import com.shixin.koin.reposity.HelloRepositoryImpl
import com.win.lib_net.net.RetrofitClient
import org.koin.dsl.module

/**
 * 声明module
 */
object AppModule {

    val appModule = module {



        //datasource实例化，单例
        single { RetrofitClient.instance }

        // single instance of HelloRepository
        single<HelloRepository> { HelloRepositoryImpl() }

        // Simple Presenter Factory
        factory { MySimplePresenter(get()) }
    }
}


