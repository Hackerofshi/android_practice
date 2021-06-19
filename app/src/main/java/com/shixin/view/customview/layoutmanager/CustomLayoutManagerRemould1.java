package com.shixin.view.customview.layoutmanager;

import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CustomLayoutManagerRemould1 extends RecyclerView.LayoutManager {
    private int mSumy        = 0;
    private int mTotalHeight = 0;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }


    private int mItemWidth, mItemHeight;
    private SparseArray<Rect> mItemRects = new SparseArray<>();

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        mItemRects.clear();
        detachAndScrapAttachedViews(recycler);

        //将item的位置存储起来
        View childView = recycler.getViewForPosition(0);
        measureChildWithMargins(childView, 0, 0);
        mItemWidth = getDecoratedMeasuredWidth(childView);
        mItemHeight = getDecoratedMeasuredHeight(childView);

        //屏幕中能够显示多少item
        int visibleCount = getVerticalSpace() / mItemHeight;

        //定义竖直方向的偏移量
        int offsetY = 0;

        for (int i = 0; i < getItemCount(); i++) {
            Rect rect = new Rect(0, offsetY, mItemWidth, offsetY + mItemHeight);
            mItemRects.put(i, rect);
            offsetY += mItemHeight;
        }

        for (int i = 0; i < visibleCount; i++) {
            Rect rect = mItemRects.get(i);
            View view = recycler.getViewForPosition(i);
            addView(view);
            //addView后一定要measure，先measure再layout
            measureChildWithMargins(view, 0, 0);
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom);
        }
        //如果所有子View的高度没有填满RecyclerView的高度
        //则将高度设置为RecyclerView的高度
        mTotalHeight = Math.max(offsetY, getVerticalSpace());
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingBottom();
    }


    @Override
    public boolean canScrollVertically() {
        return true;
    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() <= 0) {
            return dy;
        }
        int travel = dy;
        //如果滑动到顶部
        if (mSumy + dy < 0) {
            travel = -mSumy;
        } else if (mSumy + dy > mTotalHeight - getVerticalSpace()) {
            //如果滑动到最底部
            travel = mTotalHeight - getVerticalSpace() - mSumy;
        }
        mSumy += travel;
        //获取可见的区域
        Rect visibleArea = getVisibleArea();
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child    = getChildAt(i);
            int  position = getPosition(child);
            Rect rect     = mItemRects.get(position);
            //判读child是否是在当前的显示窗口中
            if (!Rect.intersects(rect, visibleArea)) {
                removeAndRecycleView(child, recycler);
            }
        }

        View lastView  = getChildAt(getChildCount() - 1);
        View firstView = getChildAt(0);

        //首先将所有的view 去除掉
        detachAndScrapAttachedViews(recycler);

        //向下滑动
        if (travel >= 0) {
            int minPos = getPosition(firstView);
            for (int i = minPos; i < getItemCount(); i++) {
                insertView(i, visibleArea, recycler, false);
            }
        }
        //向上滑动
        else {
            int maxPos = getPosition(lastView);
            for (int i = maxPos; i >= 0; i--) {
                insertView(i, visibleArea, recycler, true);
            }
        }
        return travel;
    }

    private void insertView(int pos, Rect visibleArea, RecyclerView.Recycler recycler, boolean firstPos) {
        Rect rect = mItemRects.get(pos);
        if (Rect.intersects(visibleArea, rect)) {
            View child = recycler.getViewForPosition(pos);
            if (firstPos) {
                addView(child, 0);
            } else {
                addView(child);
            }
            measureChildWithMargins(child, 0, 0);
            layoutDecoratedWithMargins(child, rect.left, rect.top - mSumy, rect.right,
                    rect.bottom - mSumy);
            //在布局item后，修改每个item的旋转度数
            child.setRotationY(child.getRotationY() + 1);
        }
    }

    private Rect getVisibleArea() {
        return new Rect(getPaddingLeft(),
                getPaddingTop() + mSumy,
                getWidth() + getPaddingRight()
                , getVerticalSpace() + mSumy);
    }
}
