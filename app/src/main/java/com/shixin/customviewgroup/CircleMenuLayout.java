package com.shixin.customviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shixin.rxjava.R;

/**
 * Created by admin on 2017/3/8 0008.
 */

public class CircleMenuLayout extends ViewGroup {


    private int mRadius;

    /**
     * 该容器内的child  item_rv 的默认尺寸
     */
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;


    /**
     * 菜单中心child的默认尺寸
     */
    private float RADIO_DEFAULT_CENTEFITEM_DIMENSION = 1 / 3F;

    /**
     * 该容器的内边距 无视padding属性，如需边距请用该变量
     */
    private static final float RADIO_PADDING_LAYOUT = 1 / 12F;


    /**
     * 当美妙达到该值时，认为是快速移动。
     */
    private static final int FLINGABLE_VALUE = 300;
    /**
     * 但移动角°达到该值时，则屏蔽点击。
     */
    private static final int NOCLICK_VALUE = 3;

    private int mFlingableValue = FLINGABLE_VALUE;


    private float mPadding;

    private double mStartAngle = 0;


    private String[] mItemTexts;

    private int[] mItemImgs;


    private int mMenuItemCount;


    private float mTempAngle;

    private long mDownTime;

    private boolean isFling;

    public void setMenuItemLayoutId(int mMenuItemLayoutId) {
        this.mMenuItemLayoutId = mMenuItemLayoutId;
    }

    private int mMenuItemLayoutId = R.layout.circle_menu_item;


    public CircleMenuLayout(Context context) {
        super(context);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 0, 0, 0);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int resWidth = 0;
        int resHeight = 0;
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            //主要设置为背景的图的高度
            resWidth = getSuggestedMinimumWidth();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;


