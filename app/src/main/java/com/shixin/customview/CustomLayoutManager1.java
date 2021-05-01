package com.shixin.customview;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: Android_Pratice
 * @Package: com.shixin.customview
 * @ClassName: CustomLayoutManager1
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2021/4/21 20:25
 * @UpdateUser: shixin：
 * @UpdateDate: 2021/4/21 20:25
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class CustomLayoutManager1 extends RecyclerView.LayoutManager {

    private int mItemViewHeight;
    private int mItemViewWidth;
    private int mItemCount;
    private int mScrollOffset = Integer.MAX_VALUE;

    private float   mItemHeightWidthRatio;
    private float   mScale;
    private boolean mHasChild;


    private final SnapHelper mSnapHelper = new CustomSnapHelper();

    public CustomLayoutManager1(float itemHeightWidthRatio, float scale) {
        mItemHeightWidthRatio = itemHeightWidthRatio;
        mScale = scale;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mSnapHelper.attachToRecyclerView(view);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0 || state.isPreLayout()) {
            return;
        }
        if (!mHasChild) {
            mHasChild = true;
            mItemViewHeight = getVerticalSpace();
            mItemViewWidth = (int) (mItemViewHeight / mItemHeightWidthRatio);
        }
        mItemCount = getItemCount();
        mScrollOffset = makeScrollOffsetWithinRange(mScrollOffset);
        fill(recycler);
    }

    private void fill(RecyclerView.Recycler recycler) {
        // 1.初始化基本变量，bottomVisiblePosition 为item的总数
        int bottomVisiblePosition = mScrollOffset / mItemViewWidth;
        //取余数，mScrollOffset初始值为所有item的总长度，取余的初始值为0
        final int                bottomItemVisibleSize = mScrollOffset % mItemViewWidth;
        final float              offsetPercent         = bottomItemVisibleSize * 1.0f / mItemViewWidth;
        final int                space                 = getHorizontalSpace();
        int                      remainSpace           = space;
        final int                defaultOffset         = mItemViewWidth / 2;
        final List<ItemViewInfo> itemViewInfos         = new ArrayList<>();
        // 2.计算每个ItemView的位置信息(left和scale)  从倒数第一个开始计算。
        for (int i = bottomVisiblePosition - 1, j = 1; i >= 0; i--, j++) {
            //缩放以后的宽度
            double       maxOffset = defaultOffset * Math.pow(mScale, j - 1);
            //计算左侧开始的位置，从倒数第一个开始
            int          start     = (int) (remainSpace - offsetPercent * maxOffset - mItemViewWidth);
            ItemViewInfo info      = new ItemViewInfo(start, (float) (Math.pow(mScale, j - 1) * (1 - offsetPercent * (1 - mScale))));
            itemViewInfos.add(0, info);
            //减去剩余占用的宽度
            remainSpace -= maxOffset;
            if (remainSpace < 0) {
                info.setLeft((int) (remainSpace + maxOffset - mItemViewWidth));
                info.setScale((float) Math.pow(mScale, j - 1));
                break;
            }
        }
        // 3.添加最右边ItemView的相关信息
        if (bottomVisiblePosition < mItemCount) {
            final int left = space - bottomItemVisibleSize;
            itemViewInfos.add(new ItemViewInfo(left, 1.0f));
        } else {
            bottomVisiblePosition -= 1;
        }
        // 4.回收其他位置的View
        final int layoutCount   = itemViewInfos.size(); //屏幕上可以绘制的item的个数
        final int startPosition = bottomVisiblePosition - (layoutCount - 1); //从左到右，左侧开始的index
        final int endPosition   = bottomVisiblePosition;//右侧的最后一个的位置
        final int childCount    = getChildCount(); //第一次绘制时候childCount数量是0，因为itemview还没有绘制到屏幕上
        for (int i = childCount - 1; i >= 0; i--) {
            final View childView = getChildAt(i);
            final int  position  = convert2LayoutPosition(i);
            if (position > endPosition || position < startPosition) {
                detachAndScrapView(childView, recycler);
            }
        }
        // 5.先回收再布局
        detachAndScrapAttachedViews(recycler);
        for (int i = 0; i < layoutCount; i++) {
            int  position = convert2AdapterPosition(startPosition + i);
            View child    = recycler.getViewForPosition(position);
            fillChild(child, itemViewInfos.get(i));
        }
    }

    private void fillChild(View view, ItemViewInfo itemViewInfo) {
        addView(view);
        measureChildWithExactlySize(view);
        final int top = getPaddingTop();

        layoutDecoratedWithMargins(view, itemViewInfo.getLeft(), top, itemViewInfo.getLeft() + mItemViewWidth, top + mItemViewHeight);
        view.setScaleX(itemViewInfo.getScale());
        view.setScaleY(itemViewInfo.getScale());
    }


    private void measureChildWithExactlySize(View child) {
        RecyclerView.LayoutParams lp         = (RecyclerView.LayoutParams) child.getLayoutParams();
        final int                 widthSpec  = View.MeasureSpec.makeMeasureSpec(mItemViewWidth - lp.leftMargin - lp.rightMargin, View.MeasureSpec.EXACTLY);
        final int                 heightSpec = View.MeasureSpec.makeMeasureSpec(mItemViewHeight - lp.topMargin - lp.bottomMargin, View.MeasureSpec.EXACTLY);
        child.measure(widthSpec, heightSpec);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //记录滑动的距离  从左往右滑动dx是负数  反之则是正数
        int pendingScrollOffset = mScrollOffset + dx;
        mScrollOffset = makeScrollOffsetWithinRange(pendingScrollOffset);
        fill(recycler);
        int i = mScrollOffset - pendingScrollOffset + dx;
        Log.i("滑动标记：", " i = " + i);
        return i;
    }

    private int convert2LayoutPosition(int adapterPosition) {
        return mItemCount - 1 - adapterPosition;
    }

    /**
     * 转换为adapter中的位置
     * @param layoutPosition
     * @return
     */
    public int convert2AdapterPosition(int layoutPosition) {
        //itemCount为所有item的总数
        return mItemCount - 1 - layoutPosition;
    }

    /**
     * 获取所有item的总长度
     * @param scrollOffset
     * @return
     */
    private int makeScrollOffsetWithinRange(int scrollOffset) {
        //返回需要显示的总长度
        int min = Math.min(Math.max(mItemViewWidth, scrollOffset), mItemCount * mItemViewWidth);
        Log.i("mScrollOffset：", " min = " + min);
        return min;
    }

    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    public int calculateDistanceToPosition(int targetPos) {
        int pendingScrollOffset = mItemViewWidth * (convert2LayoutPosition(targetPos) + 1);
        return pendingScrollOffset - mScrollOffset;
    }

    public int getFixedScrollPosition() {
        if (mHasChild) {
            if (mScrollOffset % mItemViewWidth == 0) {
                return RecyclerView.NO_POSITION;
            }
            float position = mScrollOffset * 1.0f / mItemViewWidth;
            return convert2AdapterPosition((int) (position - 0.5f));
        }
        return RecyclerView.NO_POSITION;
    }
}
