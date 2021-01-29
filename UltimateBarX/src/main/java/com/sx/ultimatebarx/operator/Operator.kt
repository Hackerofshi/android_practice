package com.sx.ultimatebarx.operator

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.sx.ultimatebarx.BarConfig
import java.util.function.BinaryOperator

interface Operator {
    fun applyStatusBar()

    fun applyNavigationBar()

    fun config(config: BarConfig):Operator

    fun transparent():Operator

    fun light(light:Boolean):Operator

    fun fitWindow(fitWindow:Boolean) :Operator

    fun drawableRes(@DrawableRes drawableRes: Int):Operator

    fun colorRes(@ColorRes colorRes:Int):Operator

    fun color(@ColorInt color:Int):Operator

}