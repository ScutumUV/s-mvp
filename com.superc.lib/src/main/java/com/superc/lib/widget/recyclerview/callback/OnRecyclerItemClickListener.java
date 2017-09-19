package com.superc.lib.widget.recyclerview.callback;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.superc.lib.widget.recyclerview.RRecyclerView;
import com.superc.lib.widget.recyclerview.adapter.RAdapter;

import java.util.List;

/**
 * 为RecyclerView点击拖拽事件
 * <p>
 * Created by superchen on  2017/9/11
 * The last edit time       2017/9/11
 */
public abstract class OnRecyclerItemClickListener<T, VH extends RecyclerView.ViewHolder> implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetectorCompat;
    private RecyclerView mRecyclerView;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mGestureDetectorCompat = new GestureDetectorCompat(mRecyclerView.getContext(),
                new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(View view, VH holder, T model, int position);

    public abstract void onItemLongClick(View view, VH holder, T model, int position);

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View childViewUnder = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childViewUnder != null) {
                RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(childViewUnder);
                if (mRecyclerView instanceof RRecyclerView) {
                    RecyclerView.LayoutManager m = mRecyclerView.getLayoutManager();
                    RRecyclerView r = (RRecyclerView) mRecyclerView;
                    int position = m.getPosition(childViewUnder);
                    List<T> d = ((RAdapter) r.getAdapter()).getInnerAdapter().getDatas();
                    onItemClick(childViewUnder, (VH) childViewHolder, d.get(position), position);
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View childViewUnder = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childViewUnder != null) {
                RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(childViewUnder);
                if (mRecyclerView instanceof RRecyclerView) {
                    RecyclerView.LayoutManager m = mRecyclerView.getLayoutManager();
                    RRecyclerView r = (RRecyclerView) mRecyclerView;
                    int position = m.getPosition(childViewUnder);
                    List<T> d = ((RAdapter) r.getAdapter()).getInnerAdapter().getDatas();
                    onItemLongClick(childViewUnder, (VH) childViewHolder, d.get(position), position);
                }
            }
        }
    }
}
