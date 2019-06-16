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

package com.chan_wen.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chan_wen.app.ImageBrowserActivity;
import com.chan_wen.app.player.VideoPlayerPageActivity;
import com.owen.adapter.CommonRecyclerViewAdapter;
import com.chan_wen.app.R;
import com.chan_wen.app.adapter.GridAdapter;
import com.chan_wen.app.data.ItemDatas;
import com.chan_wen.app.display.DisplayAdaptive;
import com.chan_wen.app.entity.Image;
import com.chan_wen.app.entity.Movie;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class GridFragment extends BaseFragment {

    @BindView(R.id.list)
    TvRecyclerView mRecyclerView;

    private CommonRecyclerViewAdapter mAdapter;
    private List<Movie> movieList;
    private List<Image> imageList;
    private int mPosition = 0;
    private LoadingDialog loadingDialog;

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public static GridFragment newInstance() {
        GridFragment fragment = new GridFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieList = new ArrayList<>();
        imageList = new ArrayList<>();
        setListener();
        // 推荐使用此方法
        mRecyclerView.setSpacingWithMargins(10, 10);
        mAdapter = new GridAdapter(getContext());
        loadingDialog =  new LoadingDialog(getContext())
                .setSuccessText("加载成功")//显示加载成功时的文字
                .setFailedText("加载失败")
                .setLoadingText("加载中...");//设置loading时显示的文字
        loadingDialog.show();
        switch (mPosition) {
            case 0:
                //创建视频页面
                mAdapter.setDatas(movieList);
                queryNetData();
                break;
            case 1:
                //创建图片页面
                mAdapter.setDatas(imageList);
                queryImageData();
                break;
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListener() {
        setScrollListener(mRecyclerView);

        mRecyclerView.setOnItemListener(new SimpleOnItemListener() {

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                float radius = DisplayAdaptive.getInstance().toLocalPx(10);
                onMoveFocusBorder(itemView, 1.1f, radius);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                if (mPosition == 0) {
                    Movie movie = movieList.get(position);
                    Intent intent = new Intent(getActivity(), VideoPlayerPageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(VideoPlayerPageActivity.MOVIE, movie.getVideoFile().getFileUrl());
                    getActivity().startActivity(intent);
                } else if (mPosition == 1) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Image image : imageList) {
                        list.add(image.getImgFile().getFileUrl());
                    }
                    ImageBrowserActivity.startImageBrowserActivity(list, position, getActivity());
                }
            }
        });

        mRecyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mFocusBorder.setVisible(hasFocus);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_grid;
    }

    /**
     * 请求视频数据源
     */
    private void queryNetData() {
        BmobQuery<Movie> query = new BmobQuery<>();
        query.setLimit(500);
        query.order("createdAt");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjects(new FindListener<Movie>() {
            @Override
            public void done(List<Movie> list, BmobException e) {
                if (list != null) {
                    loadingDialog.close();
                    movieList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    loadingDialog.loadFailed();
                }
            }
        });
    }

    /**
     * 请求图片数据源
     */
    private void queryImageData() {
        BmobQuery<Image> query = new BmobQuery<>();
        query.setLimit(500);
        query.order("createdAt");
        query.findObjects(new FindListener<Image>() {
            @Override
            public void done(List<Image> list, BmobException e) {
                if (list != null) {
                    loadingDialog.close();
                    imageList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    loadingDialog.loadFailed();
                }
            }
        });
    }

}
