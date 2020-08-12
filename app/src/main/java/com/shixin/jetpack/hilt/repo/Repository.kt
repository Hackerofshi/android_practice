package com.shixin.jetpack.hilt.repo

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
        @ApplicationContext appContext: Context,
        @ActivityContext actContext: Context) {
}