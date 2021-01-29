package com.sx.ultimatebarx.rom

import android.content.Context
import android.provider.Settings

class EmuiRom : BaseRoom() {
    override fun fullScreenGestureOn(context: Context): Boolean {
        return Settings.Global.getInt(context.contentResolver, "navigationbar_is_min", -1) > 0
    }
}