package com.shixin.ui.customview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.shixin.customview.CustomLayoutManager1;
import com.shixin.customview.CustomLayoutManagerRemould1;
import com.shixin.customview.layoutmanager.CoverFlowLayoutManager;
import com.shixin.customview.layoutmanager.CoverFlowLayoutManager1;
import com.shixin.customview.layoutmanager.RecyclerCoverFlowView;
import com.shixin.rxjava.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.internal.operators.completable.CompletableDisposeOn;

public class RVCoverActivity extends AppCompatActivity {

    private RecyclerCoverFlowView recyclerView;
    private List<String>          datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_cover);
        recyclerView = (RecyclerCoverFlowView) findViewById(R.id.rv);
        getDatas();

        //recyclerView.setLayoutManager(new CustomLayoutManager1(1.5f,0.85f));
        //recyclerView.setLayoutManager(new CustomLayoutManager());
        recyclerView.setLayoutManager(new CoverFlowLayoutManager(OrientationHelper.HORIZONTAL));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                    .inflate(R.layout.item_rv_conver, parent, false);
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
