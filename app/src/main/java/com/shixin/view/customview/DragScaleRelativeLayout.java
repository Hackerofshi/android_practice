package com.shixin.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shixin.R;
import com.shixin.callBack.ValueChange;

import org.w3c.dom.Text;

public class DragScaleRelativeLayout extends RelativeLayout implements View.OnTouchListener {

    protected            int         screenWidth;
    protected            int         screenHeight;
    protected            int         lastX;
    protected            int         lastY;
    private              int         oriLeft;
    private              int         oriRight;
    private              int         oriTop;
    private              int         oriBottom;
    private              int         dragDirection;
    private static final int         TOP           = 0x15;
    private static final int         LEFT          = 0x16;
    private static final int         BOTTOM        = 0x17;
    private static final int         RIGHT         = 0x18;
    private static final int         LEFT_TOP      = 0x11;
    private static final int         RIGHT_TOP     = 0x12;
    private static final int         LEFT_BOTTOM   = 0x13;
    private static final int         RIGHT_BOTTOM  = 0x14;
    private static final int         TOUCH_TWO     = 0x21;
    private static final int         CENTER        = 0x19;
    private              int         offset        = 0; //可超出其父控件的偏移量
    protected            Paint       paint         = new Paint();
    private static final int         touchDistance = 180; //触摸边界的有效距离
    private              ValueChange valueChange;
    private              int         w;
    private              int         h;


    // 初始的两个手指按下的触摸点的距离
    private float oriDis = 1f;

    /**
     * 初始化获取屏幕宽高
     */
    protected void initScreenW_H() {
        screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    public DragScaleRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
        initScreenW_H();
    }

    public DragScaleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        initScreenW_H();
    }

    public DragScaleRelativeLayout(Context context) {
        super(context);
        setOnTouchListener(this);
        initScreenW_H();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(4.0f);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        setBackgroundResource(R.drawable.bg_dashgap);
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN) {
            oriLeft = v.getLeft();
            oriRight = v.getRight();
            oriTop = v.getTop();
            oriBottom = v.getBottom();
            lastY = (int) event.getRawY();
            lastX = (int) event.getRawX();
            dragDirection = getDirection(v, (int) event.getX(),
                    (int) event.getY());
        }
        if (action == MotionEvent.ACTION_POINTER_DOWN) {
            oriLeft = v.getLeft();
            oriRight = v.getRight();
            oriTop = v.getTop();
            oriBottom = v.getBottom();
            lastY = (int) event.getRawY();
            lastX = (int) event.getRawX();
            dragDirection = TOUCH_TWO;
            oriDis = distance(event);
        }
        // 处理拖动事件
        delDrag(v, event, action);
        invalidate();
        //requestLayout();
        return false;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //   Log.i("TAG", "onLayout: " + getHeight());
