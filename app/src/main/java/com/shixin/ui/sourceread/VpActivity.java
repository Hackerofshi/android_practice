package com.shixin.ui.sourceread;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.shixin.rxjava.R;

import java.util.ArrayList;

public class VpActivity extends AppCompatActivity {

    private ViewPager       vp;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp);
        initView();
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.ViewPager);
        btn = (Button) findViewById(R.id.btn);

        aList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        View inflate = li.inflate(R.layout.item_vp, null, false);
        aList.add(inflate);
        aList.add(li.inflate(R.layout.item_vp1,null,false));
        aList.add(li.inflate(R.layout.item_vp2,null,false));
        aList.add(li.inflate(R.layout.item_vp3,null,false));
        mAdapter = new MyPagerAdapter(aList);

        vp.setAdapter(mAdapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(2);
            }
        });

    }

    public class MyPagerAdapter extends PagerAdapter {
        private ArrayList<View> viewLists;

        public MyPagerAdapter(ArrayList<View> viewLists) {
            super();
            this.viewLists = viewLists;
        }

        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewLists.get(position));
            return viewLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewLists.get(position));
        }
    }
}
