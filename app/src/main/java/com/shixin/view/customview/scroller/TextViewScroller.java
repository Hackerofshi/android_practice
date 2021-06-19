package com.shixin.view.customview.scroller;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @ProjectName: Android_Pratice
 * @Package: com.shixin.view.customview.scroller
 * @ClassName: TextViewScroller
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2021/5/16 11:07
 * @UpdateUser: shixin：
 * @UpdateDate: 2021/5/16 11:07
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class TextViewScroller extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    private OverScroller    overScroller;
    private VelocityTracker mVelocityTracker;
    private int             mTouchSlop;
    private int             mMinimumVelocity;
    private int             mMaximumVelocity;
    private int             top;
    private int             bottom;

    public TextViewScroller(@NonNull Context context) {
        this(context, null);
    }

    public TextViewScroller(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewScroller(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        overScroller = new OverScroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public void setBoundary(int top, int bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private float lastX;
    private float lastY;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!overScroller.isFinished()) {
                    overScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastX;
                float dy = y - lastY;

                overScroller.startScroll(0, overScroller.getFinalY(), 0, (int) dy);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    fling(initialVelocity);
                } else if (overScroller.springBack(0, (int) getTranslationY(),
                        0, 0, top - getTop(), bottom - getBottom())) {
                    postInvalidateOnAnimation();
                }
                recycleVelocityTracker();
                break;
        }
        lastX = x;
        lastY = y;
        return super.onTouchEvent(event);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void fling(int initialVelocity) {
        int minY = top - getTop();//实际上滑允许滚动的范围 = minY - overY
        int maxY = bottom - getBottom();//实际下滑允许滚动的范围 = maxY + overY
        overScroller.fling(0, (int) getTranslationY(), 0, initialVelocity, 0, 0, minY, maxY, 0, 50);
        postInvalidateOnAnimation();
    }
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
    @Override
    public void computeScroll() {
        if (overScroller.computeScrollOffset()) {
            setTranslationY(overScroller.getCurrY());
            postInvalidate();
        }
    }

}