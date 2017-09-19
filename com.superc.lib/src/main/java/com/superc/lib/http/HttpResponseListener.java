/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.superc.lib.http;

import android.app.Activity;
import android.content.DialogInterface;

import com.alibaba.fastjson.JSON;
import com.superc.lib.R;
import com.superc.lib.dialog.WaitDialog;
import com.superc.lib.http.callback.CallBack;
import com.superc.lib.http.callback.ListModelCallBack;
import com.superc.lib.http.callback.ModelCallBack;
import com.superc.lib.util.LogUtils;
import com.superc.lib.util.ToastUtil;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created in Nov 4, 2015 12:02:55 PM.
 *
 * @author Yan Zhenjie.
 */
public class HttpResponseListener<T, M extends ApiModel<M>> implements OnResponseListener<T> {

    private Activity mActivity;
    /**
     * Dialog.
     */
    private WaitDialog mWaitDialog;
    /**
     * Request.
     */
    private Request<?> mRequest;
    /**
     * 结果回调.
     */
    private CallBack<T> callback;

    private ApiModel<M> m;

    /**
     * @param activity     context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */
    public HttpResponseListener(Activity activity, Request<?> request, CallBack<T> httpCallback, boolean
            canCancel, boolean isLoading) {
        this.mActivity = activity;
        this.mRequest = request;
        if (activity != null && isLoading) {
            mWaitDialog = new WaitDialog(activity);
            mWaitDialog.setCancelable(canCancel);
            mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel();
                }
            });
        }
        this.callback = httpCallback;
    }

    /**
     * 开始请求, 这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {
        if (mWaitDialog != null && !mActivity.isFinishing() && !mWaitDialog.isShowing())
            mWaitDialog.show();
    }

    /**
     * 结束请求, 这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {
        if (mWaitDialog != null && mWaitDialog.isShowing())
            mWaitDialog.dismiss();
    }

    /**
     * 成功回调.
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
        if (callback != null) {
            // 这里判断一下http响应码，这个响应码问下你们的服务端你们的状态有几种，一般是200成功。
            // w3c标准http响应码：http://www.w3school.com.cn/tags/html_ref_httpmessages.asp
            try {
                if (response.get() != null) {
                    String result = response.get().toString();
                    JSONObject json = new JSONObject(result);
                    m = JSON.parseObject(json.toString(), ApiModel.class);
                    if (responseCode == 200) {
                        if (m != null) {
                            if (m.isSuccessed()) {
                                if (json.has("value")) {
                                    if (callback instanceof ListModelCallBack) {
                                        List<M> l = JSON.parseArray(json.getString("value"), ((ListModelCallBack) callback).getClazz());
                                        ((ListModelCallBack) callback).onListSucceed(what, response, l);
                                    } else if (callback instanceof ModelCallBack) {
                                        M m = (M) JSON.parseObject(json.getString("value"), ((ModelCallBack) callback).getClazz());
                                        this.m.setData(m);
                                        ((ModelCallBack) callback).onModelSucceed(what, response, m);
                                    } else {
                                        callback.onSucceed(what, response);
                                    }
                                } else {
                                    m.setDate("未找到value字段数据");
                                }
                            } else {
                                if (!callback.onFailed(what, m.getMessage() + "")) {
                                    ToastUtil.show(mActivity, m.getMessage() + "");
                                }
                                callback.onFailed(what, m.getMessage() + "");
                            }
                        } else {
                            LogUtils.e(getClass().getSimpleName(),
                                    "The Type of HttpResponseListener<T, M extends ApiModel<M>> change to null");
                            if (!callback.onFailed(what, m.getMessage() + "")) {
                                ToastUtil.show(mActivity, m.getMessage() + "");
                            }
                            callback.onFailed(what, m.getMessage() + "");
                        }
                    } else {
                        onFailed(what, response);
                    }
                } else {
                    IllegalArgumentException e = new IllegalArgumentException("HttpManager 返回结果为空");
                    callback.onFailed(what, response, e);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onFailed(what, response, e);
            }
        }
    }

    /**
     * 失败回调.
     */
    @Override
    public void onFailed(int what, Response<T> response) {
        Exception exception = response.getException();
        if (exception instanceof NetworkError) {// 网络不好
            ToastUtil.show(mActivity, R.string.error_please_check_network);
        } else if (exception instanceof TimeoutError) {// 请求超时
            ToastUtil.show(mActivity, R.string.error_timeout);
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            ToastUtil.show(mActivity, R.string.error_not_found_server);
        } else if (exception instanceof URLError) {// URL是错的
            ToastUtil.show(mActivity, R.string.error_url_error);
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            // 没有缓存一般不提示用户，如果需要随你。
        } else {
            ToastUtil.show(mActivity, R.string.error_unKnow);
        }
        Logger.e("错误：" + exception.getMessage());
        if (callback != null) {
            callback.onFailed(what, response, exception);
        }
    }


}
