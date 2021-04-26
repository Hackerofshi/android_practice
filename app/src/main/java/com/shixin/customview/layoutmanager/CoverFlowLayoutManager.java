package com.shixin.customview.layoutmanager;

import android.graphics.Rect;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

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
    /**
     * 滑动的总距离
     */
    private int mSumDx      = 0;
    private int mTotalWidth = 0;
    private int mItemWidth, mItemHeight;
    private SparseArray<Rect>  mItemRects        = new SparseArray<>();
    /**
     * 记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收
     */
    private SparseBooleanArray mHasAttachedItems = new SparseBooleanArray();

    private int mIntervalWidth;
    //绘制第一个view时的起点
    private int mStartX;


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {//没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
        mHasAttachedItems.clear();
        mItemRects.clear();
        detachAndScrapAttachedViews(recycler);

        //将item的位置存储起来
        View childView = recycler.getViewForPosition(0);
        measureChildWithMargins(childView, 0, 0);
        mItemWidth = getDecoratedMeasuredWidth(childView);
        mItemHeight = getDecoratedMeasuredHeight(childView);

        mIntervalWidth = getIntervalWidth();

        mStartX = getWidth() / 2 - mIntervalWidth;

        //定义水平方向的偏移量
        int offsetX = 0;

        for (int i = 0; i < getItemCount(); i++) {
            Rect rect = new Rect(mStartX + offsetX,
                    0, mStartX + offsetX + mItemWidth, mItemHeight);
            mItemRects.put(i, rect);
            mHasAttachedItems.put(i, false);
            offsetX += mIntervalWidth;
        }

        int visibleCount = getHorizontalSpace() / mIntervalWidth;
        Rect visibleRect = getVisibleArea();
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
        if (getChildCount() <= 0) {
            return dx;
        }

        int travel = dx;
        //如果滑动到最顶部
        if (mSumDx + dx < 0) {
            travel = -mSumDx;
        } else if (mSumDx + dx > getMaxOffset()) {
            //如果滑动到最底部
            travel = getMaxOffset() - mSumDx;
        }

        mSumDx += travel;

        Rect visibleRect = getVisibleArea();

        //回收越界子View
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            int position = getPosition(child);
            Rect rect = mItemRects.get(position);

            if (!Rect.intersects(rect, visibleRect)) {
                removeAndRecycleView(child, recycler);
                mHasAttachedItems.put(position, false);
            } else {
                layoutDecoratedWithMargins(child, rect.left - mSumDx, rect.top,
                        rect.right - mSumDx, rect.bottom);
                handleChildView(child, rect.left - mStartX - mSumDx);
                mHasAttachedItems.put(position, true);
            }
        }
        //填充空白区域
        View lastView = getChildAt(getChildCount() - 1);
        View firstView = getChildAt(0);
        if (travel >= 0) {
            int minPos = getPosition(firstView);
            for (int i = minPos; i < getItemCount(); i++) {
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


    private void insertView(int pos, Rect visibleRect, RecyclerView.Recycler recycler, boolean firstPos) {
        Rect rect = mItemRects.get(pos);
        if (Rect.intersects(visibleRect, rect) && !mHasAttachedItems.get(pos)) {
            View child = recycler.getViewForPosition(pos);
            if (firstPos) {
                addView(child, 0);
            } else {
                addView(child);
            }
            measureChildWithMargins(child, 0, 0);
            layoutDecoratedWithMargins(child, rect.left - mSumDx, rect.top,
                    rect.right - mSumDx, rect.bottom);

            mHasAttachedItems.put(pos, true);
            handleChildView(child, rect.left - mSumDx - mStartX);
        }
    }

    private void handleChildView(View child, int moveX) {
        float radio    = computeScale(moveX);
        float rotation = computeRotationY(moveX);

        child.setScaleX(radio);
        child.setScaleX(radio);
        child.setRotationY(rotation);
    }


    private float computeScale(int x) {
        float scale = 1 - Math.abs(x * 2.0f / (8f * getIntervalWidth()));
        if (scale < 0) scale = 0;
        if (scale > 1) scale = 1;
        return scale;
    }

    private Rect getVisibleArea() {
        return new Rect(getPaddingLeft() + mSumDx, getPaddingTop(),
                getWidth() - getPaddingRight() + mSumDx,
                getHeight() - getPaddingBottom());
    }


    private int getIntervalWidth() {
        return mItemWidth / 2;
    }

    private int getMaxOffset() {
        return (getItemCount() - 1) * getIntervalWidth();
    }

    /**
     * 获取视图中显示中位于中间的item的下标
     * -------------------------------------
     * |  0 |  1 |  2 |  6 |  5 |  4 |  3 |
     * |  0 |  1 |  2 |  3 |  4 |  5 |  6 |
     *
     *
     * @return
     */
    public int getCenterPosition() {
        int pos = (int) (mSumDx / getIntervalWidth());
        int more = (int) (mSumDx % getIntervalWidth());
        if (more > getIntervalWidth() * 0.5f) pos++;
        return pos;
    }


    /**
     * 获取第一个可见的Item位置
     * <p>Note:该Item为绘制在可见区域的第一个Item，有可能被第二个Item遮挡
     */
    public int getFirstVisiblePosition() {
        if (getChildCount() <= 0) {
            return 0;
        }
        View view = getChildAt(0);
        int pos = getPosition(view);

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
        int    extra = mSumDx % getIntervalWidth();
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

    /**
     * 计算转动
     * @param x
     * @return
     */
    private float computeRotationY(int x) {
        float rotationY;
        rotationY = -M_MAX_ROTATION_Y * x / getIntervalWidth();
        if (Math.abs(rotationY) > M_MAX_ROTATION_Y) {
            if (rotationY > 0) {
                rotationY = M_MAX_ROTATION_Y;
            } else {
                rotationY = -M_MAX_ROTATION_Y;
            }
        }
        return rotationY;
    }
}
