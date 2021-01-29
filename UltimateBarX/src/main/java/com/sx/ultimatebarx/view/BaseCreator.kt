package com.sx.ultimatebarx.view

import com.sx.ultimatebarx.UltimateBarXManager


internal abstract class BaseCreator(protected val tag: Tag) : Creator {
    protected val manager by lazy { UltimateBarXManager.getInstance() }
}