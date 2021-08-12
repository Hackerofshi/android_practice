package com.shixin.ui.jetpack.hilt.di.bean

import android.content.Context
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


//普通注入
/*
class Truck @Inject constructor() {

    fun deliver(){
        println("Truck is delivering cargo")
    }
}*/


//带参数的依赖注入
class Truck @Inject constructor(val driver: Driver) {


    //借助刚才定义的EngineModule,很明显将会注入一个GasEngine实例到engine字段当中
//    @Inject
//    lateinit var engine: Engine



    @BindGasEngine
    @Inject
    lateinit var gasEngine: Engine

    @BindElectricEngine
    @Inject
    lateinit var electricEngine: Engine

    fun deliver() {
        gasEngine.start()
        electricEngine.start()
        println("Truck is delivering cargo. Driven by $driver")
        gasEngine.shutdown()
        electricEngine.shutdown()
    }
}