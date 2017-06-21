package com.superc.lib.widget.recyclerview;

import com.superc.lib.presenter.SPresenter;
import com.superc.lib.ui.SView;

/**
 * Created by superchen on 2017/6/20.
 */

public interface RContract {

    interface RView<R> extends SView<RPresenter> {
        R getData();

        void setData(R t);
    }

    interface RPresenter<R> extends SPresenter {

    }
}
