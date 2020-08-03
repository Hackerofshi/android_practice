package com.shixin.koin.module

import com.shixin.koin.presenter.MySimplePresenter
import com.shixin.koin.reposity.HelloRepository
import com.shixin.koin.reposity.HelloRepositoryImpl
import org.koin.dsl.module

object AppModule {

    val appModule = module {

        // single instance of HelloRepository
        single<HelloRepository> { HelloRepositoryImpl() }

        // Simple Presenter Factory
        factory { MySimplePresenter(get()) }
    }
}


