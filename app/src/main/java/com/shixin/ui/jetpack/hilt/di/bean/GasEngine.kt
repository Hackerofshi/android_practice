package com.shixin.ui.jetpack.hilt.di.bean

import javax.inject.Inject
import javax.inject.Qualifier





class GasEngine @Inject constructor() :Engine {
    override fun start() {
        println("gas engine start")
    }

    override fun shutDown() {
        println("gas engine shutdown")
    }
}


class ElectricEngine @Inject constructor()  :Engine{
    override fun start() {
        println("electric engine start")
    }

    override fun shutDown() {
        println("electric engine shutdown")
    }


}