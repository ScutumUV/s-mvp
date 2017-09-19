package com.superc.lib.model;

/**
 * Created by superchen on 2017/7/14.
 */

public interface ModelObserverChangeListener<T, P, M> {

    <T extends SModel> T onChanged(T t);
}
