package com.shixin.ui.sourceread;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shixin.rxjava.R;

import java.util.ArrayList;

public class VpActivity extends AppCompatActivity {

    private ViewPager vp;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp);
        initView();
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.ViewPager);

        aList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        View inflate = li.inflate(R.layout.item_vp, null, false);
        aList.add(inflate);
        aList.add(li.inflate(R.layout.item_vp1,null,false));
        aList.add(li.inflate(R.layout.item_vp2,null,false));
        aList.add(li.inflate(R.layout.item_vp3,null,false));
        mAdapter = new MyPagerAdapter(aList);

        vp.setAdapter(mAdapter);

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
