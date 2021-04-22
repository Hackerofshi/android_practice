package com.shixin.customview;

/**
 * @ProjectName: Android_Pratice
 * @Package: com.shixin.customview
 * @ClassName: ItemViewInfo
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2021/4/21 20:41
 * @UpdateUser: shixin：
 * @UpdateDate: 2021/4/21 20:41
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class ItemViewInfo {
    private int mLeft;
    private float mScale;

    public ItemViewInfo(int left, float scale) {
        this.mLeft = left;
        this.mScale = scale;
    }

    public void setLeft(int left) {
        this.mLeft = left;
    }

    public void setScale(float scale) {
        this.mScale = scale;
    }

    public int getLeft() {
        return mLeft;
    }

    public float getScale() {
        return mScale;
    }
}
