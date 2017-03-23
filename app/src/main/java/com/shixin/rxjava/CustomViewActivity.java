package com.shixin.rxjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import customDrawable.CircleImageDrawable;
import customDrawable.MessageListItem;
import customDrawable.RoundImageDrawable;
import customview.CircleMenuLayout;

public class CustomViewActivity extends AppCompatActivity {
    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[]{"安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡"};
    private int[] mItemImgs = new int[]{R.drawable.home_mbank_1_normal,
            R.drawable.home_mbank_2_normal, R.drawable.home_mbank_3_normal,
            R.drawable.home_mbank_4_normal, R.drawable.home_mbank_5_normal,
            R.drawable.home_mbank_6_normal};
    private ImageView image;
    private MessageListItem me;
    private ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //自已切换布局文件看效果
//		setContentView(R.layout.activity_main02);
        setContentView(R.layout.activity_custom_view);

        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);


        mCircleMenuLayout.setOnMentItemClickListener(new CircleMenuLayout.OmMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                Toast.makeText(CustomViewActivity.this, mItemTexts[pos],
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void itemCenterClick(View view) {

            }
        });


        findViewById(R.id.ll).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


        image = (ImageView) findViewById(R.id.image);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.hello);

        image.setImageDrawable(new CircleImageDrawable(bitmap));
        me = (MessageListItem) findViewById(R.id.re);
        img1 = (ImageView) findViewById(R.id.id_msg_item_icon);
        me.setMessageReaded(false);


    }
}
