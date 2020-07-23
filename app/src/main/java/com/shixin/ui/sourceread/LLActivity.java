package com.shixin.ui.sourceread;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.shixin.rxjava.R;

public class LLActivity extends AppCompatActivity {
    /**
     * 结论：
     * 1. 测量过程发生在onCreateView()阶段，在onStart()方法之后，视图才能得到自身的实际高和宽
     *
     * 2. 如果layout_width 不是“wrap_content ”则measureWithLargestChild不起作用（这个没在此表格中体现出来，读者可以自己测试）
     *
     * 3. 当measureWithLargestChild=true 并且子视图总测量宽度>屏幕实际宽度时，
     * 所有带权重（weight）的子元素都会具有最大子元素的测量宽度，但带权重的子元素最后实际宽度却不是，
     * 会出现布局异常;并且LinearLayout的实际宽度=屏幕最大宽度（这里是480）
     *
     * 4. 当measureWithLargestChild=true 并且子视图总测量宽度<屏幕实际宽度时，
     * 所有带权重（weight）的子元素都会具有最大子元素的测量宽度和实际宽度；
     * 并且LinearLayout的实际宽度=最大子元素的宽度*子元素个数（这里是112*4=448）
     * 5. 当measureWithLargestChild=false时，不受以上约束
     *
     * 6. 当父layout_width =“wrap_content “时，weight属性不起作用（由linearLayout_false 得出此结论）
     *
     * 7. Button宽度计算公式：
     * 原始宽度+权重*父视图剩余空间/权重和
     * 7.1 未指定android:weightSum属性时，权重和=所有子控件的weight之和，weight未指定时为0
     * 7.2如果指定了android:weightSum属性，权重和=android:weightSum指定的值。不管子控件weight和是多少
     * 7.3weight是对剩余空间的分配而不是对LinearLayout空间的分配
     * ————————————————
     */

    private EditText textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ll);
        textView = (EditText) findViewById(R.id.textView1);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearlayout);
        layout.measure(0, 0);
        traversalView(layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        textView.append("\n\nWeightActivityFragment.onStart\n\n");
    }

    /**
     *采用递归方法遍历所有view
     *
     * @param viewGroup
     */
    public void traversalView(ViewGroup viewGroup) {
        //如果是LinearLayout 输出其宽
        if(viewGroup instanceof LinearLayout)
            doView(viewGroup);
        //求当前ViewGroup下子视图的总数量
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            //获取第I个子视图
            View view = viewGroup.getChildAt(i);
            //如果子视图属于ViewGroup,有可能其下仍然含有子视图，继续判断
            if (view instanceof ViewGroup) {
                traversalView((ViewGroup) view);
            } else {
                doView(view);
            }
        }

    }

    /**
     * 处理view
     * getMeasuredWidth()和getWidth()区别
     * getWidth(): View在布局完成后整个View的实际宽度。
     * getMeasuredWidth(): 对View上的内容进行测量后得到的View占据的宽度,有可能大于实际宽度
     *
     *
     * @param view
     */
    private void doView(final View view) {
        if(view instanceof Button) {
            textView.append(((Button) view).getText().toString().toUpperCase()
                    + " MeasuredWidth=" + view.getMeasuredWidth() + "\n");
            view.post(new Runnable() {
                @Override
                public void run() {
                    textView.append(((Button) view).getText().toString().toUpperCase()
                            + " RealWidth=" + view.getWidth() + "\n");
                }
            });
        }
        else if (view instanceof  LinearLayout)
        {
            textView.append(view.getTag()+" MeasuredWidth="+view.getMeasuredWidth()+"\n");
            //利用View的post()方法求出得View的宽度
            //如果直接使用getWidth()方法，返回的结果是0
            view.post(new Runnable() {
                @Override
                public void run() {
                    textView.append(view.getTag()+" RealWidth=" + view.getWidth() + "\n");

                }
            });
        }
    }

}
