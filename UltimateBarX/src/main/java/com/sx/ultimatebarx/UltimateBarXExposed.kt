package com.sx.ultimatebarx

import android.os.Build
import android.view.View
import com.sx.ultimatebarx.extension.navigationHeight
import com.sx.ultimatebarx.extension.statusBarHeight


val statusBarHeight: Int
    get() = UltimateBarXManager.getInstance().context.statusBarHeight

val navigationBarHeight: Int
    get() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return 0
        }
        val rom = UltimateBarXManager.getInstance().rom
        val context = UltimateBarXManager.getInstance().context
        if (!rom.navigationBarExist(context))
            return 0

        return UltimateBarXManager.getInstance().context.navigationHeight
    }

fun View.addStatusBarTopPadding() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        addStatusBarTopPadding()
}

fun View.addNavigationBarBottomPadding()
{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        addNavigationBarBottomPadding()
}