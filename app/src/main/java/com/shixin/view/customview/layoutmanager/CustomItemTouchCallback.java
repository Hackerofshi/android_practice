package com.shixin.view.customview.layoutmanager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ProjectName: Android_Pratice
 * @Package: com.shixin.view.customview.layoutmanager
 * @ClassName: CustomItemTouchCallback
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2021/5/4 15:26
 * @UpdateUser: shixin：
 * @UpdateDate: 2021/5/4 15:26
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class CustomItemTouchCallback  extends ItemTouchHelper.Callback{

    private final ItemTouchStatus mItemTouchStatus;

    public CustomItemTouchCallback(ItemTouchStatus mItemTouchStatus) {
        this.mItemTouchStatus = mItemTouchStatus;
    }

    /**
     * 在此方法里面我们需要构建两个flag，一个是dragFlags，表示拖动效果支持的方向，另一个是swipeFlags，
     * 表示侧滑效果支持的方向。在我们的Demo中，拖动执行上下两个方向，侧滑执行左右两个方向，这些操作我们都可以在此方法里面定义。
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //上下拖动
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //滑动
        int swipeFlags = ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT;

        return makeMovementFlags(dragFlags,swipeFlags);
    }

    /**
     * 当拖动效果已经产生了，会回调此方法。在此方法里面，我们通常会更新数据源，就比如说，
     * 一个ItemView从0拖到了1位置，那么对应的数据源也需要更改位置。
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //交换在数据源中相应的位置
        return mItemTouchStatus.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
    }

    /**
     * 当侧滑效果以上产生了，会回调此方法。在此方法里面，我们也会更新数据源。与onMove方法不同到的是，
     * 我们在这个方法里面从数据源里面移除相应的数据，然后调用notifyXXX方法就行了。
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // 从数据源中移除相应的数据
        mItemTouchStatus.onItemRemove(viewHolder.getAdapterPosition());
    }
}