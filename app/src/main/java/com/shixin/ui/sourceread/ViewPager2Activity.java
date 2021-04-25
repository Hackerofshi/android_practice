package com.shixin.ui.sourceread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shixin.rxjava.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager2);

        ViewPager2       viewPager2       = findViewById(R.id.viewpager2);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

    }

    class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder> {
        private List<Integer> colors = new ArrayList<>();

        {
            colors.add(android.R.color.black);
            colors.add(android.R.color.holo_purple);
            colors.add(android.R.color.holo_blue_dark);
            colors.add(android.R.color.holo_green_light);
        }

        @NonNull
        @Override
        public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewPagerViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_page, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
            holder.mTvTitle.setText("item " + position);
            holder.mContainer.setBackgroundResource(colors.get(position));
        }

        @Override
        public int getItemCount() {
            return colors.size();
        }

        class ViewPagerViewHolder extends RecyclerView.ViewHolder {
            TextView       mTvTitle;
            RelativeLayout mContainer;

            public ViewPagerViewHolder(@NonNull View itemView) {
                super(itemView);
                mContainer = itemView.findViewById(R.id.container);
                mTvTitle = itemView.findViewById(R.id.tvTitle);
            }
        }
    }
}