            resHeight = getSuggestedMinimumHeight();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resHeight = resHeight == 0 ? getDefaultHeight() : resHeight;
        } else {


            // 如果都设置为精确值，则直接取小值；
            resWidth = resHeight = Math.min(width, height);
        }
        setMeasuredDimension(resWidth, resHeight);

        // 获得半径
        mRadius = Math.max(getMeasuredHeight(), getMeasuredWidth());
        // menu item数量
        final int count = getChildCount();
        // menu item尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        // menu item测量模式
        int childMode = MeasureSpec.EXACTLY;

        for (int i = 0; i < count; i++) {

            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int makeMeasureSpec = -1;
            if (child.getId() == R.id.id_circle_menu_item_center) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec((int)
                        (mRadius * RADIO_DEFAULT_CENTEFITEM_DIMENSION), childMode);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);
            }
            child.measure(makeMeasureSpec, makeMeasureSpec);
        }
        mPadding = RADIO_PADDING_LAYOUT * mRadius;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutRadius = mRadius;
        final int childCount = getChildCount();
        int left, top;
        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        double angleDelay = (double) 360 / (getChildCount() - 1);

        // 遍历去设置menuitem的位置
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getId() == R.id.id_circle_menu_item_center) {
                continue;
            }
            if (child.getVisibility() == GONE) {
                continue;
            }
            mStartAngle %= 360;
            // 计算，中心点到menu item中心的距离
            float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;


            //toRadians  将弧度转换为角度
            // tmp cosa 即menu item中心点的横坐标
            left = layoutRadius / 2 + (int) Math.round(tmp * Math
                    .cos(Math.toRadians(mStartAngle)) - 1 / 2f * cWidth);
            // tmp sina 即menu item的纵坐标
            top = layoutRadius / 2 + (int) Math.round(tmp * Math
                    .sin(Math.toRadians(mStartAngle)) - 1 / 2f * cWidth);
            child.layout(left, top, left + cWidth, top + cWidth);
            // 叠加尺寸
            mStartAngle += angleDelay;
        }


        View cView = findViewById(R.id.id_circle_menu_item_center);

        if (cView != null) {
            cView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.itemCenterClick(v);
                    }
                }
            });
            int cl = layoutRadius / 2 - cView.getMeasuredWidth() / 2;
            int cr = cl + cView.getMeasuredHeight();
            cView.layout(cl, cl, cr, cr);

        }


    }

    public interface OmMenuItemClickListener {
        void itemClick(View view, int pos);

        void itemCenterClick(View view);


    }

    private OmMenuItemClickListener mOnMenuItemClickListener;


    public void setOnMentItemClickListener(OmMenuItemClickListener mOmOnMentItemClickListener) {

        this.mOnMenuItemClickListener = mOmOnMentItemClickListener;
    }


    private int getDefaultHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    private int getDefaultWidth() {

        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }


    private float mLastX;

    private float mLastY;

    private AutoFlingRunnable mFlingRunnable;

    public void setFlingRunnable(AutoFlingRunnable mFlingRunnable) {
        this.mFlingRunnable = mFlingRunnable;
    }


    public void setPadding(float mPadding) {
        this.mPadding = mPadding;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTempAngle = 0;
                if (isFling) {
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float start = getAngle(mLastX, mLastY);

                float end = getAngle(x, y);
                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mStartAngle += end - start;
                    mTempAngle += end - start;

                } else {
                    mStartAngle += start - end;
                    mTempAngle += start - end;
                }
                requestLayout();
                mLastY = y;
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
                float anglePersecond = mTempAngle * 1000 / (System.currentTimeMillis() - mDownTime);

                if (Math.abs(anglePersecond) > mFlingableValue && !isFling) {
                    post(mFlingRunnable = new AutoFlingRunnable(anglePersecond));
                    return true;
                }

                if (Math.abs(mTempAngle) > NOCLICK_VALUE) {
                    return true;
                }
                break;
        }

        return super.dispatchTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e("-------", "onInterceptTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("-------", "onInterceptTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("-------", "onInterceptTouchEvent ACTION_UP");
                break;

            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);

    }

    /**
     * 根据当前位置计算象限
     *
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(float x, float y) {
        int tempX = (int) (x - mRadius / 2);
        int tempY = (int) (y - mRadius / 2);

        if (tempX >= 0) {
            return tempY >= 0 ? 4 : 1;
        } else {
            return tempY >= 0 ? 3 : 2;
        }
    }

    /**
     * 根据触摸的位置获得角度
     *
     * @param mLastX
     * @param mLastY
     * @return
     */
    private float getAngle(float mLastX, float mLastY) {
        double x = mLastX - (mRadius / 2d);
        double y = mLastY - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e("-------", "TouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("-------", "TouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("-------", "TouchEvent ACTION_UP");
                break;

            default:
                break;
        }
        //返回true将onTouchEvent拦截
        return true;

    }

    public void setMenuItemIconsAndTexts(int[] resIds, String[] texts) {
        mItemImgs = resIds;
        mItemTexts = texts;
        if (resIds == null && texts == null) {
            throw new IllegalArgumentException("菜单文本和图片至少设置其一");
        }

        mMenuItemCount = resIds == null ? texts.length : resIds.length;
        if (resIds != null && texts != null) {
            mMenuItemCount = Math.min(resIds.length, texts.length);
        }
        addMenItems();
    }

    private void addMenItems() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < mMenuItemCount; i++) {
            final int j = i;
            View view = mInflater.inflate(mMenuItemLayoutId, this, false);
            ImageView iv = (ImageView)
                    view.findViewById(R.id.id_circle_menu_item_image);
            TextView tv = (TextView)
                    view.findViewById(R.id.id_circle_menu_item_text);
            if (iv != null) {
                iv.setVisibility(VISIBLE);
                iv.setImageResource(mItemImgs[i]);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.itemClick(v, j);
                        }

                    }
                });
            }

            if (tv != null) {
                tv.setVisibility(VISIBLE);
                tv.setText(mItemTexts[i]);

            }
            addView(view);
        }


    }


    private class AutoFlingRunnable implements Runnable {

        private float angelPerSecond;

        public AutoFlingRunnable(float velocity) {
            this.angelPerSecond = velocity;
        }

        @Override
        public void run() {

            System.out.println("------------------"+angelPerSecond);
            // 如果小于20,则停止
            if ((int) Math.abs(angelPerSecond) < 20) {
                isFling = false;
                return;
            }


            isFling = true;
            // 不断改变mStartAngle，让其滚动，/30为了避免滚动太快
            mStartAngle += (angelPerSecond / 30);
            // 逐渐减小这个值
            angelPerSecond /= 1.0666F;
            postDelayed(this, 30);  //不断的调用当前这个方法
            // 重新布局
            requestLayout();
        }
    }
}
