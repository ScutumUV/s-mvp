package com.superchen.demo.url;

import android.support.annotation.NonNull;

import com.superchen.demo.fragment.libTest1.PublicKeyEntity;


/**
 * 传递参数：
 * 1、当请求方式使用Get时：需要使用 @Query标签 ，如果参数多的话可以用@QueryMap标签，接收一个Map
 * 2、当请求方式使用POST时，需要使用 @Field标签 或者 @Body（紧跟一个实体类）标签 或者 FieldMap标签
 */
public interface ApiService {

}
