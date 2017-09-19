package com.superc.lib.http;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.superc.lib.http.callback.CallBack;
import com.superc.lib.util.MapUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.ByteArrayRequest;
import com.yanzhenjie.nohttp.rest.ImageRequest;
import com.yanzhenjie.nohttp.rest.JsonArrayRequest;
import com.yanzhenjie.nohttp.rest.JsonObjectRequest;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by owner on 2017/8/25.
 */
public class HttpManager<T> implements SApiService {

    private Activity sContext;

    private RequestQueue mQueue;

    private DownloadQueue mdQueue;

    private int normalThreadPoolSize = 1;

    private int downloadThreadPoolSize = 1;

    private Object signRequestTag;

    public HttpManager(@NonNull Activity activity) {
        init(activity);
    }

    private void init(@NonNull Activity activity) {
        sContext = activity;
        signRequestTag = activity.getClass().getName();
    }

    private RequestQueue initRequestQueue() {
        if (mQueue == null) {
            mQueue = NoHttp.newRequestQueue(normalThreadPoolSize);
            mQueue.start();
        }
        return mQueue;
    }

    private DownloadQueue initDownloadQueue() {
        if (mdQueue == null) {
            mdQueue = NoHttp.newDownloadQueue(downloadThreadPoolSize);
            mdQueue.start();
        }
        return mdQueue;
    }

    public DownloadQueue newDownloadQueue() {
        return newDownloadQueue(downloadThreadPoolSize);
    }

    public static DownloadQueue newDownloadQueue(int threadPoolSize) {
        DownloadQueue downloadQueue = new DownloadQueue(threadPoolSize);
        downloadQueue.start();
        return downloadQueue;
    }

    public RequestQueue getRequestQueue() {
        return initRequestQueue();
    }

    public void setRequestQueue(RequestQueue mQueue) {
        this.mQueue = mQueue;
    }

    public DownloadQueue getDownloadQueue() {
        return initDownloadQueue();
    }

    public void setDownloadQueue(DownloadQueue mdQueue) {
        this.mdQueue = mdQueue;
    }

    public int getNormalThreadPoolSize() {
        return normalThreadPoolSize;
    }

    public void setNormalThreadPoolSize(int normalThreadPoolSize) {
        this.normalThreadPoolSize = normalThreadPoolSize;
    }

    public int getDownloadThreadPoolSize() {
        return downloadThreadPoolSize;
    }

    public void setDownloadThreadPoolSize(int downloadThreadPoolSize) {
        this.downloadThreadPoolSize = downloadThreadPoolSize;
    }


    public Request<String> createStringPostRequest(String url) {
        return createStringRequest(url, RequestMethod.POST);
    }

    public Request<String> createStringGetRequest(String url) {
        return createStringRequest(url, RequestMethod.GET);
    }

    public Request<String> createStringRequest(String url, RequestMethod requestMethod) {
        return new StringRequest(url, requestMethod);
    }

    public Request<JSONObject> createJsonObjectGetRequest(String url) {
        return createJsonObjectRequest(url, RequestMethod.GET);
    }

    public Request<JSONObject> createJsonObjectPostRequest(String url) {
        return createJsonObjectRequest(url, RequestMethod.POST);
    }

    public Request<JSONObject> createJsonObjectRequest(String url, RequestMethod requestMethod) {
        return new JsonObjectRequest(url, requestMethod);
    }

    public Request<JSONArray> createJsonArrayPostRequest(String url) {
        return createJsonArrayRequest(url, RequestMethod.POST);
    }

    public Request<JSONArray> createJsonArrayGetRequest(String url) {
        return createJsonArrayRequest(url, RequestMethod.GET);
    }

    public Request<JSONArray> createJsonArrayRequest(String url, RequestMethod requestMethod) {
        return new JsonArrayRequest(url, requestMethod);
    }

    public Request<Bitmap> createBitmapPostRequest(String url) {
        return createBitmapRequest(url, RequestMethod.POST);
    }

