package com.superc.lib.widget.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.superc.lib.presenter.SPresenter;
import com.superc.lib.ui.SView;
import com.superc.lib.widget.recyclerview.holder.ItemViewDelegate;

import java.util.List;

/**
 * Created by owner on 2017/8/21.
 */
public class CommonRAdapter<T, V extends SView<P>, P extends SPresenter<V>> extends WrapperAdapter<T> {

    public CommonRAdapter(Context context, List<T> list, @NonNull ItemViewDelegate<T, V, P>... itemViewDelegateList) {
        super(context, list);
        if (itemViewDelegateList.length > 0) {
            for (ItemViewDelegate d : itemViewDelegateList) {
                addItemViewDelegate(d);
            }
        }
    }
}