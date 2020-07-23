package com.shixin.ui.customview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shixin.customview.CustomLayoutManager;
import com.shixin.rxjava.R;

import java.util.ArrayList;
import java.util.List;

public class RVActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        getDatas();
        recyclerView.setLayoutManager(new CustomLayoutManager());
        recyclerView.setAdapter(new RvAdapter());
    }

    private void getDatas() {
        datas = new ArrayList<>();
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");
        datas.add("item_rv");

    }


    class RvAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_rv,
                            parent, false);
            return new ViewHolder(inflate);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvName.setText(String.format("%s%d", datas.get(position), position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
