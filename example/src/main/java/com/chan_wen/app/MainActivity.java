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

package com.chan_wen.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.Toast;

import com.owen.focus.FocusBorder;
import com.owen.tab.TvTabLayout;
import com.chan_wen.app.entity.Movie;
import com.chan_wen.app.fragment.BaseFragment;
import com.chan_wen.app.fragment.GridFragment;
import com.chan_wen.app.player.VideoPlayerPageActivity;
import com.owen.tvrecyclerview.utils.Loger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author ZhouSuQiang
 */
public class MainActivity extends AppCompatActivity implements BaseFragment.FocusBorderHelper{
    private final String LOGTAG = MainActivity.class.getSimpleName();
    private final String[] tabNames = {
//            "VLayout", "Metro", "Spannable",
//            "Nested", "Menu&List",
            "企业风采",
            "会员专区"
//            "V7Grid", "Staggered", "UpdateData"
    };
    private final String[] fragments = {
//            VLayoutFragment.class.getName(), MetroFragment.class.getName(), SpannableFragment.class.getName(),
//            NestedFragment.class.getName(), ListFragment.class.getName(),
            GridFragment.class.getName(),
            GridFragment.class.getName()
//            V7GridFragment.class.getName(), StaggeredFragment.class.getName(), UpdateDataFragment.class.getName()
    };

    @BindView(R.id.tab_layout)
    TvTabLayout mTabLayout;
    
    private FocusBorder mFocusBorder;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    
        //是否打开TvRecyclerView的log打印
        Loger.isDebug = true;
        
        // 移动框
        if(null == mFocusBorder) {
            mFocusBorder = new FocusBorder.Builder()
                    .asColor()
                    .borderColorRes(R.color.actionbar_color)
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 3.2f)
                    .shadowColorRes(R.color.green_bright)
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 22f)
                    .build(this);
        }
        
        mTabLayout.addOnTabSelectedListener(new TabSelectedListener());
        for(String tabName : tabNames) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabName));
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //1、通过语音控制功能打开app
            String voiceKey = bundle.getString("name");
            if (voiceKey != null) {
                progressDialog = ProgressDialog.show(this, "提示", "正在解析语音数据，请稍后...");
                progressDialog.show();
                requestServerData(voiceKey);
            } else {
                Toast.makeText(this, "解析语音失败!", Toast.LENGTH_SHORT).show();
            }
        } else {
            //2、通过普通方式启动app
            mTabLayout.selectTab(0);
        }
    }

    @Override
    public FocusBorder getFocusBorder() {
        return mFocusBorder;
    }

    public class TabSelectedListener implements TvTabLayout.OnTabSelectedListener {
        private GridFragment mFragment;

        @Override
        public void onTabSelected(TvTabLayout.Tab tab) {
            final int position = tab.getPosition();
            mFragment = (GridFragment) getSupportFragmentManager().findFragmentByTag(position + "");
            FragmentTransaction mFt = getSupportFragmentManager().beginTransaction();
            if (mFragment == null) {
                mFragment = (GridFragment) Fragment.instantiate(MainActivity.this, fragments[position]);
                mFragment.setPosition(tab.getPosition());
                mFt.add(R.id.content, mFragment, String.valueOf(position));
            } else {
                mFt.attach(mFragment);
            }
            mFt.commit();
        }

        @Override
        public void onTabUnselected(TvTabLayout.Tab tab) {
            if (mFragment != null) {
                FragmentTransaction mFt = getSupportFragmentManager().beginTransaction();
                mFt.detach(mFragment);
                mFt.commit();
            }
        }

        @Override
        public void onTabReselected(TvTabLayout.Tab tab) {

        }
    }

    /**
     * 开始获取服务器数据
     */
    private void requestServerData(String voiceKey) {
        BmobQuery<Movie> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereContains("title", voiceKey);
        categoryBmobQuery.findObjects(new FindListener<Movie>() {
            @Override
            public void done(List<Movie> list, BmobException e) {
                progressDialog.dismiss();
                if (e == null && list != null && !list.isEmpty()) {
                    Movie movie = list.get(0);
                    Intent intent = new Intent(MainActivity.this, VideoPlayerPageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(VideoPlayerPageActivity.MOVIE, movie.getVideoFile().getFileUrl());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "抱歉，未找到您需要的内容，请重试！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
