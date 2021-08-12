package com.shixin.ui.view.customview.layoutmanager;

/**
 * @ProjectName: Android_Pratice
 * @Package: com.shixin.ui.view.customview.layoutmanager
 * @ClassName: ItemTouchStatus
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2021/5/4 15:29
 * @UpdateUser: shixin：
 * @UpdateDate: 2021/5/4 15:29
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public interface ItemTouchStatus {

    boolean onItemMove(int fromPosition, int toPosition);

    boolean onItemRemove(int position);
}
