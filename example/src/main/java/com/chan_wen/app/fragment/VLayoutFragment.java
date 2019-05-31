package com.chan_wen.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.owen.focus.FocusBorder;
import com.chan_wen.app.R;
import com.chan_wen.app.display.DisplayAdaptive;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.VLayoutManager;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author ZhouSuQiang
 * @date 2018/11/6
 */
public class VLayoutFragment extends BaseFragment {
    @BindView(R.id.list)
    TvRecyclerView mRecyclerView;
    
    private List<DelegateAdapter.Adapter> adapters;
    
    @Override
    int getLayoutId() {
        return R.layout.layout_vlayout;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setListener();
    
        final DelegateAdapter delegateAdapter = new DelegateAdapter((VLayoutManager)mRecyclerView.getLayoutManager(), true);
        mRecyclerView.setAdapter(delegateAdapter);
        mRecyclerView.setSelectedItemAtCentered(true);
//        mRecyclerView.setSpacingWithMargins(10, 10);
        
        adapters = new LinkedList<>();
        setDatas();
        delegateAdapter.setAdapters(adapters);
    
        mFocusBorder = new FocusBorder.Builder()
                .asColor()
                .borderColor(getResources().getColor(R.color.green_bright))
                .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 3)
                .shadowColor(getResources().getColor(R.color.actionbar_color))
                .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 22)
                .padding(DisplayAdaptive.getInstance().toLocalPx(-5)) //这里的apdding值与item布局的padding一致
                .build((ViewGroup) view);
    
//        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                outRect.set(5, 5, 5, 5);
//            }
//        };
//        mRecyclerView.addItemDecoration(itemDecoration);
        
        mRecyclerView.bringToFront(); //将RecyclerView移致前层, 这样焦点就会在RecyclerView底层移动
    
//        mRecyclerView.setSelection(30);
    }
    
    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                mRecyclerView.setSelection(1);
                break;
            case R.id.btn2:
                mRecyclerView.scrollToPosition(30);
                break;
            case R.id.btn4:
                mRecyclerView.scrollToPosition(70);
                break;
            case R.id.btn3:
                mRecyclerView.setSelectionWithSmooth(10);
                break;
            case R.id.btn5:
                mRecyclerView.smoothScrollToPositionWithOffset(60, 0, true);
                break;
        }
    }
    
    private void setDatas() {
        adapters.add(new TitleAdapter(getActivity(), "Title 标题"));
        
        {
            OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
//            helper.setBgColor(0xffef8ba3);
            helper.setAspectRatio(3.0f);
            helper.setColWeights(new float[]{40f});
            helper.setRowWeight(40f);
//            helper.setMargin(0, 0, 0, 10);
//            helper.setPadding(10, 10, 10, 10);
            adapters.add(new SubAdapter(getActivity(), helper, 4) {
                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    VirtualLayoutManager.LayoutParams lp = (VirtualLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                    if (position == 0) {
                        lp.rightMargin = 1;
                    } else if (position == 1) {
                    
                    } else if (position == 2) {
                        lp.topMargin = 1;
                        lp.rightMargin = 1;
                    }
                }
            });
        }
    
        adapters.add(new TitleAdapter(getActivity(), "Title 标题"));
        
        {
            OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
//            helper.setBgColor(0xff876384);
//            helper.setMargin(10, 10, 10, 10);
//            helper.setPadding(100, 10, 10, 10);
            adapters.add(new SubAdapter(getActivity(), helper, 3));
        }
    
        {
            OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
//            helper.setBgColor(0xff876384);
//            helper.setMargin(10, 10, 10, 10);
//            helper.setPadding(100, 10, 10, 10);
            adapters.add(new SubAdapter(getActivity(), helper, 4));
        }
        
        {
            GridLayoutHelper layoutHelper;
            layoutHelper = new GridLayoutHelper(8);
//            layoutHelper.setHGap(10);
//            layoutHelper.setVGap(10);
            layoutHelper.setAspectRatio(14f);
            adapters.add(new SubAdapter(getActivity(), layoutHelper, 10));
        }
    
        {
            GridLayoutHelper layoutHelper;
            layoutHelper = new GridLayoutHelper(2);
//            layoutHelper.setMargin(0, 10, 0, 10);
//            layoutHelper.setHGap(3);
            layoutHelper.setAspectRatio(13f);
            adapters.add(new SubAdapter(getActivity(), layoutHelper, 2));
        }
    
        {
            GridLayoutHelper layoutHelper;
            layoutHelper = new GridLayoutHelper(5);
//            layoutHelper.setHGap(10);
//            layoutHelper.setVGap(10);
            layoutHelper.setAspectRatio(4f);
            adapters.add(new SubAdapter(getActivity(), layoutHelper, 88));
        }
    
    }
    
    private void setListener() {
        setScrollListener(mRecyclerView);
    
        mRecyclerView.setOnInBorderKeyEventListener(new TvRecyclerView.OnInBorderKeyEventListener() {
            @Override
            public boolean onInBorderKeyEvent(int direction, View focused) {
                Log.i("zsq", "onInBorderKeyEvent...");
                switch (direction) {
                    case View.FOCUS_DOWN:
                    
                        break;
                    case View.FOCUS_UP:
                    
                        break;
                    case View.FOCUS_LEFT:
                    
                        break;
                    case View.FOCUS_RIGHT:
                        return false;
                }
                return false;
            }
        });
        
        mRecyclerView.setOnItemListener(new SimpleOnItemListener() {
            
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                float radius = DisplayAdaptive.getInstance().toLocalPx(10);
                onMoveFocusBorder(itemView, 1f);
            }
            
            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                showToast("onItemClick::"+position);
            }
        });
        
        mRecyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mFocusBorder.setVisible(hasFocus);
            }
        });

    }
    
    static class SubAdapter extends DelegateAdapter.Adapter<MainViewHolder> {
        
        private Context mContext;
        
        private LayoutHelper mLayoutHelper;
        
        
        private VirtualLayoutManager.LayoutParams mLayoutParams;
        private int mCount = 0;
        
        
        public SubAdapter(Context context, LayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        }
        
        public SubAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }
    
        @Override
        public int getItemViewType(int position) {
            return 1;
        }
    
        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }
        
        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_vlayout, parent, false));
        }
        
        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new VirtualLayoutManager.LayoutParams(mLayoutParams));
        }
        
        
        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }
        
        @Override
        public int getItemCount() {
            return mCount;
        }
    }
    
    static class TitleAdapter extends DelegateAdapter.Adapter<MainViewHolder> {
        
        private Context mContext;
        private String mTitleText;
        private LayoutHelper mLayoutHelper;
        
        public TitleAdapter(Context context, String titleText) {
            mContext = context;
            mTitleText = titleText;
            mLayoutHelper = new SingleLayoutHelper();
        }
    
        @Override
        public int getItemViewType(int position) {
            return 0;
        }
    
        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }
        
        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_vlayout_title, parent, false));
        }
        
        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
        
        }
        
        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(mTitleText + Integer.toString(offsetTotal));
        }
        
        @Override
        public int getItemCount() {
            return 1;
        }
    }
    
    static class MainViewHolder extends RecyclerView.ViewHolder {
        
        public static volatile int existing = 0;
        public static int createdTimes = 0;
        
        public MainViewHolder(View itemView) {
            super(itemView);
            createdTimes++;
            existing++;
        }
        
        @Override
        protected void finalize() throws Throwable {
            existing--;
            super.finalize();
        }
    }
}
