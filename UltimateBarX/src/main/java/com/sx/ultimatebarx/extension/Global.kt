package com.sx.ultimatebarx.extension

import android.text.TextUtils
import com.sx.ultimatebarx.rom.*
import com.sx.ultimatebarx.rom.FuntouchRom
import com.sx.ultimatebarx.rom.MiuiRom
import com.sx.ultimatebarx.rom.OtherRom
import com.sx.ultimatebarx.rom.Rom
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

internal fun getRom(): Rom {
    if (!TextUtils.isEmpty(getProp(Rom.KEY_VERSION_MIUI))) {
        return MiuiRom()
    }
    if (!TextUtils.isEmpty(getProp(Rom.KEY_VERSION_EMUI))) {
        return EmuiRom()
    }
    if (!TextUtils.isEmpty(getProp(Rom.KEY_VERSION_VIVO))) {
        return FuntouchRom()
    }
    return OtherRom()
}


private fun getProp(name: String): String? {
    val line: String?
    var input: BufferedReader? = null
    try {
        val p = Runtime.getRuntime().exec("getprop $name")
        input = BufferedReader(InputStreamReader(p.inputStream), 1024)
        line = input.readLine()
        input.close()
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    } finally {
        if (null != input) {
            try {
                input.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    return line
}