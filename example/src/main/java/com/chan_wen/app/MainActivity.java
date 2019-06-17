package com.chan_wen.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.owen.focus.FocusBorder;
import com.owen.tab.TvTabLayout;
import com.chan_wen.app.fragment.BaseFragment;
import com.chan_wen.app.fragment.GridFragment;
import com.chan_wen.app.player.VideoPlayerPageActivity;
import com.owen.tvrecyclerview.utils.Loger;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chan_wen.app.HomeActivity.MEDIA_TYPE;

/**
 * 版权: 无
 * 作者: 胡涛
 * 创建日期:   2019-6-11
 * 创建时间:   23:16
 * 描述: app启动首页
 */
public class MainActivity extends AppCompatActivity implements BaseFragment.FocusBorderHelper {
    private final String[] tabNames = {
            "企业风采",
            "会员专区"
    };
    private final String[] fragments = {
            GridFragment.class.getName(),
            GridFragment.class.getName()
    };

    @BindView(R.id.tab_layout)
    TvTabLayout mTabLayout;

    private FocusBorder mFocusBorder;
    private GridFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过普通方式启动app
        init();
        String mediaType = getIntent().getStringExtra(MEDIA_TYPE);
        FragmentTransaction mFt = getSupportFragmentManager().beginTransaction();
        mFragment = (GridFragment) Fragment.instantiate(MainActivity.this,GridFragment.class.getName());
        switch (mediaType) {
            case HomeActivity.VIDEO:
                mTabLayout.selectTab(0);
                mFragment.setPosition(0);
                break;
            case HomeActivity.IMAGE:
                mTabLayout.selectTab(1);
                mFragment.setPosition(1);
                break;
            default:
                mTabLayout.selectTab(0);
                mFragment.setPosition(0);
                break;
        }
        mFt.add(R.id.content, mFragment);
        mFt.commit();
    }

    private void init() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //是否打开TvRecyclerView的log打印
        Loger.isDebug = false;
        // 移动框
        if (null == mFocusBorder) {
            mFocusBorder = new FocusBorder.Builder()
                    .asColor()
                    .borderColorRes(R.color.actionbar_color)
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 3.2f)
                    .shadowColorRes(R.color.green_bright)
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 22f)
                    .build(this);
        }

//        mTabLayout.addOnTabSelectedListener(new TabSelectedListener());
//        for (String tabName : tabNames) {
//            mTabLayout.addTab(mTabLayout.newTab().setText(tabName));
//        }
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
}
