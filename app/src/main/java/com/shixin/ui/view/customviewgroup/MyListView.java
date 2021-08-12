package com.shixin.ui.view.customviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.shixin.R;

/**
 * Created by shixin on 2017/3/23 0023.
 */

public class MyListView extends ListView implements AbsListView.OnScrollListener{
    private View mFootView;
    private int mTotalItemCount; //item的总数
    private OnLoadMoreListener mLoadMoreListener;

    private boolean mIsLoading=false;//是否正在加载


    public MyListView(Context context) {
        this(context,null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);



    }

    private void init(Context context) {

        mFootView= LayoutInflater.from(context).inflate(R.layout.foot_view,null);
        setOnScrollListener(this);


    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        // 滑到底部后，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        int lastVisibleIndex=listView.getLastVisiblePosition();
        if (!mIsLoading&&scrollState == OnScrollListener.SCROLL_STATE_IDLE//停止滚动
                && lastVisibleIndex ==mTotalItemCount-1) {//滑动到最后一项
            mIsLoading=true;
            addFooterView(mFootView);
            if (mLoadMoreListener!=null) {
                mLoadMoreListener.onloadMore();
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }




    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount=totalItemCount;
    }

    public void setONLoadMoreListener(OnLoadMoreListener listener){
        mLoadMoreListener=listener;
    }

    public interface OnLoadMoreListener{
        void onloadMore();
    }

    public void setLoadCompleted(){
        mIsLoading=false;
        removeFooterView(mFootView);
    }
}