//        Log.i("TAG", "onLayout: " + getWidth());
        int                cCount  = getChildCount();
        int                cWidth  = 0;
        int                cHeight = 0;
        MarginLayoutParams cParams = null;
        /* *
         * 遍历所有childView根据其宽和高，以及margin进行布局*/

        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0, ct = 0, cr = 0, cb = 0;

            switch (i) {
                case 0:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin;
                    break;
                case 1:
                    cl = getWidth() / 2 - cWidth / 2 - cParams.leftMargin
                            - cParams.rightMargin;
                    ct = cParams.topMargin;
                    break;
                case 2:
                    cl = getWidth() - cWidth - cParams.leftMargin
                            - cParams.rightMargin;
                    ct = cParams.topMargin;
                    break;
                case 3:
                    cl = cParams.leftMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;
                case 4:
                    cl = getWidth() / 2 - cWidth / 2 - cParams.leftMargin
                            - cParams.rightMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;
                case 5:
                    cl = getWidth() - cWidth - cParams.leftMargin
                            - cParams.rightMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;
                case 6:
                    View previous = getChildAt(i - 1);
                    MarginLayoutParams preParams = (MarginLayoutParams) previous.getLayoutParams();

                    cl = cParams.leftMargin;
                    cr = getWidth() - cParams.rightMargin;
                    ct = cParams.topMargin + previous.getMeasuredHeight() + preParams.topMargin + preParams.bottomMargin;
                    cb = getHeight() - cParams.bottomMargin -
                            (previous.getMeasuredHeight() + preParams.topMargin + preParams.bottomMargin);

                    if (childView instanceof TextView) {
                        w = getWidth() - cParams.leftMargin - cParams.rightMargin;
                        h = getHeight() - cParams.topMargin - cParams.bottomMargin -
                                (previous.getMeasuredHeight() + preParams.topMargin + preParams.bottomMargin) * 2;
                        TextView tv = (TextView) childView;
                        tv.setWidth(w);
                    }
                    childView.layout(cl, ct, cr, cb);
                    break;
            }
            if (i != 6) {
                cr = cl + cWidth;
                cb = cHeight + ct;
                childView.layout(cl, ct, cr, cb);
            }
        }
    }


    /**
     * 处理拖动事件
     *
     * @param v
     * @param event
     * @param action
     */
    protected void delDrag(View v, MotionEvent event, int action) {
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                switch (dragDirection) {
                    case LEFT: // 左边缘
//                        left(v, dx);
                        break;
                    case RIGHT: // 右边缘
//                        right(v, dx);
                        break;
                    case BOTTOM: // 下边缘
//                        bottom(v, dy);
                        break;
                    case TOP: // 上边缘
//                        top(v, dy);
                        break;
                    case CENTER: // 点击中心-->>移动
                        //center(v, dx, dy);
                        break;
                    case LEFT_BOTTOM: // 左下


//                        left(v, dx);
//                        bottom(v, dy);
                        center(v, dx, dy);
                        break;
                    case LEFT_TOP: // 左上
                        //left(v, dx);
                        //top(v, dy);
                        break;
                    case RIGHT_BOTTOM: // 右下
                        right(v, dx);
                        bottom(v, dy);
                        break;
                    case RIGHT_TOP: // 右上
                        //right(v, dx);
                        //top(v, dy);
                        break;
                    case TOUCH_TWO: //双指操控
                        float newDist = distance(event);
                        float scale = newDist / oriDis;
                        //控制双指缩放的敏感度
                        int distX = (int) (scale * (oriRight - oriLeft) - (oriRight - oriLeft)) / 50;
                        int distY = (int) (scale * (oriBottom - oriTop) - (oriBottom - oriTop)) / 50;
                        if (newDist > 10f) {//当双指的距离大于10时，开始相应处理
                            left(v, -distX);
                            top(v, -distY);
                            right(v, distX);
                            bottom(v, distY);
                        }
                        break;

                }
                if (dragDirection != CENTER && dragDirection != LEFT_BOTTOM) {
                    MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();

                    params.height = (oriBottom - oriTop);
                    params.width = (oriRight - oriLeft);
                    params.leftMargin = oriLeft;
                    params.topMargin = oriTop;


                    v.setLayoutParams(params);
                    //  v.layout(oriLeft, oriTop, oriRight, oriBottom);
                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (dragDirection != CENTER) {
                    valueChange.changed(oriRight - oriLeft, oriBottom - oriTop,
                            getLeft(), getTop());
                }
                dragDirection = 0;
                break;
        }
    }

    /**
     * 触摸点为中心->>移动
     *
     * @param v
     * @param dx
     * @param dy
     */
    private void center(View v, int dx, int dy) {
        int left   = v.getLeft() + dx;
        int top    = v.getTop() + dy;
        int right  = v.getRight() + dx;
        int bottom = v.getBottom() + dy;
        if (left < -offset) {
            left = -offset;
            right = left + v.getWidth();
        }
        if (right > screenWidth + offset) {
            right = screenWidth + offset;
            left = right - v.getWidth();
        }
        if (top < -offset) {
            top = -offset;
            bottom = top + v.getHeight();
        }
        if (bottom > screenHeight + offset) {
            bottom = screenHeight + offset;
            top = bottom - v.getHeight();
        }
        Log.d("raydrag", left + "  " + top + "  " + right + "  " + bottom + "  " + dx);
        // v.layout(left, top, right, bottom);

        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();

        params.height = (bottom - top);
        params.width = (right - left);
        params.leftMargin = left;
        params.topMargin = top;

        v.setLayoutParams(params);
    }

    /**
     * 触摸点为上边缘
     *
     * @param v
     * @param dy
     */
    private void top(View v, int dy) {
        oriTop += dy;
        if (oriTop < -offset) {
            //对view边界的处理，如果子view达到父控件的边界，offset代表允许超出父控件多少
            oriTop = -offset;
        }
        if (oriBottom - oriTop - 2 * offset < 200) {
            oriTop = oriBottom - 2 * offset - 200;
        }
    }

    /**
     * 触摸点为下边缘
     *
     * @param v
     * @param dy
     */
    private void bottom(View v, int dy) {
        oriBottom += dy;
        if (oriBottom > screenHeight + offset) {
            oriBottom = screenHeight + offset;
        }
        if (oriBottom - oriTop - 2 * offset < 200) {
            oriBottom = 200 + oriTop + 2 * offset;
        }
    }

    /**
     * 触摸点为右边缘
     *
     * @param v
     * @param dx
     */
    private void right(View v, int dx) {
        oriRight += dx;
        if (oriRight > screenWidth + offset) {
            oriRight = screenWidth + offset;
        }
        if (oriRight - oriLeft - 2 * offset < 200) {
            oriRight = oriLeft + 2 * offset + 200;
        }
    }

    /**
     * 触摸点为左边缘
     *
     * @param v
     * @param dx
     */
    private void left(View v, int dx) {
        oriLeft += dx;
        if (oriLeft < -offset) {
            oriLeft = -offset;
        }
        if (oriRight - oriLeft - 2 * offset < 200) {
            oriLeft = oriRight - 2 * offset - 200;
        }
    }

    /**
     * 获取触摸点flag
     *
     * @param v
     * @param x
     * @param y
     * @return
     */
    protected int getDirection(View v, int x, int y) {
        int left   = v.getLeft();
        int right  = v.getRight();
        int bottom = v.getBottom();
        int top    = v.getTop();
        if (x < touchDistance && y < touchDistance) {
            return LEFT_TOP;
        }
        if (y < touchDistance && right - left - x < touchDistance) {
            return RIGHT_TOP;
        }
        if (x < touchDistance && bottom - top - y < touchDistance) {
            return LEFT_BOTTOM;
        }
        if (right - left - x < touchDistance && bottom - top - y < touchDistance) {
            return RIGHT_BOTTOM;
        }
        if (x < touchDistance) {
            return LEFT;
        }
        if (y < touchDistance) {
            return TOP;
        }
        if (right - left - x < touchDistance) {
            return RIGHT;
        }
        if (bottom - top - y < touchDistance) {
            return BOTTOM;
        }
        return CENTER;
    }

    /**
     * 计算两个手指间的距离
     *
     * @param event 触摸事件
     * @return 放回两个手指之间的距离
     */
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);//两点间距离公式
    }

    public void setValueChange(ValueChange valueChange) {
        this.valueChange = valueChange;
    }


}
