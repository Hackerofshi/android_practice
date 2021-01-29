package com.sx.ultimatebarx.extension

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowId
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity

/**
 * 扩展方法
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
internal fun FragmentActivity.setSystemUiFlagWithLight(StatusLight: Boolean, navigationBarLight: Boolean) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        return
    }
    window?.decorView?.systemUiVisibility = SystemUiFlag(StatusLight, navigationBarLight)
}

/**
 * 扩展方法，给FragmentActivity添加一个扩展方法
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
internal fun FragmentActivity.barTransparent() {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
            if (window.attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION == 0) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.KITKAT)
fun SystemUiFlag(statusLight: Boolean, navigationBarLight: Boolean): Int {
    var flag = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
    when {
        Build.VERSION.SDK_INT > Build.VERSION_CODES.O -> {
            if (statusLight) flag = flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            if (navigationBarLight) flag = flag or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        }
        Build.VERSION.SDK_INT > Build.VERSION_CODES.M -> {
            if (statusLight) flag = flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    return flag
}


