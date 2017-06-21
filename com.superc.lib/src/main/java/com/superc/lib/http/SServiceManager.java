package com.superc.lib.http;


import android.content.Context;
import android.support.annotation.NonNull;

import com.superc.lib.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.BaseUrl;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * RetrofitService管理类
 * <p>
 * create by SuperChen    2017/06/15
 * <p>
 * 暂无法实现在使用过程中更换BaseUrl
 */
public class SServiceManager {

    /**
     * 服务器地址
     */
    private static String HOST_URL = "http://116.62.19.252:81";
    /**
     *
     */
    private static Retrofit mRetrofit;


    private SServiceManager() {
    }

    public static class Builder {

        private long TIMEOUT_CONNECT = 20;
        private long TIMEOUT_WRITE = 15;
        private long TIMEOUT_READ = 15;
        private TimeUnit mTimeUnit = TimeUnit.SECONDS;
        private long cacheMaxSize = 1024 * 1024 * 50;
        private File mCacheFile;

        private HashMap<String, String> mHeaderMapFields;
        private Cache mCache;

        private List<Interceptor> mInterceptorList;
        private List<Executor> mExecutorList;
        private List<Converter.Factory> mConverterFactoryList;
        private List<CallAdapter.Factory> mCallAdapterFactoryList;

        public Builder(@NonNull Context context) {
            mCacheFile = new File(context.getCacheDir().getAbsolutePath(), "cache");
            mCache = new Cache(mCacheFile, cacheMaxSize);
        }

        public Builder setHostUrl(@NonNull String host) {
            if (StringUtils.isEmpty(host)) {
                throw new NullPointerException("The host url not be null");
            }
            HOST_URL = host;
            return this;
        }

        public Builder setHeaderMapFields(HashMap<String, String> mHeaderMapFields) {
            this.mHeaderMapFields = mHeaderMapFields;
            return this;
        }

        public Builder setTimeoutConnect(long timeCount) {
            this.TIMEOUT_CONNECT = timeCount;
            return this;
        }

        public Builder setTimeoutWrite(long timeCount) {
            this.TIMEOUT_WRITE = timeCount;
            return this;
        }

        public Builder setTimeoutRead(long timeCount) {
            this.TIMEOUT_READ = timeCount;
            return this;
        }

        public Builder setTimeoutUnit(@NonNull TimeUnit timeUnit) {
            this.mTimeUnit = timeUnit;
            return this;
        }

        public Builder setMaxCacheSize(long maxSize) {
            this.cacheMaxSize = maxSize;
            mCache = new Cache(mCacheFile, cacheMaxSize);
            return this;
        }

        public Builder setCacheFile(@NonNull File cacheFile) {
            this.mCacheFile = cacheFile;
            mCache = new Cache(mCacheFile, cacheMaxSize);
            return this;
        }

        public Builder setInterceptors(List<Interceptor> list) {
            this.mInterceptorList = list;
            return this;
        }

        public Builder setExcutors(List<Executor> list) {
            this.mExecutorList = list;
            return this;
        }

        public Builder setConverterFactories(List<Converter.Factory> list) {
            this.mConverterFactoryList = list;
            return this;
        }

        public Builder setCallAdapterFactories(List<CallAdapter.Factory> list) {
            this.mCallAdapterFactoryList = list;
            return this;
        }

        private Retrofit getRetrofit() {
            if (mRetrofit == null) {

                mRetrofit = createNewRetrofit();

            } else {
                BaseUrl b = mRetrofit.baseUrl();
                if (!b.url().url().toString().equals(HOST_URL)) {
                    mRetrofit = createNewRetrofit();
                }
            }
            return mRetrofit;
        }

        private Retrofit createNewRetrofit() {
            OkHttpClient client = getOkHttpClient();

            Retrofit.Builder rBuilder = new Retrofit.Builder();
            rBuilder.client(client)
                    .baseUrl(HOST_URL);
            addConverterFactories(rBuilder);
            addCallAdapterFactories(rBuilder);
            addExcutors(rBuilder);

            return rBuilder.build();
        }


        public SServiceManager build() {
            getRetrofit();
            return SingleHolder.mInstance;
        }

        private OkHttpClient getOkHttpClient() {
            OkHttpClient.Builder hBuilder = new OkHttpClient.Builder();
            hBuilder.connectTimeout(TIMEOUT_CONNECT, mTimeUnit);
            hBuilder.writeTimeout(TIMEOUT_WRITE, mTimeUnit);
            hBuilder.readTimeout(TIMEOUT_READ, mTimeUnit);
            // 设置 请求的缓存
            hBuilder.cache(mCache);

            if (mInterceptorList != null && mInterceptorList.size() > 0) {
                for (Interceptor i : mInterceptorList) {
                    hBuilder.addInterceptor(i);
                }
            } else {
                // Log拦截器  打印所有的Log
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                hBuilder.addInterceptor(logInterceptor);
            }
            addHeader();

            return hBuilder.build();
        }

        private Interceptor addHeader() {
            Interceptor requestInterceptor = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request.Builder b = chain.request().newBuilder();
                    if (mHeaderMapFields != null && mHeaderMapFields.size() > 0) {
                        for (Map.Entry<String, String> params : mHeaderMapFields.entrySet()) {
                            b.addHeader(params.getKey(), params.getValue());
                        }
                    } else {
                        b
                                .addHeader("OS", "Android")
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Content-Encoding", "deflate")
                                .addHeader("Accept-Encoding", "UTF-8");
                    }
                    Request request = b.build();
                    return chain.proceed(request);
                }
            };
            return requestInterceptor;
        }

        private void addConverterFactories(@NonNull Retrofit.Builder builder) {
            if (!checkListEmpty(mConverterFactoryList)) {
                for (Converter.Factory f : mConverterFactoryList) {
                    builder.addConverterFactory(f);
                }
            } else {
                // 添加转换器Converter(将json 转为JavaBean)
                builder.addConverterFactory(GsonConverterFactory.create());
            }
        }

        private void addCallAdapterFactories(@NonNull Retrofit.Builder builder) {
            if (!checkListEmpty(mCallAdapterFactoryList)) {
                for (CallAdapter.Factory f : mCallAdapterFactoryList) {
                    builder.addCallAdapterFactory(f);
                }
            } else {
                // 配合RxJava 使用
                builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            }
        }

        private void addExcutors(@NonNull Retrofit.Builder builder) {
            if (!checkListEmpty(mExecutorList)) {
                for (Executor e : mExecutorList) {
                    builder.callbackExecutor(e);
                }
            }
        }
    }

    public void resetBaseUrl(@NonNull Context context, String baseUrl) {
        Builder b = new Builder(context);
        b.setHostUrl(baseUrl);
        b.build();
    }

    public SServiceManager getManager(){
        return this;
    }

    public <T> T create(Class<T> serviceInterface) {
        return mRetrofit.create(serviceInterface);
    }

    /**
     * 对 Observable<T> 做统一的处理，处理了线程调度、分割返回结果等操作组合了起来
     *
     * @param responseObservable
     * @param <T>
     * @return
     */
    protected <T> Observable<T> applySchedulers(Observable<T> responseObservable) {
        return null;
    }


    private static class SingleHolder {

        private static final SServiceManager mInstance = new SServiceManager();
    }

    public static <T> boolean checkListEmpty(List<T> l) {
        if (l == null || l.size() == 0) {
            return true;
        }
        return false;
    }

}
