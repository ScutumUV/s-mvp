package com.superc.lib.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;

import com.superc.lib.util.ToastUtil;
import com.superc.lib.widget.recyclerview.callback.OnItemClickListener;
import com.superc.lib.widget.recyclerview.callback.OnRecyclerItemClickListener;
import com.superc.lib.widget.recyclerview.callback.RecyItemTouchHelperCallBack;

/**
 * 可供拖拽或侧滑删除的RecyclerView
 * <p>
 * Created by superchen on  2017/9/11
 * The last edit time       2017/9/11
 */
public class RDragRecyclerView extends RRecyclerView<RDragRecyclerView> {

    protected RecyItemTouchHelperCallBack mTouchHelperCallBack;

    public RDragRecyclerView(Context context) {
        super(context, null);
    }

    public RDragRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RDragRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RecyItemTouchHelperCallBack getTouchHelperCallBack() {
        return mTouchHelperCallBack;
    }

    public void setTouchHelperCallBack(RecyItemTouchHelperCallBack touchHelperCallBack) {
        this.mTouchHelperCallBack = touchHelperCallBack;
    }

    @Override
    public <T, VH extends ViewHolder> void setOnItemClickListener(final OnItemClickListener<T, VH> onClickListener) {
        if (onClickListener == null) return;
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createDefaultTouchHelperCallBack());
        addOnItemTouchListener(new OnRecyclerItemClickListener<T, VH>(this) {

            @Override
            public void onItemClick(View view, VH holder, T model, int position) {
                onClickListener.onItemClick(view, holder, model, position);
            }

            @Override
            public void onItemLongClick(View view, VH holder, T model, int position) {
                if (!createDefaultTouchHelperCallBack().getmImmobilizationPositionList().contains(position)) {
                    itemTouchHelper.startDrag(holder);
                } else {
                    ToastUtil.show(getContext(), "该项固定，不能移动！");
                }
            }
        });
    }

    private RecyItemTouchHelperCallBack createDefaultTouchHelperCallBack() {
        if (mTouchHelperCallBack == null) {
            mTouchHelperCallBack = new RecyItemTouchHelperCallBack(rAdapter.getInnerAdapter(), false, null);
        }
        return mTouchHelperCallBack;
    }
}
