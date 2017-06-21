package com.superchen.demo.url;

import android.support.annotation.NonNull;

import com.superc.lib.http.SApiServer;
import com.superchen.demo.fragment.libTest1.PublicKeyEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 传递参数：
 * 1、当请求方式使用Get时：需要使用 @Query标签 ，如果参数多的话可以用@QueryMap标签，接收一个Map
 * 2、当请求方式使用POST时，需要使用 @Field标签 或者 @Body（紧跟一个实体类）标签 或者 FieldMap标签
 */
public interface ApiService extends SApiServer {


    interface GetPublicKeyService {
        @POST(Url.GET_PUBLIC_KEY)
        Observable<PublicKeyEntity> getPublicKeyMethod();
    }

    interface Login {
        @FormUrlEncoded
        @POST(Url.LOGIN)
        Observable<Object> login(
                @Field("UserName") @NonNull String username,
                @Field("Password") @NonNull String password);
    }
}
