package com.shixin.view.customviewgroup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.RequiresApi;

import com.shixin.ui.rxjava.R;

/**
 * Created by shixin on 2017/3/31 0031.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SwipeMenuLayout extends ViewGroup {

    private static final String TAG = "shixin/SwipeMenuLayout";
    /** 为了处理单击事件的冲突，判断屏幕上面最小的滑动距离*/
    private int mScaleTouchSlop;
    private int mMaxVelocity;   // 计算滑动的速度用
    private int mPointerId; //多点触摸只算第一根手指的速度
    private int mHeight; // 计算自己的高度

    //右侧菜单宽度总和 最大滑动距离
    private int mRightMenuWidths;


    //滑动判定临界值（右侧菜单的40%） 手指抬起时候 超过了展开  没有超过收起menu

    private int mLimit;
    private View mContentView;  //存储contentView（第一个View）

    //记录上一次的xy
    private PointF mLastP = new PointF();
    /** 仿QQ 侧滑菜单展开时，点击侧滑菜单之外的区域，关闭侧滑菜单。
    增加一个布尔值变量，dispatch函数里  每次down时，为true ，move时判断  如果是滑动动作设为false
     在intercept 函数的up时判断这个变量  如果仍为true,说明是点击事件，则关闭菜单。*/
    private boolean isUnMoved = true;

    //判断手指的起落点 如果距离属于滑动，就屏蔽一切点击事件。
    // up-down的坐标  判断是否是滑动，如果是，则屏蔽一切点击事件
    private PointF mFirstP = new PointF();
    private boolean isUserSwiped;


    //储存的是当前正在展开的View
    private static SwipeMenuLayout mViewCache;

    //防止多只手一起滑动的Flag 在每次down里判断，touch事件结束清空
    private static boolean isTouching;

    private VelocityTracker mVelocityTracker; //滑动速度变量
    private android.util.Log LogUtils;


    /**
     * 右侧删除功能的开关  默认开
     */
    private boolean isSwipeEnable;

    /**
     * IOS  QQ式交互  默认开
     */

    private boolean isIos;


    private boolean iosInterceptFlag;//IOS类型下，是否拦截事件的flag
    /**
     * 左滑右滑的开关,默认左滑打开菜单
     */
    private boolean isLeftSwipe;


    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);

        init(context, attrs, defStyleAttr);
    }


    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public boolean isSwipeEnable() {
        return isSwipeEnable;
    }

    public void setSwipeEnable(boolean swipeEnable) {
        isSwipeEnable = swipeEnable;
    }


    public boolean isIos() {
        return isIos;
    }

    public SwipeMenuLayout setIos(boolean ios) {
        isIos = ios;
        return this;

    }


    public boolean isLeftSwipe() {
        return isLeftSwipe;
    }

    /**
     * 设置是否开启左滑出菜单，设置false 为右滑出菜单
     *
     * @param leftSwipe
     * @return
     */
    public SwipeMenuLayout setLeftSwipe(boolean leftSwipe) {
        isLeftSwipe = leftSwipe;
        return this;
    }


    /**
     * 返回ViewCache
     *
     * @return
     */
    public static SwipeMenuLayout getmViewCache() {
        return mViewCache;
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();

        //右侧删除功能开关 默认开
        isSwipeEnable = true;
        //iOS 、QQ式交互  默认开
        isIos = true;
        isLeftSwipe = true;

        TypedArray ta = context.getTheme().obtainStyledAttributes
                (attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0);
        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++) {

            int attr = ta.getIndex(i);
            //如果引用成AndroidLib 资源都不是常量，无法使用switch case
            if (attr == R.styleable.SwipeMenuLayout_swipeEnable) {
                isSwipeEnable = ta.getBoolean(attr, true);
            } else if (attr == R.styleable.SwipeMenuLayout_ios) {
                isIos = ta.getBoolean(attr, true);
            } else if (attr == R.styleable.SwipeMenuLayout_leftSwipe) {
                isLeftSwipe = ta.getBoolean(attr, true);
            }
        }
        ta.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setClickable(true); // 令自己可以点击  从而获取触摸事件

        mRightMenuWidths = 0;//由于ViewHolder的复用机制，每次这里要手动恢复初始值

        mHeight = 0;
        int contentWidth = 0; //适配GridLayoutManager  将以第一个子Item(即ContentItem)的宽度为控件宽度
        int childCount = getChildCount();
        //add by 2016 08 11 为了子View的高，可以matchParent(参考的FrameLayout 和LinearLayout的Horizontal)
        final boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        boolean isNeedMeasureChildHeight = false;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.setClickable(true);
            if (childView.getVisibility() != GONE) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                mHeight = Math.max(mHeight, childView.getMeasuredHeight()/* + lp.topMargin + lp.bottomMargin*/);
                if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {  //父布局没有设置EXACTLY 同时子布局是MATCH_PARENT
                    isNeedMeasureChildHeight = true;
                }

                if (i > 0)//第一个布局是Left item_rv 从第二个开才是RightMenu
                {
                    mRightMenuWidths += childView.getMeasuredWidth();

                } else {
                    mContentView = childView;
                    contentWidth = childView.getMeasuredWidth();
                }
            }
        }

        setMeasuredDimension(getPaddingLeft() + getPaddingRight() + contentWidth, mHeight + getPaddingBottom() + getPaddingTop());
        mLimit = mRightMenuWidths * 4 / 10; //滑动判断的临界值

        //Log.d(TAG, "onMeasure() called with: " + "mRightMenuWidths = [" + mRightMenuWidths);
        if (isNeedMeasureChildHeight) {//如果子View的height有MatchParent属性的，设置子View高度
            forceUniformHeight(childCount, widthMeasureSpec);
        }


    }

    /**
     * 给MatchParent的子View设置高度
     *
     * @param childCount
     * @param widthMeasureSpec
     * @see android.widget.LinearLayout# 同名方法
     */
    private void forceUniformHeight(int childCount, int widthMeasureSpec) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent

        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec
                (getMeasuredHeight(), MeasureSpec.EXACTLY);//以父布局高度构建一个Exactly的测量参数


        for (int i = 0; i < childCount; i++) {

            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    int oldWidth = lp.width;//measureChildWithMargins 这个函数会用到宽，所以要保存一下
                    lp.width = child.getMeasuredWidth();
                    // Remeasure with new dimensions
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 0 + getPaddingLeft();
        int right = 0 + getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                if (i == 0)//第一个子View是内容 宽度设置为全屏
                {
                    childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth()
                            , getPaddingTop() + childView.getMeasuredHeight());

                    left = left + childView.getMeasuredWidth();

                } else {
                    if (isLeftSwipe) {
                        childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(),
                                getPaddingTop() + childView.getMeasuredHeight());
                        left = left + childView.getMeasuredWidth();
                    } else {
                        childView.layout(right - childView.getMeasuredWidth(), getPaddingTop(), right,
                                getPaddingTop() + childView.getMeasuredHeight());
                        right = right - childView.getMeasuredWidth();
                    }
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isSwipeEnable) {

            acquireVelocityTracker(ev);
            final VelocityTracker verTracker = mVelocityTracker;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isUserSwiped = false;
                    isUnMoved = true;
                    iosInterceptFlag = false;
                    if (isTouching) {
                        return false;
                    } else {
                        isTouching = true;
                    }
                    mLastP.set(ev.getRawX(), ev.getRawY());
                    mFirstP.set(ev.getRawX(), ev.getRawY());
                    //如果down ，View   和cancelView不一样  则让他立马还原  这把它设置为null
                    if (mViewCache != null) {
                        if (mViewCache != this) {
                            mViewCache.smoothClose();
                            iosInterceptFlag = isIos;//IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。
                        }
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    mPointerId = ev.getPointerId(0);
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (iosInterceptFlag) {
                        break;
                    }
                    float gap = mLastP.x - ev.getRawX();

                    //为了水平滑动中禁止父类ListView等再竖直滑动
                    if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10)//2016 09 29 修改此处，使屏蔽父布局滑动更加灵敏，
                    {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                    //仿QQ 侧滑菜单展开时，点击内容区域，关闭侧滑菜单，begin
                    if (Math.abs(gap) > mScaleTouchSlop) {
                        isUnMoved = false;
                    }
                    //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。end
                    //如果scroller还没有滑动结束 停止滑动动画
                    /*if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }*/
                    scrollBy((int) (gap), 0);//滑动使用scrollBy

                    //越界修正
                    if (isLeftSwipe) {
                        if (getScrollX() < 0) {
                            scrollTo(0, 0);
                        }
                        if (getScrollX() > mRightMenuWidths) {
                            scrollTo(mRightMenuWidths, 0);
                        }
                    } else {
                        if (getScrollX() < -mRightMenuWidths) {
                            scrollTo(-mRightMenuWidths, 0);
                        }
                        if (getScrollX() > 0) {
                            scrollTo(0, 0);
                        }
                    }

                    mLastP.set(ev.getRawX(), ev.getRawY());
                    break;

                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_CANCEL:
                    //判断手指落点，如果距离属于滑动了，就屏蔽一切点击事件
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                        isUserSwiped = true;
                    }
                    //add by 2016 09 11 ，IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (!iosInterceptFlag) {
                        verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                        final float velocityX = verTracker.getXVelocity(mPointerId);
                        if (Math.abs(velocityX) > 1000)//滑动速度超过阈值
                        {
                            if (velocityX < -1000) {
                                if (isLeftSwipe) {
                                    smoothExpand();
                                } else {
                                    smoothClose();
                                }
                            } else {
                                if (isLeftSwipe) {
                                    //平滑关闭menu
                                    smoothClose();
                                } else {
                                    //平滑展开menu
                                    smoothExpand();
                                }
                            }
                        } else {//否则就判断滑动距离
                            if (Math.abs(getScrollX()) > mLimit) {
                                //平滑展开Menu
                                smoothExpand();
                            } else {
                                smoothClose();
                            }
                        }
                    }
                    //释放
                    releaseVelocityTracker();
                    isTouching = false;
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent ev) {

        //禁止侧滑 点击事件不受干扰

        if (isSwipeEnable) {
            switch (ev.getAction()) {

                //fix c长按事件和侧滑的冲突
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                        return true;
                    }
                    break;
                //add by zhangxutong 2016 11 04 end
                case MotionEvent.ACTION_UP:
                    //为了在侧滑时候，屏蔽子View的点击事件
                    if (isSwipeEnable) {
                        if (getScrollX() > mScaleTouchSlop) {
                            if (ev.getX() < getWidth() - getScrollX()) {
                                if (isUnMoved) {
                                    smoothClose();
                                }
                                return true;//true表示拦截
                            }
                        }
                    } else {
                        if (-getScrollX() > mScaleTouchSlop) {
                            if (ev.getX() > -getScrollX()) {
                                //点击范围在菜单外 屏蔽
                                //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                                if (isUnMoved) {
                                    smoothClose();
                                }
                                return true;
                            }
                        }
                    }
                    //add by zhangxutong 2016 11 03 begin:
                    // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    if (isUserSwiped) {
                        return true;
                    }
                    //add by zhangxutong 2016 11 03 end

                    break;

            }

            //模仿IOS 点击其他区域关闭：
            if (iosInterceptFlag) {
                //IOS模式开启，且当前有菜单的View，且不是自己的 拦截点击事件给子View
                return true;
            }
        }

        return super.onInterceptHoverEvent(ev);
    }


    //平滑展开
    private ValueAnimator mExpandAnim, mCloseAnim;

    private boolean isExpand;//代表当前是否是展开状态 2016 11 03 add

    private void smoothExpand() {

        //展开就加入ViewCache
        mViewCache = SwipeMenuLayout.this;

        if (null != mContentView) {
            mContentView.setLongClickable(false);
        }

        cancelAnim();
        mExpandAnim = ValueAnimator.ofFloat(getScrollX(), isLeftSwipe ? mRightMenuWidths : -mRightMenuWidths);
        mExpandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnim.setInterpolator(new OvershootInterpolator());
        mExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = true;
            }
        });
        mExpandAnim.setDuration(300).start();


    }

    /**
     * 每次执行动画之前都应该先取消之前的动画
     */
    private void cancelAnim() {
        if (mCloseAnim != null && mCloseAnim.isRunning()) {
            mCloseAnim.cancel();
        }
        if (mExpandAnim != null && mExpandAnim.isRunning()) {
            mExpandAnim.cancel();
        }


    }

    private void smoothClose() {
        //Log.d(TAG, "smoothClose() called" + this);
/*        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
        invalidate();*/
        mViewCache = null;

        //2016 11 13 add 侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(true);
        }

        cancelAnim();
        mCloseAnim = ValueAnimator.ofInt(getScrollX(), 0);
        mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mCloseAnim.setInterpolator(new AccelerateInterpolator());
        mCloseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = false;

            }
        });
        mCloseAnim.setDuration(300).start();
        //LogUtils.d(TAG, "smoothClose() called with:getScrollX() " + getScrollX());

    }

    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * * 释放VelocityTracker
     *
     * @see VelocityTracker#clear()
     * @see VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    //每次ViewDetach的时候，判断一下 ViewCache是不是自己，如果是自己，关闭侧滑菜单，且ViewCache设置为null，
    // 理由：1 防止内存泄漏(ViewCache是一个静态变量)
    // 2 侧滑删除后自己后，这个View被Recycler回收，复用，下一个进入屏幕的View的状态应该是普通状态，而不是展开状态。
    @Override
    protected void onDetachedFromWindow() {
        if (this == mViewCache) {
            mViewCache.smoothClose();
            mViewCache = null;
        }
        super.onDetachedFromWindow();
    }


    //展开时，禁止长按
    @Override
    public boolean performLongClick() {
        if (Math.abs(getScrollX()) > mScaleTouchSlop) {
            return false;
        }
        return super.performLongClick();
    }

    //平滑滚动 弃用 改属性动画实现
/*    @Override
    public void computeScroll() {
        //判断Scroller是否执行完毕：
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            invalidate();
        }
    }*/

    /**
     * 快速关闭。
     * 用于 点击侧滑菜单上的选项,同时想让它快速关闭(删除 置顶)。
     * 这个方法在ListView里是必须调用的，
     * 在RecyclerView里，视情况而定，如果是mAdapter.notifyItemRemoved(pos)方法不用调用。
     */
    public void quickClose() {
        if (this == mViewCache) {
            //先取消展开动画
            cancelAnim();
            mViewCache.scrollTo(0, 0);//关闭
            mViewCache = null;
        }
    }
}
