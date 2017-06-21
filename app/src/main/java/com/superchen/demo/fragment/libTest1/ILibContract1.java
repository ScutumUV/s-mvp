package com.superchen.demo.fragment.libTest1;

import com.superc.lib.presenter.SPresenter;
import com.superc.lib.ui.SView;

/**
 * Created by superchen on 2017/6/8.
 */

public interface ILibContract1 {

    interface ILibContractView1 extends SView<ILibContractPresenter1> {
        void success();
    }

    interface ILibContractPresenter1 extends SPresenter {

        void getPublicKey();

        void login();
    }
}
