package com.sx.ultimatebarx

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.sx.ultimatebarx.operator.Operator
import com.sx.ultimatebarx.operator.OperatorProvider

class UltimateBarX {
    companion object{
        @JvmStatic
        fun with(activity: FragmentActivity):Operator = OperatorProvider.create(activity)


        @JvmStatic
        fun width(fragment:Fragment):Operator  = OperatorProvider.create(fragment)

        @JvmStatic
        fun get(fragment: Fragment):Operator = OperatorProvider.get(fragment)

        @JvmStatic
        fun getStatusBarConfig(activity: FragmentActivity): BarConfig = UltimateBarXManager.getInstance().getStatusBarConfig(activity)

        @JvmStatic
        fun getNavigationBarConfig(activity: FragmentActivity): BarConfig = UltimateBarXManager.getInstance().getNavigationBarConfig(activity)

        @JvmStatic
        fun getStatusBarConfig(fragment: Fragment): BarConfig = UltimateBarXManager.getInstance().getStatusBarConfig(fragment)

        @JvmStatic
        fun getNavigationBarConfig(fragment: Fragment): BarConfig = UltimateBarXManager.getInstance().getNavigationBarConfig(fragment)

        @JvmStatic
        fun getStatusBarHeight() = statusBarHeight

        @JvmStatic
        fun getNavigationBarHeight() = navigationBarHeight

        @JvmStatic
        fun addStatusBarTopPadding(target: View) = target.addStatusBarTopPadding()

        @JvmStatic
        fun addNavigationBarBottomPadding(target: View) = target.addNavigationBarBottomPadding()
    }

}