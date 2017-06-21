package com.superchen.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.superchen.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by superchen on 2017/5/16.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.Holder> {

    private Context mContext;
    private String[] mDatas;
    private AdapterView.OnItemClickListener l;

    public MainAdapter(Context context, String[] datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public MainAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final MainAdapter.Holder holder, final int position) {
        holder.tv.setText(mDatas[position]);
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.onItemClick(null, holder.tv, position, holder.tv.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.length;
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_main_item)
        TextView tv;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnClickListener(AdapterView.OnItemClickListener l) {
        this.l = l;
    }
}
