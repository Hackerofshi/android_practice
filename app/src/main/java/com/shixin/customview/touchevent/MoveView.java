package com.shixin.customview.touchevent;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @ProjectName: Android_Pratice
 * @Package: com.shixin.customview.touchevent
 * @ClassName: MoveView
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2021/5/9 16:45
 * @UpdateUser: shixin：
 * @UpdateDate: 2021/5/9 16:45
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MoveView extends View {

    private int mLastTouchX;
    private int mLastTouchY;

    private int     mTouchSlop;
    private boolean mCanMove;
    private int     mScrollPointerId;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoveView(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * Action备注
     * MotionEvent.ACTION_MASK       动作掩码、用于多点触控
     * MotionEvent.ACTION_DOWN       手势按下屏幕时产生，也可以用于检测按钮的状态
     * MotionEvent.ACTION_UP         手势结束时产生，该动作包含最终位置
     * MotionEvent.ACTION_MOVE       手势移动时经过的位置
     * MotionEvent.ACTION_CANCEL     手势中断后产生
     * MotionEvent.ACTION_OUTSIDE    在View范围之外移动产生的动作，只提供初始位置
     * MotionEvent.ACTION_POINTER_DOWN   多点触控时的按下手势
     * MotionEvent.ACTION_POINTER_UP     多点触控时的抬起手势
     * MotionEvent.ACTION_HOVER_MOVE  未触发down就发生了改变的手势，事件将传递给onGenericMotionEvent()
     * MotionEvent.ACTION_SCROLL      相对垂直或水平的滚动偏移，滚动手势(动作会传递给子view)，事件传递给onGenericMotionEvent()
     * MotionEvent.ACTION_HOVER_ENTER    回车动作,事件传递给onGenericMotionEvent()
     * MotionEvent.ACTION_HOVER_EXIT         退出动作,事件传递给onGenericMotionEvent()
     * MotionEvent.ACTION_BUTTON_PRESS      按钮被点击了，这不是touch event，所以事件传递给onGenericMotionEvent()
     * MotionEvent.ACTION_BUTTON_RELEASE       按钮被释放了，这不是touch event，所以事件传递给onGenericMotionEvent()
     * MotionEvent.ACTION_POINTER_INDEX_MASK   多点触控时点的索引，getPointerId获得标识，getX获得实际位置
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int actionIndex = event.getActionIndex();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mScrollPointerId = event.getPointerId(0);
                mLastTouchX = (int) (event.getX() + 0.5f);
                mLastTouchY = (int) (event.getY() + 0.5f);
                mCanMove = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i("ACTION_POINTER_DOWN", actionIndex + "");
                mScrollPointerId = event.getPointerId(actionIndex);
                mLastTouchX = (int) (event.getX(actionIndex) + 0.5f);
                mLastTouchY = (int) (event.getY(actionIndex) + 0.5f);
                break;
            case MotionEvent.ACTION_MOVE:
                int index = event.findPointerIndex(mScrollPointerId);
                int x = (int) (event.getX(index) + 0.5f);
                int y = (int) (event.getY(index) + 0.5f);
                int dx = mLastTouchX - x;
                int dy = mLastTouchY - y;
                if (!mCanMove) {
                    if (Math.abs(dy) >= mTouchSlop) {
                        if (dy > 0) {
                            dy -= mTouchSlop;
                        } else {
                            dy += mTouchSlop;
                        }
                        mCanMove = true;
                    }

                    if (Math.abs(dx) >= mTouchSlop) {
                        if (dx > 0) {
                            dx -= mTouchSlop;
                        } else {
                            dx += mTouchSlop;
                        }
                        mCanMove = true;
                    }
                    if (mCanMove) {
                        offsetTopAndBottom(-dy);
                        offsetLeftAndRight(-dx);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onPointerUp(event);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void onPointerUp(MotionEvent event) {
        final int actionIndex = event.getActionIndex();
        if (event.getPointerId(actionIndex) == mScrollPointerId) {
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mScrollPointerId = event.getPointerId(newIndex);
            mLastTouchX = (int) (event.getX(newIndex) + 0.5f);
            mLastTouchY = (int) (event.getY(newIndex) + 0.5f);
        }
    }
}