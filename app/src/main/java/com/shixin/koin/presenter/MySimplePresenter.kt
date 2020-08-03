package com.shixin.koin.presenter

import com.shixin.koin.reposity.HelloRepository
import com.shixin.koin.reposity.HelloRepositoryImpl
import org.koin.dsl.module

class MySimplePresenter(val repo: HelloRepository) {

    fun sayHello() = "${repo.giveHello()} from $this"
}