    public Request<Bitmap> createBitmapPostRequest(String url, int maxWidth, int maxHeight,
                                                   Bitmap.Config config, ImageView.ScaleType scaleType) {
        return createBitmapRequest(url, RequestMethod.POST, maxWidth, maxHeight, config, scaleType);
    }

    public Request<Bitmap> createBitmapGetRequest(String url) {
        return createBitmapRequest(url, RequestMethod.GET);
    }

    public Request<Bitmap> createBitmapGetRequest(String url, int maxWidth, int maxHeight,
                                                  Bitmap.Config config, ImageView.ScaleType scaleType) {
        return createBitmapRequest(url, RequestMethod.GET, maxWidth, maxHeight, config, scaleType);
    }

    public Request<Bitmap> createBitmapRequest(String url, RequestMethod method) {
        return createBitmapRequest(url, method, 1000, 1000,
                Bitmap.Config.ARGB_8888, ImageView.ScaleType.CENTER_INSIDE);
    }

    public Request<Bitmap> createBitmapRequest(String url, RequestMethod requestMethod, int maxWidth, int maxHeight,
                                               Bitmap.Config config, ImageView.ScaleType scaleType) {
        return new ImageRequest(url, requestMethod, maxWidth, maxHeight, config, scaleType);
    }

    public Request<byte[]> createByteArrayPostRequest(String url) {
        return createByteArrayRequest(url, RequestMethod.POST);
    }

    public Request<byte[]> createByteArrayGetRequest(String url) {
        return createByteArrayRequest(url, RequestMethod.GET);
    }

    public Request<byte[]> createByteArrayRequest(String url, RequestMethod requestMethod) {
        return new ByteArrayRequest(url, requestMethod);
    }

    public static DownloadRequest createDownloadRequest(String url, String fileFolder, boolean isDeleteOld) {
        return createDownloadRequest(url, RequestMethod.GET, fileFolder, isDeleteOld);
    }

    public static DownloadRequest createDownloadRequest(String url, RequestMethod requestMethod, String fileFolder,
                                                        boolean isDeleteOld) {
        return createDownloadRequest(url, requestMethod, fileFolder, true, isDeleteOld);
    }

    public static DownloadRequest createDownloadRequest(String url, RequestMethod requestMethod, String fileFolder,
                                                        boolean isRange, boolean isDeleteOld) {
        return new DownloadRequest(url, requestMethod, fileFolder, isRange, isDeleteOld);
    }


//    public <M> void doString(String url, int what, RequestMethod requestMethod, CallBack<String, M> listener) {
//        doString(url, what, requestMethod, null, null, listener, true, true);
//    }
//
//    public <M> void doString(String url, int what, RequestMethod requestMethod, Map<String, Object> params, CallBack<String, M> listener) {
//        doString(url, what, requestMethod, null, params, listener, true, true);
//    }
//
//    public <M> void doString(String url, int what, RequestMethod requestMethod, Map<String, Object> params, CallBack<String, M> listener,
//                             boolean canCancelEnable, boolean showLoadingDialogEnable) {
//        doString(url, what, requestMethod, null, params, listener, canCancelEnable, showLoadingDialogEnable);
//    }
//
//    public <M> void doString(String url, int what, RequestMethod requestMethod, Map<String, String> header,
//                             Map<String, Object> params, CallBack<String, M> listener) {
//        doString(url, what, requestMethod, header, params, listener, true, true);
//    }
//
//    public <M> void doString(String url, int what, RequestMethod requestMethod, Map<String, String> header, Map<String, Object> params,
//                             CallBack<String, M> listener, boolean canCancelEnable, boolean showLoadingDialogEnable) {
//        final Request<String> request = createStringRequest(url, requestMethod);
//        HttpHelper.addMapForRequest(request, header, params);
//        initRequestQueue().add(what, request,
//                new HttpResponseListener<String, M>(sContext, request, listener, canCancelEnable, showLoadingDialogEnable));
//    }
//
//    public <M> void doStringBody(String url, int what, RequestMethod requestMethod, CallBack<String, M> listener) {
//        doStringBody(url, what, requestMethod, null, null, listener, true, true);
//    }
//
//    public <M> void doStringBody(String url, int what, RequestMethod requestMethod, String params, CallBack<String, M> listener) {
//        doStringBody(url, what, requestMethod, null, params, listener, true, true);
//    }
//
//    public <M> void doStringBody(String url, int what, RequestMethod requestMethod, String params, CallBack<String, M> listener,
//                                 boolean canCancelEnable, boolean showLoadingDialogEnable) {
//        doStringBody(url, what, requestMethod, null, params, listener, canCancelEnable, showLoadingDialogEnable);
//    }
//
//    public <M> void doStringBody(String url, int what, RequestMethod requestMethod, Map<String, String> header,
//                                 String params, CallBack<String, M> listener) {
//        doStringBody(url, what, requestMethod, header, params, listener, true, true);
//    }
//
//    public <M> void doStringBody(String url, int what, RequestMethod requestMethod, Map<String, String> header, String params,
//                                 CallBack<String, M> listener, boolean canCancelEnable, boolean showLoadingDialogEnable) {
//        final Request<String> request = createStringRequest(url, requestMethod);
//        HttpHelper.addMapForRequest(request, header, null);
//        request.setDefineRequestBody(params, "application/json");
//        initRequestQueue().add(what, request,
//                new HttpResponseListener<String, M>(sContext, request, listener, canCancelEnable, showLoadingDialogEnable));
//    }


