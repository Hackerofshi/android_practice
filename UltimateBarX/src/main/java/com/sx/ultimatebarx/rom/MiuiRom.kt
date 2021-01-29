package com.sx.ultimatebarx.rom

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.sx.ultimatebarx.extension.screenHeight

internal class MiuiRom : BaseRoom() {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun screenIndicatorOn(context: Context): Boolean {
        val navHeight = navigationBarHeight
        val screenHeight = context.screenHeight
        return navHeight > 0 && screenHeight / navHeight > 30
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun fullScreenGestureOn(context: Context): Boolean {
        return Settings.Secure.getInt(context.contentResolver, "force_fsg_nav_bar", -1) > 0;
    }


}