package com.shixin.customview.layoutmanager;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ProjectName: Android_Practice
 * @Package: com.shixin.customview.layoutmanager
 * @ClassName: RecyclerCoverFlowView
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2021/4/24 21:10
 * @UpdateUser: shixin：
 * @UpdateDate: 2021/4/24 21:10
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class RecyclerCoverFlowView extends RecyclerView {

    public RecyclerCoverFlowView(@NonNull Context context) {
        super(context);
        init();
    }

    public RecyclerCoverFlowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerCoverFlowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setChildrenDrawingOrderEnabled(true);
    }

    /**
     * 获取layoutmanager 并强制转换为coverFlowLayoutManager
     */
    public CoverFlowLayoutManager getCoverFlowLayout() {
        return ((CoverFlowLayoutManager) getLayoutManager());
    }


    /**
     * -------------------------------------
     * |  0 |  1 |  2 |  6 |  5 |  4 |  3 |
     * |  0 |  1 |  2 |  3 |  4 |  5 |  6 |
     * -------------------------------------
     * 上面是按照正常的规则显示，后显示的则会覆盖先显示的，这样就无法达到中间显示在最上面的效果，所以需要改变绘制的顺序
     * 中间的最先绘制
     * 先绘制6 再绘制 2，5 依次绘制
     * <p>
     * 中间 order = childCount - 1;
     * 左侧的依次绘制
     * 右侧的 center + childCount - 1 - i;
     *
     * @return
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int center =
                getCoverFlowLayout().getCenterPosition() -
                        getCoverFlowLayout().getFirstVisiblePosition();
        int order;
        if (i == center) {
            order = childCount - 1;
        } else if (i > center) {
            order = center + childCount - 1 - i;
        } else {
            order = i;
        }
        return order;

    }

    /*@Override
    public boolean fling(int velocityX, int velocityY) {
        //缩小滚动距离
        int                    flingX       = (int) (velocityX * 0.04f);
        CoverFlowLayoutManager manager      = getCoverFlowLayout();
        double                 distance     = getSplineFlingDistance(flingX);
        double                 newDistance  = manager.calculateDistance(velocityX, distance);
        int                    fixVelocityX = getVelocity(newDistance);
        if (velocityX > 0) {
            flingX = fixVelocityX;
        } else {
            flingX = -fixVelocityX;
        }

        int    flingY       = (int) (velocityY * 0.04f);
        double distanceY    = getSplineFlingDistance(flingY);
        double newDistanceY = manager.calculateDistanceY(velocityY, distanceY);
        int    fixVelocityY = getVelocity(newDistanceY);
        if (velocityY > 0) {
            flingY = fixVelocityY;
        } else {
            flingY = -fixVelocityY;
        }
        return super.fling(flingX, flingY);
    }*/


    private int getVelocity(double newDistance) {
        final double decelMinusOne = DECELERATION_RATE - 1.0f;
        double       aecel         = Math.log(newDistance / (mFlingFriction * mPhysicalCoeff)) * decelMinusOne / DECELERATION_RATE;
        return Math.abs((int) (Math.exp(aecel) * (mFlingFriction * mPhysicalCoeff) / INFLEXION));
    }

    /**
     * 根据松手后的滑动速度计算出fling的距离
     *
     * @param velocity
     * @return
     */
    private double getSplineFlingDistance(int velocity) {
        final double l             = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0f;
        return mFlingFriction * getPhysicalCoeff() * Math.exp(DECELERATION_RATE / decelMinusOne * l);
    }

    /**
     * --------------flling辅助类---------------
     */
    private static final float INFLEXION         = 0.35f; // Tension lines cross at (INFLEXION, 1)
    private              float mFlingFriction    = ViewConfiguration.getScrollFriction();
    private static       float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));
    private              float mPhysicalCoeff    = 0;

    private double getSplineDeceleration(int velocity) {
        final float ppi = this.getResources().getDisplayMetrics().density * 160.0f;
        float mPhysicalCoeff = SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * ppi
                * 0.84f; // look and feel tuning
        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
    }

    private float getPhysicalCoeff() {
        if (mPhysicalCoeff == 0) {
            final float ppi = this.getResources().getDisplayMetrics().density * 160.0f;
            mPhysicalCoeff = SensorManager.GRAVITY_EARTH // g (m/s^2)
                    * 39.37f // inch/meter
                    * ppi
                    * 0.84f; // look and feel tuning
        }
        return mPhysicalCoeff;
    }
}
