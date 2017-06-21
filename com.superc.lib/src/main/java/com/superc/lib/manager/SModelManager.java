package com.superc.lib.manager;

import com.superc.lib.model.SBaseModel;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by superchen on 2017/6/20.
 */

public class SModelManager {

    /**
     * 缓存数据(内存中的)
     */
    private static Map<String, WeakReference<SBaseModel>> mModeCache;

    public static SModelManager getInstance() {
        return Instance.m;
    }

    private static final class Instance {
        private static final SModelManager m = new SModelManager();
    }

    public SModelManager() {
        mModeCache = new Hashtable<>();
    }
}
