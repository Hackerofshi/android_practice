package com.shixin.ui.jetpack.hilt.di.bean

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Qualifier



//定义两种类型的注解，用于区分同一种类型的类
@Qualifier
@Retention
annotation class BindGasEngine

@Qualifier
@Retention
annotation class BindElectricEngine

@Module
@InstallIn(ActivityComponent::class)
abstract class EngineModule {


    //抽象函数加上@binds注解，这样hilt才能识别它
    @BindGasEngine
    @Binds
    abstract fun bindEngine(gasEngine: GasEngine):Engine


    @BindElectricEngine
    @Binds
    abstract fun bindElectricEngine(electricEngine: BindElectricEngine):Engine

}