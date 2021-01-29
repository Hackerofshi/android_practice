package com.sx.ultimatebarx.rom

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.sx.ultimatebarx.extension.commonNavigationBarExist

abstract class BaseRoom : Rom {
    override fun navigationBarExist(context: Context): Boolean {
        if (fullScreenGestureOn(context)) {
            if (screenIndicatorOn(context)) {
                return true
            }
            return false
        }
        return context.commonNavigationBarExist()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected open fun screenIndicatorOn(context: Context): Boolean = false

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected abstract fun fullScreenGestureOn(context: Context): Boolean
}