package com.shixin.ui.jetpack.hilt.di.module

import com.shixin.ui.jetpack.hilt.di.qualifiers.FragmentScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {
    @FragmentScope
    @Provides
    fun provide(): String {
        return hashCode().toString()
    }
}