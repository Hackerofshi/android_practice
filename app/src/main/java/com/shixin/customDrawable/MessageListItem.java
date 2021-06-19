package com.shixin.customDrawable;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.shixin.ui.rxjava.R;

/**
 * Created by admin on 2017/3/9 0009.
 */

public class MessageListItem extends RelativeLayout {
    private static final int[]   STATE_MESSAGE_READED = {R.attr.state_message_readed};
    private              boolean mMessgeReaded        = false;

    public MessageListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMessageReaded(boolean readed) {
        if (this.mMessgeReaded != readed) {
            mMessgeReaded = readed;
            refreshDrawableState();
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        if (mMessgeReaded) {

//            声明了一个STATE_MESSAGE_READED，然后在mMessgeReaded=true的情况下，通过onCreateDrawableState方法，加入我们自定义的状态。
            final int[] drawableState = super
                    .onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(drawableState, STATE_MESSAGE_READED);
            return drawableState;
        }
        return super.onCreateDrawableState(extraSpace);
    }
}
