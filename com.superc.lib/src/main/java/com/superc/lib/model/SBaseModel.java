package com.superc.lib.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by superchen on 2017/5/16.
 */
public class SBaseModel<T> implements SModel, Parcelable, Serializable {

    private static final long serialVersionUID = 6109859576148233150L;
    public static final String TYPE = "SBaseModel";

    private SBaseModel model = this;
    protected String __type;
    protected String className;
    protected String objectId;

    public SBaseModel(){
    }

//    protected SBaseModel(String name) {
//        Bundle b = in.readBundle(getClass().getClassLoader());
//        if (b != null) {
//            model = b.getParcelable("data");
//        }
//    }

    protected SBaseModel(Parcel in) {
        Bundle b = in.readBundle(getClass().getClassLoader());
        if (b != null) {
            model = b.getParcelable("data");
        }
    }

    @Override
    public SBaseModel getData() {
        return this;
    }

    @Override
    public void setModel(SBaseModel sBaseModel) {

    }

    @Override
    public void addData() {

    }

    @Override
    public void updateData() {

    }

    @Override
    public void deleteData() {

    }

    /**
     * 将Pointer转换为所需对象
     *
     * @param cls 转换目标类型
     * @return 转换后的对象, 转换失败时为null
     */
    public <T> T parseToObject(Class<T> cls) {
        T t = null;
        Gson gson = new Gson();
        try {
            // 原Pointer对象转为json, 如果用include查询的话, Pointer中除了className还会包含目标对象的数据
            String json = gson.toJson(this);
            // 转为目标对象, 多余的className正好通过转换过滤掉
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle b = new Bundle();
        b.putParcelable("data", model);
        dest.writeBundle(b);
    }

    public static final Creator<SBaseModel> CREATOR = new Creator<SBaseModel>() {
        /**
         * @param in
         * @return createFromParcel()方法中我们要去读取刚才写出的name和age字段，
         * 并创建一个Person对象进行返回，其中color和size都是调用Parcel的readXxx()方法读取到的，
         * 注意这里读取的顺序一定要和刚才写出的顺序完全相同。
         * 读取的工作我们利用一个构造函数帮我们完成了
         */
        @Override
        public SBaseModel createFromParcel(Parcel in) {
            return new SBaseModel(in); // 在构造函数里面完成了 读取 的工作
        }

        //供反序列化本类数组时调用的
        @Override
        public SBaseModel[] newArray(int size) {
            return new SBaseModel[size];
        }
    };
}
