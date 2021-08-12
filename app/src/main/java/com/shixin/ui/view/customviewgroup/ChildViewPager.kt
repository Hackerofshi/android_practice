package com.shixin.ui.view.customviewgroup

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ScrollView
import androidx.viewpager.widget.ViewPager

//ViewPager里面嵌套ViewPager导致的滑动冲突
class ChildViewPager : ViewPager {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val curPosition: Int
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> parent.requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_MOVE -> {
                curPosition = this.currentItem
                val count = this.adapter!!.count
                Log.i(TAG, "curPosition:=$curPosition")
                // 当当前页面在最后一页和第0页的时候，由父亲拦截触摸事件
                if (curPosition == count - 1 || curPosition == 0) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else { //其他情况，由孩子拦截触摸事件
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        private const val TAG = "xujun"
    }
}

//ScrollView 里面嵌套ViewPager导致的滑动冲突
//外部解决法
class VerticalScrollView : ScrollView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    @TargetApi(21)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    private var mDownPosX = 0f
    private var mDownPosY = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mDownPosX = x
                mDownPosY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = Math.abs(x - mDownPosX)
                val deltaY = Math.abs(y - mDownPosY)
                // 这里是否拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (deltaX > deltaY) { // 左右滑动不拦截
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}

//内部解决法
class MyViewPager : ViewPager {
    var lastX = -1
    var lastY = -1

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.rawX.toInt()
        val y = ev.rawY.toInt()
        var dealtX = 0
        var dealtY = 0
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                dealtX = 0
                dealtY = 0
                // 保证子View能够接收到Action_move事件
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                dealtX += Math.abs(x - lastX)
                dealtY += Math.abs(y - lastY)
                Log.i(TAG, "dealtX:=$dealtX")
                Log.i(TAG, "dealtY:=$dealtY")
                // 这里是否拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX >= dealtY) { // 左右滑动请求父 View 不要拦截
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_CANCEL -> {
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        private const val TAG = "xujun"
    }
}

