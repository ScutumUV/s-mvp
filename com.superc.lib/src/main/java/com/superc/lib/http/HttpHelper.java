package com.superc.lib.http;

import android.support.annotation.NonNull;

import com.superc.lib.util.StringUtils;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.Map;

/**
 * Created by owner on 2017/8/25.
 */
public class HttpHelper {

    public static <T> void addMapForRequest(@NonNull Request<T> request, Map<String, String> header, Map<String, Object> params) {
        if (header != null && header.size() > 0) {
            addMapDetail(request, header, 0);
        }
        if (params != null && params.size() > 0) {
            addMapDetail(request, params, 1);
        }
    }

    public static <T, K, V> void addMapDetail(@NonNull Request<T> request, Map<K, V> map, int type) {
        for (Map.Entry<K, V> e : map.entrySet()) {
            if (!StringUtils.isNull(e.getKey() + "") && !StringUtils.isNull(e.getValue() + "")) {
                if (type == 0) {
                    request.addHeader(e.getKey() + "", e.getValue() + "");
                } else if (type == 1) {
                    request.add(e.getKey() + "", e.getValue() + "");
                }
            }
        }
    }
}
