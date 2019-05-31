package com.chan_wen.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chan_wen.app.R;
import com.owen.adapter.CommonRecyclerViewAdapter;
import com.owen.adapter.CommonRecyclerViewHolder;
import com.chan_wen.app.App;
import com.chan_wen.app.data.ItemBean;
import com.owen.tvrecyclerview.widget.MetroGridLayoutManager;
import com.owen.tvrecyclerview.widget.MetroTitleItemDecoration;

/**
 * Created by owen on 2017/7/14.
 */

public class MetroAdapter extends CommonRecyclerViewAdapter<ItemBean> implements MetroTitleItemDecoration.Adapter{
    public MetroAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item;
    }
    
    public void showImage(CommonRecyclerViewHolder helper, int viewId, String url) {
        ImageView imageView = helper.getHolder().getView(viewId);
        Glide.with(App.get()).load(url).into(imageView);
    }

    @Override
    public void onBindItemHolder(CommonRecyclerViewHolder helper, ItemBean item, int position) {
        helper.getHolder()
                .setText(R.id.title, String.valueOf(position));
        showImage(helper, R.id.image, item.imgUrl);
        
        final View itemView = helper.itemView;
        MetroGridLayoutManager.LayoutParams lp = (MetroGridLayoutManager.LayoutParams) itemView.getLayoutParams();

        if(position > 18){
            lp.sectionIndex = 2;
            lp.rowSpan = 3;
            lp.colSpan = 2;
        }
        else if(position > 6) {
            lp.sectionIndex = 1;
            lp.isSuportIntelligentScrollEnd = false;
            lp.isSuportIntelligentScrollStart = true;
            if(position < 10) {
                lp.rowSpan = 15;
                lp.colSpan = 20;
            } else if(position < 14) {
                lp.rowSpan = 9;
                lp.colSpan = 15;
            } else {
                lp.rowSpan = 7;
                lp.colSpan = 12;
            }
        } else {
            lp.sectionIndex = 0;
            if(position == 0 || position == 6) {
                lp.rowSpan = 3;
                lp.colSpan = 4;
            } else {
                lp.rowSpan = 6;
                lp.colSpan = 4;
            }
        }
        itemView.setLayoutParams(lp);
    }

    @Override
    public View getTitleView(int index, RecyclerView parent) {
        switch (index) {
            case 1:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title2, parent, false);
            case 2:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title3, parent, false);
        }
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
    }
}
