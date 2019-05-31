package com.chan_wen.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.owen.adapter.CommonRecyclerViewAdapter;
import com.owen.adapter.CommonRecyclerViewHolder;
import com.chan_wen.app.R;
import com.chan_wen.app.adapter.ListAdapter;
import com.chan_wen.app.data.ItemDatas;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import butterknife.BindView;

/**
 * @author ZhouSuQiang
 * @date 2018/11/19
 */
public class NestedFragment extends BaseFragment {
    @BindView(R.id.list)
    TvRecyclerView mRecyclerView;
    
    @Override
    int getLayoutId() {
        return R.layout.layout_nested;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mRecyclerView.setAdapter(new CommonRecyclerViewAdapter(getContext()) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_nested_recyclerview;
            }
    
            @Override
            public int getItemCount() {
                return 2;
            }
    
            @Override
            public void onBindItemHolder(CommonRecyclerViewHolder helper, Object item, int position) {
                TvRecyclerView recyclerView = helper.getHolder().getView(R.id.list);
                ListAdapter mAdapter = new ListAdapter(getContext(), false);
                mAdapter.setDatas(ItemDatas.getDatas(64));
                recyclerView.setSpacingWithMargins(10, 10);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setSelectedItemAtCentered(true);
    
                recyclerView.setOnItemListener(new TvRecyclerView.OnItemListener() {
                    @Override
                    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        
                    }
    
                    @Override
                    public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                        onMoveFocusBorder(itemView, 1f);
                    }
    
                    @Override
                    public void onItemClick(TvRecyclerView parent, View itemView, int position) {
        
                    }
                });
            }
            
        });
    
        mRecyclerView.setOnItemListener(new TvRecyclerView.OnItemListener() {
            @Override
            public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
            
            }
    
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
            }
    
            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
        
            }
        });
        
        mRecyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("qq", "onFocusChange1==> " +hasFocus);
                if(!hasFocus) {
                    mFocusBorder.setVisible(false);
                }
            }
        });
    }
    
    
}


