package com.shixin.ui.koin.presenter

import com.shixin.ui.koin.reposity.HelloRepository

class MySimplePresenter(val repo: HelloRepository) {

    fun sayHello() = "${repo.giveHello()} from $this"
}


