package com.shixin.libary.baseKt

import android.util.Log
import androidx.fragment.app.Fragment

abstract class LazyFragment : Fragment() {
    private var isLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyInit()
            Log.d("TAG", "lazyInit:!!!!!!!")
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    abstract fun lazyInit()
}