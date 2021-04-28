package com.shixin.customview.layoutmanager;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
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

    private final int mOrientation;

    public static final int HORIZONTAL          = OrientationHelper.HORIZONTAL;
    public static final int VERTICAL            = OrientationHelper.VERTICAL;
    private             int mPendingScrollPosition;
    private             int mCenterItemPosition = INVALID_POSITION;
    private             int mItemsCount;

    public static final int                INVALID_POSITION  = -1;
    public static final int                MAX_VISIBLE_ITEMS = 3;
    private final       LayoutHelper       mLayoutHelper     = new LayoutHelper(MAX_VISIBLE_ITEMS);
    @Nullable
    private             CarouselSavedState mPendingCarouselSavedState;

    public CoverFlowLayoutManager(int orientation) {
        if (HORIZONTAL != orientation && VERTICAL != orientation) {
            throw new IllegalArgumentException("orientation should be HORIZONTAL or VERTICAL");
        }
        this.mOrientation = orientation;

        //初始化为-1
        mPendingScrollPosition = INVALID_POSITION;
    }

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


        if (mOrientation == OrientationHelper.VERTICAL) {
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


            if (INVALID_POSITION != mPendingScrollPosition) {
                final int itemsCount = state.getItemCount();
                mPendingScrollPosition = 0 == itemsCount ? INVALID_POSITION : Math.max(0, Math.min(itemsCount - 1, mPendingScrollPosition));
            }

            if (INVALID_POSITION != mPendingScrollPosition) {
                mLayoutHelper.mScrollOffset = calculateScrollForSelectingPosition(mPendingScrollPosition, state);
                mPendingScrollPosition = INVALID_POSITION;
                mPendingCarouselSavedState = null;
            } else if (null != mPendingCarouselSavedState) {
                mLayoutHelper.mScrollOffset = calculateScrollForSelectingPosition(mPendingCarouselSavedState.mCenterItemPosition, state);
                mPendingCarouselSavedState = null;
            } else if (state.didStructureChange() && INVALID_POSITION != mCenterItemPosition) {
                mLayoutHelper.mScrollOffset = calculateScrollForSelectingPosition(mCenterItemPosition, state);
            }

            int  visibleCount = getHorizontalSpace() / mIntervalWidth;
            Rect visibleRect  = getVisibleArea();
            for (int i = 0; i < visibleCount; i++) {
                insertView(i, visibleRect, recycler, false);
            }
            mItemsCount = state.getItemCount();
            //如果所有子View的宽度和没有填满RecyclerView的宽度，
            // 则将宽度设置为RecyclerView的宽度
            mTotalWidth = Math.max(offsetX, getHorizontalSpace());
        }

    }

    /**
     * @return maximum scroll value to fill up all items in layout. Generally this is only needed for non cycle layouts.
     */
    private int getMaxScrollOffset() {
        return getScrollItemSize() * (mItemsCount - 1);
    }

    /**
     * @return full item size
     */
    protected int getScrollItemSize() {
        if (VERTICAL == mOrientation) {
            return mItemHeight;
        } else {
            return mItemWidth;
        }
    }

    private int calculateScrollForSelectingPosition(final int itemPosition, final RecyclerView.State state) {
        if (itemPosition == INVALID_POSITION) {
            return 0;
        }
        final int fixedItemPosition = itemPosition < state.getItemCount() ? itemPosition : state.getItemCount() - 1;
        return fixedItemPosition * (VERTICAL == mOrientation ? mItemHeight : mItemWidth);
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
        if (mLayoutHelper.mScrollOffset + dx < 0) {
            travel = -mLayoutHelper.mScrollOffset;
        } else if (mLayoutHelper.mScrollOffset + dx > getMaxOffset()) {
            //如果滑动到最底部
            travel = getMaxOffset() - mLayoutHelper.mScrollOffset;
        }

        mLayoutHelper.mScrollOffset += travel;

        Rect visibleRect = getVisibleArea();

        //回收越界子View
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child    = getChildAt(i);
            
            int  position = getPosition(child);
            Rect rect     = mItemRects.get(position);

            if (!Rect.intersects(rect, visibleRect)) {
                removeAndRecycleView(child, recycler);
                mHasAttachedItems.put(position, false);
            } else {
                layoutDecoratedWithMargins(child, rect.left - mLayoutHelper.mScrollOffset, rect.top,
                        rect.right - mLayoutHelper.mScrollOffset, rect.bottom);
                handleChildView(child, rect.left - mStartX - mLayoutHelper.mScrollOffset);
                mHasAttachedItems.put(position, true);
            }
        }
        //填充空白区域
        View lastView  = getChildAt(getChildCount() - 1);
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
            layoutDecoratedWithMargins(child, rect.left - mLayoutHelper.mScrollOffset, rect.top,
                    rect.right - mLayoutHelper.mScrollOffset, rect.bottom);

            mHasAttachedItems.put(pos, true);
            handleChildView(child, rect.left - mLayoutHelper.mScrollOffset - mStartX);
        }
    }

    private void handleChildView(View child, int moveX) {
        float radio    = computeScale(moveX);
        float rotation = computeRotationY(moveX);

        child.setScaleX(radio);
        child.setScaleY(radio);
        child.setRotationY(rotation);
    }


    private float computeScale(int x) {
        float scale = 1 - Math.abs(x * 2.0f / (8f * getIntervalWidth()));
        if (scale < 0) scale = 0;
        if (scale > 1) scale = 1;
        return scale;
    }

    private Rect getVisibleArea() {
        return new Rect(getPaddingLeft() + mLayoutHelper.mScrollOffset, getPaddingTop(),
                getWidth() - getPaddingRight() + mLayoutHelper.mScrollOffset,
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
     * @return
     */
    public int getCenterPosition() {
        int pos  = (int) (mLayoutHelper.mScrollOffset / getIntervalWidth());
        int more = (int) (mLayoutHelper.mScrollOffset % getIntervalWidth());
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
        int  pos  = getPosition(view);

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
        int    extra = mLayoutHelper.mScrollOffset % getIntervalWidth();
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
     *
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

    private static class LayoutHelper {
        private int mMaxVisibleItems;

        private int mScrollOffset;

        LayoutHelper(final int maxVisibleItems) {
            mMaxVisibleItems = maxVisibleItems;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        if (null != mPendingCarouselSavedState) {
            return new CarouselSavedState(mPendingCarouselSavedState);
        }
        final CarouselSavedState savedState = new CarouselSavedState(super.onSaveInstanceState());
        savedState.mCenterItemPosition = mCenterItemPosition;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(final Parcelable state) {
        if (state instanceof CarouselSavedState) {
            mPendingCarouselSavedState = (CarouselSavedState) state;

            super.onRestoreInstanceState(mPendingCarouselSavedState.mSuperState);
        } else {
            super.onRestoreInstanceState(state);
        }
    }


    protected static class CarouselSavedState implements Parcelable {

        private final Parcelable mSuperState;
        private       int        mCenterItemPosition;

        protected CarouselSavedState(@Nullable final Parcelable superState) {
            mSuperState = superState;
        }

        private CarouselSavedState(@NonNull final Parcel in) {
            mSuperState = in.readParcelable(Parcelable.class.getClassLoader());
            mCenterItemPosition = in.readInt();
        }

        protected CarouselSavedState(@NonNull final CarouselSavedState other) {
            mSuperState = other.mSuperState;
            mCenterItemPosition = other.mCenterItemPosition;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel parcel, final int i) {
            parcel.writeParcelable(mSuperState, i);
            parcel.writeInt(mCenterItemPosition);
        }

        public static final Parcelable.Creator<CarouselSavedState> CREATOR
                = new Parcelable.Creator<CarouselSavedState>() {
            @Override
            public CarouselSavedState createFromParcel(final Parcel parcel) {
                return new CarouselSavedState(parcel);
            }

            @Override
            public CarouselSavedState[] newArray(final int i) {
                return new CarouselSavedState[i];
            }
        };
    }

}
