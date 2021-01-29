package com.sx.ultimatebarx

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

internal class UltimateBarXObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner)
    {
        UltimateBarXManager.getInstance().removeAllData(owner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(owner: LifecycleOwner)
    {
        if (owner is Fragment)
        {
            val staDefault  = UltimateBarXManager.getInstance().getStatusBarDefault(owner)
            val navDefault = UltimateBarXManager.getInstance().getNavinationBarDefault(owner)
            if (staDefault)
            {

            }
        }
    }

}