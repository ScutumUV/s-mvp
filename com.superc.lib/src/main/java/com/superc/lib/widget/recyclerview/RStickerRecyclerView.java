package com.superc.lib.widget.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.superc.lib.widget.recyclerview.StickyVIew.StickyRecyclerHeadersDecoration;
import com.superc.lib.widget.recyclerview.StickyVIew.StickyRecyclerHeadersTouchListener;
import com.superc.lib.widget.recyclerview.adapter.RAdapter;
import com.superc.lib.widget.recyclerview.adapter.WrapperStickerAdapter;

/**
 * 分组组头悬停的RecyclerView
 * <p>
 * Created by superchen on  2017/9/11
 * The last edit time       2017/9/11
 */
public class RStickerRecyclerView extends RRecyclerView<RStickerRecyclerView> {

    public RStickerRecyclerView(Context context) {
        super(context, null);
        init();
    }

    public RStickerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public RStickerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setShowStickerHeader(true);
    }

    public <T extends WrapperStickerAdapter> void setAdapter(@NonNull T adapter) {
        if (rAdapter != null) {
            rAdapter.registerAdapterDataObserver(rDataObserver);
            rDataObserver.onChanged();
        }
        if (rHelper.isShowStickerHeader()) {
            final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
            addItemDecoration(headersDecor);
            if (rHelper.isStickerHeaderClickEnable() && rHelper.getOnHeaderClickListener() != null) {
                StickyRecyclerHeadersTouchListener stickyRecyclerHeadersTouchListener =
                        new StickyRecyclerHeadersTouchListener(this, headersDecor);
                stickyRecyclerHeadersTouchListener.setOnHeaderClickListener(rHelper.getOnHeaderClickListener());
            }
        }
        rAdapter = new RAdapter(getContext(), adapter, rHelper);
        super.setAdapter(rAdapter);
        adapter.registerAdapterDataObserver(rDataObserver);
        rDataObserver.onChanged();
    }

    public void setOnHeaderClickListener(StickyRecyclerHeadersTouchListener.OnHeaderClickListener onHeaderClickListener) {
        rHelper.setOnHeaderClickListener(onHeaderClickListener);
    }

    public void setShowStickerHeader(boolean showStickerHeader) {
        rHelper.setShowStickerHeader(showStickerHeader);
    }

    public boolean isShowStickerHeader() {
        return rHelper.isShowStickerHeader();
    }

    public void setStickerHeaderClickEnable(boolean stickerHeaderClickEnable) {
        rHelper.setStickerHeaderClickEnable(stickerHeaderClickEnable);
    }

    public boolean isStickerHeaderClickEnable() {
        return rHelper.isStickerHeaderClickEnable();
    }


}
