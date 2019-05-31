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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chan_wen.app.R;
import com.owen.adapter.CommonRecyclerViewAdapter;
import com.owen.adapter.CommonRecyclerViewHolder;
import com.chan_wen.app.App;
import com.chan_wen.app.entity.Image;
import com.chan_wen.app.entity.Movie;

public class GridAdapter extends CommonRecyclerViewAdapter<Object> {
    public GridAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item;
    }

    @Override
    public void onBindItemHolder(CommonRecyclerViewHolder helper, Object item, int position) {
        if (item instanceof Image) {
            Image image = (Image) item;
            helper.getHolder()
                    .setText(R.id.tv_content, image.getTitle());
            showImage(helper, R.id.image, image.getImgFile().getFileUrl());
        } else if (item instanceof Movie) {
            Movie movie = (Movie) item;
            helper.getHolder()
                    .setText(R.id.tv_content, movie.getTitle());
            showImage(helper, R.id.image, movie.getImageFile().getFileUrl());
        }
    }

    public void showImage(CommonRecyclerViewHolder helper, int viewId, String url) {
        ImageView imageView = helper.getHolder().getView(viewId);
        Glide.with(App.get()).load(url).into(imageView);
    }
}
