package com.superc.lib.model;

import com.superc.lib.S;

import java.util.Map;

/**
 * Created by superchen on 2017/5/10.
 */
public interface SModel<T extends SBaseModel> extends S{

    void setModel(T t);

    T getData();

    void addData();

    void updateData();

    void deleteData();

}
