package com.sx.ultimatebarx.rom

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

internal class OtherRom :BaseRoom(){

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun fullScreenGestureOn(context: Context): Boolean = false

}