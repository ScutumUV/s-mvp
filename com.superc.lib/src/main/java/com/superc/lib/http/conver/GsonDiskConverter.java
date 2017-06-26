package com.superc.lib.http.conver;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.superc.lib.util.LogUtils;
import com.superc.lib.util.SUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * <p>描述：GSON-数据转换器</p>
 * 1.GSON-数据转换器其实就是存储字符串的操作<br>
 * 2.如果你的Gson有特殊处理，可以自己创建一个，否则用默认。<br>
 * <p>
 * 优点：<br>
 * 相对于SerializableDiskConverter转换器，存储的对象不需要进行序列化（Serializable），<br>
 * 特别是一个对象中又包含很多其它对象，每个对象都需要Serializable，比较麻烦<br>
 * </p>
 * <p>
 * <p>
 * 缺点：<br>
 * 就是存储和读取都要使用Gson进行转换，object->String->Object的给一个过程，相对来说<br>
 * 每次都要转换性能略低，但是以现在的手机性能可以忽略不计了。<br>
 * </p>
 * <p>
 * 《-------骚年，自己根据实际需求选择吧！！！------------》<br>
 * 《--看到这里，顺便提下知道IDiskConverter的好处了吧，面向接口编程是不是很灵活(*^_^*)----------》<br>
 * <p>
 * 作者： zhouyou<br>
 * 日期： 2016/12/24 17:35<br>
 * 版本： v2.0<br>
 */
public class GsonDiskConverter implements SDiskConverter {
    private Gson gson;

    public GsonDiskConverter() {
        this.gson = new Gson();
    }

    public GsonDiskConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public <T> T load(InputStream source, Type type) {
        T value = null;
        try {
            if (gson != null) gson = new Gson();
            value = gson.fromJson(new InputStreamReader(source), type);
        } catch (JsonIOException e) {
            LogUtils.e(e.getMessage());
        } catch (JsonSyntaxException e) {
            LogUtils.e(e.getMessage());
        } finally {
            SUtil.close(source);
        }
        return value;
    }

    @Override
    public boolean writer(OutputStream sink, Object data) {
        try {
            String json = gson.toJson(data);
            byte[] bytes = json.getBytes();
            sink.write(bytes, 0, bytes.length);
            sink.flush();
            return true;
        } catch (JsonIOException e) {
            LogUtils.e(e.getMessage());
        } catch (JsonSyntaxException e) {
            LogUtils.e(e.getMessage());
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
        }
        return false;
    }
}
