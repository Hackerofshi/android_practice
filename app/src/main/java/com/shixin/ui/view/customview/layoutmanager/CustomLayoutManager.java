package com.shixin.ui.view.customview.layoutmanager;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CustomLayoutManager extends RecyclerView.LayoutManager {


    /*当我们派生自LayoutManager时，会强制让我们生成一个方法generateDefaultLayoutParams。
    这个方法就是RecyclerView Item的布局参数，换种说法，就是RecyclerView 子 item 的 LayoutParameters，若是想修改子Item的布局参数（比如：宽/高/margin/padding等等），那么可以在该方法内进行设置。
    一般来说，没什么特殊需求的话，则可以直接让子item自己决定自己的宽高即可（wrap_content）。
    所以，我们一般的写法是：*/
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    //在这个函数中，我主要做了两个事：
    //第一：把所有的item所对应的view加进来：

    /*
    * 首先，我们通过measureChildWithMargins(view, 0, 0);函数测量这个view，
    * 并且通过getDecoratedMeasuredWidth(view)得到测量出来的宽度，
    * 需要注意的是通过getDecoratedMeasuredWidth(view)得到的是item+decoration的总宽度。
    * 如果你只想得到view的测量宽度，通过view.getMeasuredWidth()就可以得到了

    然后通过layoutDecorated();函数将每个item摆放在对应的位置，每个Item的左右位置都是相同的，
    从左侧x=0开始摆放，只是y的点需要计算。所以这里有一个变量offsetY，
    用以累加当前Item之前所有item的高度。从而计算出当前item的位置。

    * */
    private int mTotalHeight = 0;
    private int mSumDy = 0;
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int offsetY = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width  = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            layoutDecorated(view, 0, offsetY, width, offsetY + height);
            offsetY += height;
        }

        //如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        mTotalHeight = Math.max(offsetY, getVerticalSpace());

    }


    @Override
    public boolean canScrollVertically() {
        return true;
    }




    /**
     * 2.2 添加异常判断
     * (1)、判断到顶
     * 判断到顶相对比较容易，我们只需要把所有的dy相加，如果小于0，就表示已经到顶了。就不让它再移动就行，
     * 代码如下：
     * 当手指由下往上滑时,dy>0
     * 当手指由上往下滑时,dy<0
     * (2)、判断到底
     * 判断到底的方法，其实就是我们需要知道所有item的总高度，用总高度减去最后一屏的高度(getVerticalSpace)，
     * 就是到底的时的偏移值，如果大于这个偏移值就说明超过底部了。
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int travel = dy;
        //如果滑动到最顶部
        if (mSumDy + dy < 0) {
            travel = -mSumDy;
        } else if (mSumDy + dy > mTotalHeight - getVerticalSpace()) {
            travel = mTotalHeight - getVerticalSpace() - mSumDy;
        }
        mSumDy += travel;
        Log.i("mSumDy", "scrollVerticallyBy: " + mSumDy);
        Log.i("travel", "travel: " + travel);
        // 平移容器内的item
        offsetChildrenVertical(-travel);
        return dy;
    }

    /**
     * getVerticalSpace()函数可以得到RecyclerView用于显示item的真实高度。而相比上面的onLayoutChildren，
     * 这里只添加了一句代码：mTotalHeight = Math.max(offsetY, getVerticalSpace());
     * 这里只所以取最offsetY和getVerticalSpace()的最大值是因为，offsetY是所有item的总高度，
     * 而当item填不满RecyclerView时，offsetY应该是比RecyclerView的真正高度小的，
     * 而此时的真正的高度应该是RecyclerView本身所设置的高度。
     */
    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}
