package com.superc.lib.widget.recyclerview.callback;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.superc.lib.widget.recyclerview.adapter.WrapperAdapter;

import java.util.Collections;
import java.util.List;

/**
 * 滑动或拖拽帮助类，理论上是应该可以支持所有列表View的，这里只支持了RecyclerView
 * <p>
 * Created by superchen on  2017/9/11
 * The last edit time       2017/9/11
 */
public class RecyItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    public static final float ALPHA_FULL = 1.0f;

    private RecyclerView.Adapter mAdapter;
    /**
     * 是否需要侧滑删除
     */
    private boolean isSwipeEnable = false;
    /**
     * 需要固定顺序不允许拖拽的List
     */
    private List<Integer> mImmobilizationPositionList;

    public RecyItemTouchHelperCallBack(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    public RecyItemTouchHelperCallBack(RecyclerView.Adapter adapter, boolean isSwipeEnable) {
        mAdapter = adapter;
        this.isSwipeEnable = isSwipeEnable;
    }

    public RecyItemTouchHelperCallBack(RecyclerView.Adapter adapter, boolean isSwipeEnable, List<Integer> immobilizationPositionList) {
        mAdapter = adapter;
        this.isSwipeEnable = isSwipeEnable;
        this.mImmobilizationPositionList = immobilizationPositionList;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (mImmobilizationPositionList != null && mImmobilizationPositionList.contains(toPosition) ||
                mImmobilizationPositionList.contains(fromPosition)) {
            return false;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(((WrapperAdapter) mAdapter).getDatas(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(((WrapperAdapter) mAdapter).getDatas(), i, i - 1);
            }
        }
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int adapterPosition = viewHolder.getAdapterPosition();
        mAdapter.notifyItemRemoved(adapterPosition);
        ((WrapperAdapter) mAdapter).getDatas().remove(adapterPosition);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) itemView.getWidth();
            itemView.setAlpha(alpha);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(ALPHA_FULL);
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isSwipeEnable;
    }

    public void setSwipeEnable(boolean swipeEnable) {
        isSwipeEnable = swipeEnable;
    }

    public void setmImmobilizationPositionList(List<Integer> mImmobilizationPositionList) {
        this.mImmobilizationPositionList = mImmobilizationPositionList;
    }

    public List<Integer> getmImmobilizationPositionList() {
        return mImmobilizationPositionList;
    }
}
