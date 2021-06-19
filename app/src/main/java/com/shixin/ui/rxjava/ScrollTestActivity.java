package com.shixin.ui.rxjava;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.shixin.R;

public class ScrollTestActivity extends AppCompatActivity {
    private LinearLayout itemRoot;
    private int mlastX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_test);
        itemRoot = (LinearLayout) findViewById(R.id.lin_root);
    }

    private static String TAG = "-------------";

    private int mDownX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*int scrollX = itemRoot.getScrollX();
        int x = (int)com.shixin.event.getX();

        if (com.shixin.event.getAction() == MotionEvent.ACTION_MOVE){
            int newScrollX = scrollX + mlastX - x;
            itemRoot.scrollTo(newScrollX,0);
        }
        mlastX = x;*/

        int x = (int) event.getX();
        int scrollX = itemRoot.getScrollX();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int movex = x - mDownX;

                if (movex < 0 && -movex + scrollX >= 400) {
                    movex = -(-movex - ((-movex + scrollX) - 400));
                   // System.out.println("----------movex" + (-movex + scrollX));
                }


                //System.out.println("+-------------计算前movex=" + movex);


                if (movex > 0 && -movex + scrollX < 0) {
                    //System.out.println("+----------movex" + (-movex + scrollX));
                    //例如scroll为13    moveX = 27
                    //只需要向右滑动-13 即可
                    // -27+13+27 = 13  下列表达式
                    movex = (movex/*右滑正值*/ + ((-movex + scrollX)/*计算不加控制的滑动距离，负值*/));
                }
                System.out.println("-----------移动的距离="
                        + movex
                        + "----scrollX="
                        + scrollX);
                if (scrollX <= 400 && scrollX >= 0)
                    itemRoot.scrollBy(-movex, 0); //可以提供另一种思路就是  当>400 <0 d的时候可以调用scrollTo这个方法
                break;
            case MotionEvent.ACTION_UP:
                System.out.println(TAG+"ACTION_UP");
                break;
            //在设计设置页面的滑动开关时，如果不监听ACTION_CANCEL，在滑动到中间时，如果你手指上下移动，
            //就是移动到开关控件之外，则此时会触发ACTION_CANCEL，而不是ACTION_UP，造成开关的按钮停顿在中间位置。
            //超出了父控件监听的范围
            case MotionEvent.ACTION_CANCEL:
                System.out.println(TAG+"ACTION_CANCEL");
                break;

        }
        mDownX = x;
        return super.onTouchEvent(event);
    }
}
