package com.shixin.customview.layoutmanager;

import android.graphics.Rect;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.shixin.jetpack.databinding.MainActivity7;

/**
 * @ProjectName: Android_Pratice
 * @Package: com.shixin.customview.layoutmanager
 * @ClassName: CoverFlowLayoutManager
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2021/4/24 16:43
 * @UpdateUser: shixin：
 * @UpdateDate: 2021/4/24 16:43
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class CoverFlowLayoutManager extends RecyclerView.LayoutManager {
    private int mSumx       = 0;
    private int mTotalWidth = 0;
    private int mItemWidth, mItemHeight;
    private SparseArray<Rect>  mItemRects      = new SparseArray<>();
    /**
     * 记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收
     */
    private SparseBooleanArray mHasAttachItems = new SparseBooleanArray();

    private int mIntervalWidth;
    //绘制第一个view时的起点
    private int mStartX;


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        mHasAttachItems.clear();
        mItemRects.clear();
        detachAndScrapAttachedViews(recycler);

        //将item的位置存储起来
        View childView = recycler.getViewForPosition(0);
        measureChildWithMargins(childView, 0, 0);
        mItemWidth = getDecoratedMeasuredWidth(childView);
        mItemHeight = getDecoratedMeasuredHeight(childView);

        //每一个item之间的间隔
        mIntervalWidth = getIntervalWidth();

        mStartX = getWidth() / 2 - mIntervalWidth;

        //定义水平方向偏移量
        int offsetX = 0;
        for (int i = 0; i < getItemCount(); i++) {
            Rect rect = new Rect(mStartX + offsetX, 0,
                    mStartX + offsetX + mItemWidth, mItemHeight);
            mItemRects.put(i, rect);
            mHasAttachItems.put(i, false);
            offsetX += mIntervalWidth;
        }

        int  visibleCount = getHorizontalSpace() / mIntervalWidth;
        Rect visibleRect  = getVisibleArea();
        for (int i = 0; i < visibleCount; i++) {
            insertView(i, visibleRect, recycler, false);
        }

        //如果所有子View的宽度和没有填满RecyclerView的宽度，
        // 则将宽度设置为RecyclerView的宽度
        mTotalWidth = Math.max(offsetX, getHorizontalSpace());

    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }


    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() > 0) {
            return dx;
        }

        int travel = dx;
        //如果滑动到顶部
        if (mSumx + dx < 0) {
            travel = -mSumx;
        } else if (mSumx + dx > getMaxOffset()) {
            //如果滑动到最底部
            travel = getMaxOffset() - mSumx;
        }
        mSumx += travel;
        Rect visibleRect = getVisibleArea();

        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child    = getChildAt(i);
            int  position = getPosition(child);
            Rect rect     = mItemRects.get(position);
            if (!Rect.intersects(rect, visibleRect)) {
                removeAndRecycleView(child, recycler);
            } else {
                layoutDecoratedWithMargins(child,
                        rect.left - mSumx, rect.top
                        , rect.right - mSumx, rect.bottom
                );
                handChildView(child, rect.left - mStartX - mSumx);
                mHasAttachItems.put(position, true);
            }
        }

        //填充空白区域
        View lastView  = getChildAt(getChildCount() - 1);
        View firstView = getChildAt(0);
        if (travel >= 0) {
            int minPost = getPosition(firstView);
            for (int i = minPost; i < getChildCount(); i++) {
                insertView(i, visibleRect, recycler, false);
            }
        } else {
            int maxPos = getPosition(lastView);
            for (int i = maxPos; i >= 0; i--) {
                insertView(i, visibleRect, recycler, true);

            }
        }
        return travel;
    }


    private void insertView(int i, Rect visibleRect, RecyclerView.Recycler recycler, boolean firstPos) {
        Rect rect = mItemRects.get(i);
        if (Rect.intersects(visibleRect, rect) && !mHasAttachItems.get(i)) {
            View child = recycler.getViewForPosition(i);
            if (firstPos) {
                addView(child, 0);
            } else {
                addView(child);
            }
            measureChildWithMargins(child, 0, 0);
            layoutDecoratedWithMargins(child,
                    rect.left - mSumx, rect.top,
                    rect.right - mSumx, rect.bottom);
            mHasAttachItems.put(i, true);
            handChildView(child, rect.left - mSumx - mStartX);

        }
    }

    private void handChildView(View child, int moveX) {
        float radio    = computeScale(moveX);
        float rotation = computeRotationY(moveX);

        child.setScaleX(radio);
        child.setScaleX(radio);
        child.setRotationY(rotation);
    }


    private float computeScale(int x) {
        float scale = 1 - Math.abs(x * 2.0f) / (8f * getIntervalWidth());
        if (scale < 0) scale = 0;
        if (scale > 1) scale = 1;
        return scale;
    }

    private Rect getVisibleArea() {
        return new Rect(getPaddingLeft() + mSumx, getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
    }


    private int getIntervalWidth() {
        return mItemWidth / 2;
    }

    private int getMaxOffset() {
        return (getItemCount() - 1) * getIntervalWidth();
    }

    public int getCenterPosition() {
        //mSumx 是移动的距离，布局完成的时候mSumx是0
        int pos  = (int) (mSumx / getIntervalWidth());
        int more = (int) (mSumx % getIntervalWidth());
        if (more > getIntervalWidth() * 0.5f) pos++;
        return pos;
    }

    public int getFirstVisiblePosition() {
        if (getChildCount() <= 0) {
            return 0;
        }
        View vi  = getChildAt(0);
        int  pos = getPosition(vi);
        return pos;
    }

    /**
     * 计算滑动时候某一个item是否滑动超过一半，超过一半就全部滑动过去
     *
     * @param velocityX
     * @param distance
     * @return
     */
    public double calculateDistance(int velocityX, double distance) {
        int    extra = mSumx % getIntervalWidth();
        double realDistance;
        if (velocityX > 0) {
            if (distance < getIntervalWidth()) {
                realDistance = getIntervalWidth() - extra;
            } else {
                realDistance = distance - distance % getIntervalWidth() - extra;
            }
        } else {
            if (distance < getIntervalWidth()) {
                realDistance = extra;
            } else {
                realDistance = distance - distance % getIntervalWidth() + extra;
            }
        }
        return realDistance;
    }

    private float M_MAX_ROTATION_Y = 30.0f;

    private float computeRotationY(int x) {
        float rotationY;
        rotationY = -M_MAX_ROTATION_Y * x / getIntervalWidth();
        if (Math.abs(rotationY) > M_MAX_ROTATION_Y) {
            if (rotationY > 0) {
                rotationY = M_MAX_ROTATION_Y;
            }
        } else {
            rotationY = -M_MAX_ROTATION_Y;
        }
        return rotationY;
    }
}
