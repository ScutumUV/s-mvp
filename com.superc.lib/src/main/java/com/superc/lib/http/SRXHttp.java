package com.superc.lib.http;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.superc.lib.constants.Constants;
import com.superc.lib.http.cache.SCache;
import com.superc.lib.http.interceptor.HttpLogInterceptor;
import com.superc.lib.util.LogUtils;
import com.superc.lib.util.SUtil;
import com.superc.lib.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * RetrofitService管理类
 * <p>
 * create by SuperChen    2017/06/15
 * <p>
 * 暂无法实现在使用过程中更换BaseUrl
 */
public class SRXHttp implements SHttpService {

    private int mRetryCount = Constants.HttpConstants.DEFAULT_RETRY_COUNT;
    private int mRetryDelay = Constants.HttpConstants.DEFAULT_RETRY_DELAY;
    private int mRetryIncreaseDelay = Constants.HttpConstants.DEFAULT_RETRY_DELAY;
    private TimeUnit mTimeUnit = Constants.HttpConstants.DEFAULT_TIME_UNIT;
    private String mBaseUrl = Constants.HttpConstants.HOST_URL;

    private String mCacheDirectory = Constants.FileConstants.CACHE_File_DIR;
    private Cache mCache = null;
    private SCache sCache;
    private Retrofit.Builder bRetrofit;
    private OkHttpClient.Builder bOKHttp;
    private List<Interceptor> interceptorList;
    private List<Executor> mExecutorList;
    private List<Converter.Factory> mConverterFactoryList;
    private List<CallAdapter.Factory> mCallAdapterFactoryList;
    private SHttpService httpService;

    private static Retrofit mRetrofit;
    private static Application sContext;
    /**
     * 使用CompositeSubscription来持有所有的Subscriptions
     */
    private static CompositeSubscription mCompositeSubscription;

    private SRXHttp() {
        initOkHttp();
        initRetrofit();
    }

    private void initOkHttp() {
        bOKHttp = new OkHttpClient.Builder();
        bOKHttp
                .connectTimeout(Constants.HttpConstants.DEFAULT_TIMEOUT_CONNECT, Constants.HttpConstants.DEFAULT_TIME_UNIT)
                .writeTimeout(Constants.HttpConstants.DEFAULT_TIMEOUT_WRITE, Constants.HttpConstants.DEFAULT_TIME_UNIT)
                .readTimeout(Constants.HttpConstants.DEFAULT_TIMEOUT_READ, Constants.HttpConstants.DEFAULT_TIME_UNIT);
    }

    private void initRetrofit() {
        bRetrofit = new Retrofit.Builder();
    }

    public static void init(Application app) {
        sContext = app;
    }

    public static SRXHttp getInstance() {
        return SingTon.singleton;
    }

    public SRXHttp setRetryCount(int count) {
        mRetryCount = count;
        return this;
    }

    public SRXHttp setRetryDelay(int mRetryDelay) {
        this.mRetryDelay = mRetryDelay;
        return this;
    }

    public SRXHttp setBaseUrl(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
        return this;
    }

    public SRXHttp setCacheDirectory(String mCacheDirectory) {
        this.mCacheDirectory = mCacheDirectory;
        return this;
    }

    public SRXHttp setRetrofitBuilder(Retrofit.Builder bRetrofit) {
        this.bRetrofit = bRetrofit;
        return this;
    }

    public SRXHttp setOKHttpBuilder(OkHttpClient.Builder bOKHttp) {
        this.bOKHttp = bOKHttp;
        return this;
    }

    public SRXHttp setInterceptorList(List<Interceptor> interceptorList) {
        this.interceptorList = interceptorList;
        return this;
    }

    public SRXHttp addInterceptor(Interceptor interceptor) {
        this.interceptorList.add(interceptor);
        return this;
    }

    public SRXHttp setExecutorList(List<Executor> mExecutorList) {
        this.mExecutorList = mExecutorList;
        return this;
    }

    public SRXHttp addExecutor(Executor executor) {
        this.mExecutorList.add(executor);
        return this;
    }

    public SRXHttp setConverterFactoryList(List<Converter.Factory> mConverterFactoryList) {
        this.mConverterFactoryList = mConverterFactoryList;
        return this;
    }

    public SRXHttp addConverterFactory(Converter.Factory f) {
        this.mConverterFactoryList.add(f);
        return this;
    }

    public SRXHttp setCallAdapterFactoryList(List<CallAdapter.Factory> mCallAdapterFactoryList) {
        this.mCallAdapterFactoryList = mCallAdapterFactoryList;
        return this;
    }

