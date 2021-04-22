package com.shixin.customview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

/**
 * @ProjectName: Android_Pratice
 * @Package: com.shixin.customview
 * @ClassName: CustomSnapHelper
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2021/4/21 21:23
 * @UpdateUser: shixin：
 * @UpdateDate: 2021/4/21 21:23
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class CustomSnapHelper extends SnapHelper {

    @Override
    public int[] calculateDistanceToFinalSnap(
            @NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {

        if (layoutManager instanceof CustomLayoutManager1) {
            int[] out = new int[2];
            if (layoutManager.canScrollHorizontally()) {
                out[0] = ((CustomLayoutManager1) layoutManager).calculateDistanceToPosition(
                        layoutManager.getPosition(targetView));
                out[1] = 0;
            } else {
                out[0] = 0;
                out[1] = ((CustomLayoutManager1) layoutManager).calculateDistanceToPosition(
                        layoutManager.getPosition(targetView));
            }
            return out;
        }
        return null;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX,
                                      int velocityY) {
        return RecyclerView.NO_POSITION;
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof CustomLayoutManager1) {
            int pos = ((CustomLayoutManager1) layoutManager).getFixedScrollPosition();
            if (pos != RecyclerView.NO_POSITION) {
                return layoutManager.findViewByPosition(pos);
            }
        }
        return null;
    }
}
