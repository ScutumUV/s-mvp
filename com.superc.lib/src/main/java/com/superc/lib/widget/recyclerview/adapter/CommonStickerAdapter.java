package com.superc.lib.widget.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.superc.lib.presenter.SPresenter;
import com.superc.lib.ui.SView;
import com.superc.lib.widget.recyclerview.holder.ItemStickerViewDelegate;
import com.superc.lib.widget.recyclerview.holder.ItemViewDelegate;

import java.util.List;

/**
 * Created by owner on 2017/8/21.
 */
public class CommonStickerAdapter<T, V extends SView<P>, P extends SPresenter<V>> extends WrapperStickerAdapter<T> {

    public CommonStickerAdapter(Context context, List<T> list, ItemStickerViewDelegate<T, V, P> stickerViewDelegate,
                                @NonNull ItemViewDelegate<T, V, P>... itemViewDelegateList) {
        super(context, list);
        if (stickerViewDelegate != null) {
            addStickerItemViewDelegate(stickerViewDelegate);
        }
        if (itemViewDelegateList.length > 0) {
            for (ItemViewDelegate d : itemViewDelegateList) {
                addItemViewDelegate(d);
            }
        }
    }
}