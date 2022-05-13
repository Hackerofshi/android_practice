package com.shixin.ui.jetpack.hilt.di.module

import com.shixin.ui.jetpack.hilt.di.qualifiers.AppScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @AppScope
    @Provides
    fun provide(): String {
        return hashCode().toString()
    }
}