package com.sx.ultimatebarx

import android.content.Context
import androidx.startup.Initializer

class UltimateBarXInitializer: Initializer<Unit> {
    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        UltimateBarXManager.getInstance().context = context
    }

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     * <br></br>
     * For e.g. if a [Initializer] `B` defines another
     * [Initializer] `A` as its dependency, then `A` gets initialized before `B`.
     */
    override fun dependencies(): MutableList<Class<out Initializer<*>>>  = mutableListOf()

}