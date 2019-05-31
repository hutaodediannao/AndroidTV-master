/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chan_wen.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chan_wen.app.R;
import com.owen.adapter.CommonRecyclerViewAdapter;
import com.owen.adapter.CommonRecyclerViewHolder;
import com.chan_wen.app.App;
import com.chan_wen.app.data.ItemBean;
import com.owen.tvrecyclerview.widget.StaggeredGridLayoutManager;

public class StaggeredAdapter extends CommonRecyclerViewAdapter<ItemBean> {
    private RecyclerView mRecyclerView;
    
    public StaggeredAdapter(Context context, RecyclerView recyclerView) {
        super(context);
        mRecyclerView = recyclerView;
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
        final boolean isVertical = mRecyclerView.getLayoutManager().canScrollVertically();
        
        final int dimenId;
        if (position % 3 == 0) {
            dimenId = R.dimen.staggered_child_medium;
        } else if (position % 5 == 0) {
            dimenId = R.dimen.staggered_child_large;
        } else if (position % 7 == 0) {
            dimenId = R.dimen.staggered_child_xlarge;
        } else {
            dimenId = R.dimen.staggered_child_small;
        }

        final int span;
        if (position == 2) {
            span = 2;
        } else {
            span = 1;
        }

        final int size = App.get().getResources().getDimensionPixelSize(dimenId);
        final StaggeredGridLayoutManager.LayoutParams lp =
                (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();

        if (!isVertical) {
            lp.span = span;
            lp.width = size;
            itemView.setLayoutParams(lp);
        } else {
            lp.span = span;
            lp.height = size;
            itemView.setLayoutParams(lp);
        }
    }
}
