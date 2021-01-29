package com.sx.ultimatebarx.view

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.sx.ultimatebarx.navigationBarHeight
import com.sx.ultimatebarx.statusBarHeight

internal class FrameLayoutCreator(private val frameLayout: FrameLayout, tag: Tag)
    : BaseCreator(tag) {
    override fun getStatusBarView(context: Context, fitWindow: Boolean): View {
        var statusBar: View? = frameLayout.findViewWithTag(tag.statusBarViewTag())
        if (null == statusBar) {
            statusBar = View(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        statusBarHeight
                ).apply {
                    gravity = Gravity.TOP
                }
            }
            statusBar.tag = tag.statusBarViewTag()
            frameLayout.addView(statusBar)
        }

        statusBar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                statusBar.layoutParams = (statusBar.layoutParams as FrameLayout.LayoutParams)
                        .apply { topMargin = if (fitWindow) -statusBarHeight else 0 }
                statusBar.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
        return statusBar
    }

    override fun getNavigationBarView(context: Context, fitWindow: Boolean): View {
        var navigationBar: View? = frameLayout.findViewWithTag(tag.navigationBarViewTag())
        if (null == navigationBar) {
            navigationBar = View(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, navigationBarHeight
                ).apply {
                    gravity = Gravity.BOTTOM
                }
            }
            navigationBar.tag = tag.navigationBarViewTag()
            frameLayout.addView(navigationBar)
        }
        navigationBar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            /**
             * Callback method to be invoked when the global layout state or the visibility of views
             * within the view tree changes
             */
            override fun onGlobalLayout() {
                navigationBar.layoutParams = (navigationBar.layoutParams as FrameLayout.LayoutParams)
                        .apply {
                            bottomMargin = if (fitWindow) -navigationBarHeight else 0
                        }
                navigationBar.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
        return navigationBar
    }
}