    public SRXHttp addCallAdapterFactory(CallAdapter.Factory f) {
        this.mCallAdapterFactoryList.add(f);
        return this;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    public int getRetryDelay() {
        return mRetryDelay;
    }

    public int getRetryIncreaseDelay() {
        return mRetryIncreaseDelay;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public String getCacheDirectory() {
        return mCacheDirectory;
    }

    public Retrofit.Builder getbRetrofit() {
        return bRetrofit;
    }

    public OkHttpClient.Builder getbOKHttp() {
        return bOKHttp;
    }

    public List<Interceptor> getInterceptorList() {
        return interceptorList;
    }

    public List<Executor> getExecutorList() {
        return mExecutorList;
    }

    public List<Converter.Factory> getConverterFactoryList() {
        return mConverterFactoryList;
    }

    public List<CallAdapter.Factory> getCallAdapterFactoryList() {
        return mCallAdapterFactoryList;
    }

    public SRXHttp setDebug(String tag, boolean isPrintException) {
        if (isPrintException) {
            SUtil.checkNull(this, "please call SRXHttp.getInstance() first");
            // Log拦截器  打印所有的Log
            HttpLogInterceptor logInterceptor = new HttpLogInterceptor(tag, isPrintException);
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            bOKHttp.addInterceptor(logInterceptor);
        }
        if (!StringUtils.isEmpty(tag))
            LogUtils.customTagPrefix = "SRXHttp";
        LogUtils.allowE = isPrintException;
        LogUtils.allowD = isPrintException;
        LogUtils.allowI = isPrintException;
        LogUtils.allowV = isPrintException;
        return this;
    }

//    public static class Builder {
//
//        public Builder() {
//            SUtil.checkNull(sContext, "Please init first");
//            mCacheFile = new File(sContext.getCacheDir().getAbsolutePath(), "cache");
//            mCache = new Cache(mCacheFile, cacheMaxSize);
//        }
//
//        public Builder setHostUrl(@NonNull String host) {
//            if (StringUtils.isEmpty(host)) {
//                throw new NullPointerException("The host url not be null");
//            }
//            HOST_URL = host;
//            return this;
//        }
//
//        public Builder setHeaderMapFields(HashMap<String, String> mHeaderMapFields) {
//            this.mHeaderMapFields = mHeaderMapFields;
//            return this;
//        }
//
//        public Builder setTimeoutConnect(long timeCount) {
//            this.TIMEOUT_CONNECT = timeCount;
//            return this;
//        }
//
//        public Builder setTimeoutWrite(long timeCount) {
//            this.TIMEOUT_WRITE = timeCount;
//            return this;
//        }
//
//        public Builder setTimeoutRead(long timeCount) {
//            this.TIMEOUT_READ = timeCount;
//            return this;
//        }
//
//        public Builder setTimeoutUnit(@NonNull TimeUnit timeUnit) {
//            this.mTimeUnit = timeUnit;
//            return this;
//        }
//
//        public Builder setMaxCacheSize(long maxSize) {
//            this.cacheMaxSize = maxSize;
//            mCache = new Cache(mCacheFile, cacheMaxSize);
//            return this;
//        }
//
//        public Builder setCacheFile(@NonNull File cacheFile) {
//            this.mCacheFile = cacheFile;
//            mCache = new Cache(mCacheFile, cacheMaxSize);
//            return this;
//        }
//
//        public Builder setInterceptors(List<Interceptor> list) {
//            this.mInterceptorList = list;
//            return this;
//        }
//
//        public Builder setExcutors(List<Executor> list) {
//            this.mExecutorList = list;
//            return this;
//        }
//
//        public Builder setConverterFactories(List<Converter.Factory> list) {
//            this.mConverterFactoryList = list;
//            return this;
//        }
//
//        public Builder setCallAdapterFactories(List<CallAdapter.Factory> list) {
//            this.mCallAdapterFactoryList = list;
//            return this;
//        }
//
//        private Retrofit getRetrofit() {
//            if (mRetrofit == null) {
//
//                mRetrofit = createNewRetrofit();
//
//            } else {
//                BaseUrl b = mRetrofit.baseUrl();
//                if (!b.url().toString().equals(HOST_URL)) {
//                    mRetrofit = createNewRetrofit();
//                }
//            }
//            return mRetrofit;
//        }
//
//        private Retrofit createNewRetrofit() {
//            OkHttpClient client = getOkHttpClient();
//
//            Retrofit.Builder rBuilder = new Retrofit.Builder();
//            rBuilder.client(client)
//                    .baseUrl(HOST_URL);
//            addConverterFactories(rBuilder);
//            addCallAdapterFactories(rBuilder);
//            addExcutors(rBuilder);
//
//            return rBuilder.build();
//        }
//
//        public SRXHttp build() {
//            getRetrofit();
//            mCompositeSubscription = new CompositeSubscription();
//            return SingTon.singleton;
//        }
//
//        private OkHttpClient getOkHttpClient() {
//            OkHttpClient.Builder hBuilder = new OkHttpClient.Builder();
//            hBuilder.connectTimeout(TIMEOUT_CONNECT, mTimeUnit);
//            hBuilder.writeTimeout(TIMEOUT_WRITE, mTimeUnit);
//            hBuilder.readTimeout(TIMEOUT_READ, mTimeUnit);
//            // 设置 请求的缓存
//            hBuilder.cache(mCache);
//
//            if (mInterceptorList != null && mInterceptorList.size() > 0) {
//                for (Interceptor i : mInterceptorList) {
//                    hBuilder.addInterceptor(i);
//                }
//            } else {
//                // Log拦截器  打印所有的Log
//                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
//                logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                hBuilder.addInterceptor(logInterceptor);
//            }
//            addHeader();
//
//            return hBuilder.build();
//        }
//
//        private Interceptor addHeader() {
//            Interceptor requestInterceptor = new Interceptor() {
//                @Override
//                public okhttp3.Response intercept(Chain chain) throws IOException {
//                    Request.Builder b = chain.request().newBuilder();
//                    if (mHeaderMapFields != null && mHeaderMapFields.size() > 0) {
//                        for (Map.Entry<String, String> params : mHeaderMapFields.entrySet()) {
//                            b.addHeader(params.getKey(), params.getValue());
//                        }
//                    } else {
//                        b
//                                .addHeader("OS", "Android")
//                                .addHeader("Content-Type", "application/json")
//                                .addHeader("Content-Encoding", "deflate")
//                                .addHeader("Accept-Encoding", "UTF-8");
//                    }
//                    Request request = b.build();
//                    return chain.proceed(request);
//                }
//            };
//            return requestInterceptor;
//        }
//
//        private void addConverterFactories(@NonNull Retrofit.Builder builder) {
//            if (!SUtil.checkListEmpty(mConverterFactoryList)) {
//                for (Converter.Factory f : mConverterFactoryList) {
//                    builder.addConverterFactory(f);
//                }
//            } else {
//                // 添加转换器Converter(将json 转为JavaBean)
//                builder.addConverterFactory(GsonConverterFactory.create());
//            }
//        }
//
//        private void addCallAdapterFactories(@NonNull Retrofit.Builder builder) {
//            if (!SUtil.checkListEmpty(mCallAdapterFactoryList)) {
//                for (CallAdapter.Factory f : mCallAdapterFactoryList) {
//                    builder.addCallAdapterFactory(f);
//                }
//            } else {
//                // 配合RxJava 使用
//                builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
//            }
//        }
//
//        private void addExcutors(@NonNull Retrofit.Builder builder) {
//            if (!SUtil.checkListEmpty(mExecutorList)) {
//                for (Executor e : mExecutorList) {
//                    builder.callbackExecutor(e);
//                }
//            }
//        }
//    }

    public void resetBaseUrl(String baseUrl) {
//        Builder b = new Builder();
//        b.setHostUrl(baseUrl);
//        b.build();
    }

    @Override
    public <T> T create(@NonNull Class<T> serviceInterface) {
        return mRetrofit.create(serviceInterface);
    }

    @Override
    public <T> Observable<T> applySchedulers(@NonNull Observable<T> responseObservable) {
        return responseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<T, Observable<T>>() {
                    @Override
                    public Observable<T> call(T tResponse) {
                        return flatResponse(tResponse);
                    }
                });
    }

    @Override
    public <T> Observable<T> flatResponse(@NonNull final T response) {
        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (response != null) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(response);
                    }
                } else {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(new HttpExceptionEntity().setCode("404").setMessage("网络错误，请检查网络设置"));
                    }
                    return;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public <T> Subscriber createNewSubscriber(final SubsCallBack callBack) {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                callBack.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (e != null) {
                    if (callBack.onError(e) != null && callBack.onError(e)) {
                        if (e instanceof HttpException) {
                            ResponseBody body = ((HttpException) e).response().errorBody();
                            try {
                                String json = body.string();
                                Gson gson = new Gson();
                                HttpExceptionEntity he = gson.fromJson(json, HttpExceptionEntity.class);
                                if (he != null && he.getMessage() != null) {
                                    callBack.onError(he);
                                }
                            } catch (IOException IOe) {
                                IOe.printStackTrace();
                            }
                        } else {
                            callBack.onError(e);
                        }
                    } else {
                        callBack.onError(e);
                    }
                } else {
                    callBack.onError("网络错误，请检查网络设置");
                }
            }

            @Override
            public void onNext(T e) {
                SUtil.checkNull(mCompositeSubscription, "The CompositeSubscription is null");
                if (!mCompositeSubscription.isUnsubscribed()) {
                    callBack.onNext(e);
                }
            }
        };
    }

    @Override
    public void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    private static final class SingTon {
        private static final SRXHttp singleton = new SRXHttp();
    }

}
