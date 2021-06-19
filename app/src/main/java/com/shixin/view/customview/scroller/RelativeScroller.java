package com.shixin.view.customview.scroller;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import androidx.annotation.RequiresApi;

/**
 * @ProjectName: Android_Pratice
 * @Package: com.shixin.view.customview.scroller
 * @ClassName: RelativeScroller
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2021/5/30 14:32
 * @UpdateUser: shixin：
 * @UpdateDate: 2021/5/30 14:32
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RelativeScroller extends RelativeLayout {
    private OverScroller overScroller;
    private Scroller     scroller;
    private int          mTouchSlop;

    public RelativeScroller(Context context) {
        this(context, null);
    }

    public RelativeScroller(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RelativeScroller(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        overScroller = new OverScroller(context);
        scroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();

    }


    private float lastX;
    private float lastY;

    //
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("ce", "onTouchEvent: 测试");
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastX;
                float dy = y - lastY;

                Log.i("TAG", "onTouchEvent: " + dy);
                if (Math.abs(dy) > mTouchSlop) {
                    scroller.startScroll(0, scroller.getFinalY(), 0, (int) dy);
                }

                //只能滑动内容
                //scrollBy((int) dx, (int) dy);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        lastX = x;
        lastY = y;
        Log.i("ce", "onTouchEvent: 测试");
        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            setTranslationY(scroller.getCurrY());
            postInvalidate();
        }
    }

}