package com.owen.tvrecyclerview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.owen.tvrecyclerview.R;

/**
 * @author ZhouSuQiang
 * @date 2018/11/6
 */
public class VLayoutManager extends VirtualLayoutManager {
    public VLayoutManager(@NonNull Context context) {
        super(context);
    }
    
    public VLayoutManager(@NonNull Context context, int orientation) {
        super(context, orientation);
    }
    
    public VLayoutManager(@NonNull Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
    
    public VLayoutManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context);
        final TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.TvRecyclerView, defStyleAttr, 0);
        int orientation = a.getInt(R.styleable.TvRecyclerView_android_orientation, 1);
        a.recycle();
        setOrientation(orientation);
    }
    
    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
        if(parent instanceof TvRecyclerView) {
            return parent.requestChildRectangleOnScreen(child, rect, immediate);
        }
        return super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
    }
}
