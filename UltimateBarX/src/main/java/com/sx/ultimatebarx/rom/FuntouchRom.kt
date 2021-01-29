package com.sx.ultimatebarx.rom

import android.content.Context
import android.provider.Settings

internal class FuntouchRom : BaseRoom() {
    override fun fullScreenGestureOn(context: Context): Boolean {
        return Settings.Secure.getInt(context.contentResolver,
                "navigation_gesture_on", -1) > 0;
    }

}