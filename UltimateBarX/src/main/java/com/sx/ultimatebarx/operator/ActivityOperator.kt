package com.sx.ultimatebarx.operator

import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.sx.ultimatebarx.BarConfig
import com.sx.ultimatebarx.core.addObserver
import com.sx.ultimatebarx.core.defaultNavigationBar
import com.sx.ultimatebarx.core.ultimateBarXInitialization
import com.sx.ultimatebarx.core.updateStatusBar
import com.sx.ultimatebarx.extension.setSystemUiFlagWithLight

internal class ActivityOperator private
constructor(private val activity: FragmentActivity, config: BarConfig) : BaseOperator(config) {

    companion object {
        internal fun newInstance(activity: FragmentActivity, config: BarConfig = BarConfig.newInstance()) = ActivityOperator(activity, config)
    }

    /**
     * 应用到状态栏
     */
    override fun applyStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        activity.ultimateBarXInitialization()
        val navLight = manager.getStatusBarConfig(activity).light
        activity.setSystemUiFlagWithLight(config.light, navLight)
        activity.updateStatusBar(config)
        activity.defaultNavigationBar()
        activity.addObserver()
    }

    override fun applyNavigationBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        activity.ultimateBarXInitialization()
        val navLight = manager.getNavigationBarConfig(activity).light
        activity.setSystemUiFlagWithLight(config.light, navLight)
        activity.updateStatusBar(config)
        activity.defaultNavigationBar()
        activity.addObserver()
    }
}