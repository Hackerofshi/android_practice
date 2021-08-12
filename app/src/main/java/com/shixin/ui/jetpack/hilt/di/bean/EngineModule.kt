package com.shixin.ui.jetpack.hilt.di.bean

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Qualifier



@Module
@InstallIn(ActivityComponent::class)
abstract class EngineModule {


    //抽象函数加上@binds注解，这样hilt才能识别它
    @BindGasEngine
    @Binds
    abstract fun bindGasEngine(gasEngine: GasEngine): Engine

    @BindElectricEngine
    @Binds
    abstract fun bindElectricEngine(electricEngine: ElectricEngine): Engine

}