    public <M> void doJsonObject(String url, int what, RequestMethod requestMethod, CallBack<JSONObject> listener) {
        doJsonObject(url, what, requestMethod, null, null, listener, true, true);
    }

    public <M> void doJsonObject(String url, int what, RequestMethod requestMethod, Map<String, Object> params, CallBack<JSONObject> listener) {
        doJsonObject(url, what, requestMethod, null, params, listener, true, true);
    }

    public <M> void doJsonObject(String url, int what, RequestMethod requestMethod, Map<String, Object> params, CallBack<JSONObject> listener,
                                 boolean canCancelEnable, boolean showLoadingDialogEnable) {
        doJsonObject(url, what, requestMethod, null, params, listener, canCancelEnable, showLoadingDialogEnable);
    }

    public <M> void doJsonObject(String url, int what, RequestMethod requestMethod, Map<String, String> header,
                                 Map<String, Object> params, CallBack<JSONObject> listener) {
        doJsonObject(url, what, requestMethod, header, params, listener, true, true);
    }

    public <M> void doJsonObject(String url, int what, RequestMethod requestMethod, Map<String, String> header, Map<String, Object> params,
                                 CallBack<JSONObject> listener, boolean canCancelEnable, boolean showLoadingDialogEnable) {
        final Request<JSONObject> request = createJsonObjectRequest(url, requestMethod);
        HttpHelper.addMapForRequest(request, header, null);
        request.setDefineRequestBodyForJson(MapUtil.mapToJsonObject(params));
        initRequestQueue().add(what, request,
                new HttpResponseListener<>(sContext, request, listener, canCancelEnable, showLoadingDialogEnable));
    }


//    public <M> void doJsonArray(String url, int what, RequestMethod requestMethod, CallBack<JSONArray, M> listener) {
//        doJsonArray(url, what, requestMethod, null, null, listener, true, true);
//    }
//
//    public <M> void doJsonArray(String url, int what, RequestMethod requestMethod, Map<String, Object> params, CallBack<JSONArray, M> listener) {
//        doJsonArray(url, what, requestMethod, null, params, listener, true, true);
//    }
//
//    public <M> void doJsonArray(String url, int what, RequestMethod requestMethod, Map<String, Object> params, CallBack<JSONArray, M> listener,
//                                boolean canCancelEnable, boolean showLoadingDialogEnable) {
//        doJsonArray(url, what, requestMethod, null, params, listener, canCancelEnable, showLoadingDialogEnable);
//    }
//
//    public <M> void doJsonArray(String url, int what, RequestMethod requestMethod, Map<String, String> header,
//                                Map<String, Object> params, CallBack<JSONArray, M> listener) {
//        doJsonArray(url, what, requestMethod, header, params, listener, true, true);
//    }
//
//    public <M extends Class<M>> void doJsonArray(String url, int what, RequestMethod requestMethod, Map<String, String> header, Map<String, Object> params,
//                                CallBack<JSONArray> listener, boolean canCancelEnable, boolean showLoadingDialogEnable) {
//        final Request<JSONArray> request = createJsonArrayRequest(url, requestMethod);
//        HttpHelper.addMapForRequest(request, header, params);
//        initRequestQueue().add(what, request,
//                new HttpResponseListener<>(sContext, request, listener, canCancelEnable, showLoadingDialogEnable));
//    }
//
//
//    public void doBitmap(String url, int what, RequestMethod requestMethod, CallBack<Bitmap, Void> listener) {
//        doBitmap(url, what, requestMethod, null, null, listener, true, true);
//    }
//
//    public void doBitmap(String url, int what, RequestMethod requestMethod, Map<String, Object> params, CallBack<Bitmap, Void> listener) {
//        doBitmap(url, what, requestMethod, null, params, listener, true, true);
//    }
//
//    public void doBitmap(String url, int what, RequestMethod requestMethod, Map<String, Object> params, CallBack<Bitmap, Void> listener,
//                         boolean canCancelEnable, boolean showLoadingDialogEnable) {
//        doBitmap(url, what, requestMethod, null, params, listener, canCancelEnable, showLoadingDialogEnable);
//    }
//
//    public void doBitmap(String url, int what, RequestMethod requestMethod, Map<String, String> header,
//                         Map<String, Object> params, CallBack<Bitmap, Void> listener) {
//        doBitmap(url, what, requestMethod, header, params, listener, true, true);
//    }
//
//    //
//    public void doBitmap(String url, int what, RequestMethod requestMethod, Map<String, String> header, Map<String, Object> params,
//                         CallBack<Bitmap, Void> listener, boolean canCancelEnable, boolean showLoadingDialogEnable) {
//        final ImageRequest request = (ImageRequest) createBitmapRequest(url, requestMethod);
//        HttpHelper.addMapForRequest(request, header, params);
//        initRequestQueue().add(what, request,
//                new HttpResponseListener<>(sContext, request, listener, canCancelEnable, showLoadingDialogEnable));
//    }
//
//
//    public void doByteArray(String url, int what, RequestMethod requestMethod, CallBack<JSONObject, Void> listener) {
//        doByteArray(url, what, requestMethod, null, null, listener, true, true);
//    }
//
//    public void doByteArray(String url, int what, RequestMethod requestMethod, Map<String, Object> params, CallBack<JSONObject, Void> listener) {
//        doByteArray(url, what, requestMethod, null, params, listener, true, true);
//    }
//
//    public void doByteArray(String url, int what, RequestMethod requestMethod, Map<String, Object> params, CallBack<JSONObject, Void> listener,
//                            boolean canCancelEnable, boolean showLoadingDialogEnable) {
//        doByteArray(url, what, requestMethod, null, params, listener, canCancelEnable, showLoadingDialogEnable);
//    }
//
//    public void doByteArray(String url, int what, RequestMethod requestMethod, Map<String, String> header,
//                            Map<String, Object> params, CallBack<JSONObject, Void> listener) {
//        doByteArray(url, what, requestMethod, header, params, listener, true, true);
//    }
//
//    public void doByteArray(String url, int what, RequestMethod requestMethod, Map<String, String> header, Map<String, Object> params,
//                            CallBack<JSONObject, Void> listener, boolean canCancelEnable, boolean showLoadingDialogEnable) {
//        final Request<JSONObject> request = createJsonObjectRequest(url, requestMethod);
//        HttpHelper.addMapForRequest(request, header, params);
//        initRequestQueue().add(what, request,
//                new HttpResponseListener(sContext, request, listener, canCancelEnable, showLoadingDialogEnable));
//    }

    public void destroyAllTask() {
        if (mQueue != null) {
            mQueue.stop();
            mQueue.cancelAll();
        }
        if (mdQueue != null) {
            mdQueue.stop();
            mdQueue.cancelAll();
        }
    }
}
