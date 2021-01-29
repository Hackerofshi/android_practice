package com.sx.ultimatebarx.view

import android.content.Context
import android.view.View

internal interface Creator {
    fun getStatusBarView(context: Context, fitWindow: Boolean): View
    fun getNavigationBarView(context: Context,fitWindow: Boolean):View
}