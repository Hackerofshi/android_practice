package com.shixin.view.customview;

import android.content.Context;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

public class MyTextView extends AppCompatTextView {

    public MyTextView(Context context) {
        super(context);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    public void layout(int l, int t, int r, int b, int w, int h) {
        layout(l, t, r, b);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }
}
