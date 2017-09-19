package com.superc.lib.http;

/**
 * Created by owner on 2017/8/25.
 */
public class ApiModel<T> {

    private int code = -9999;
    private String message;
    private T value;
    private String date;
    private String extraValue;
    private boolean isSuccessed = false;

    public ApiModel() {
    }

    public ApiModel(int code, String message, T value, String date, String extraValue, boolean isSuccessed) {
        this.code = code;
        this.message = message;
        this.value = value;
        this.date = date;
        this.extraValue = extraValue;
        this.isSuccessed = isSuccessed;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        setSuccessed(code == 0);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public T getData() {
        return value;
    }

    public void setData(T data) {
        this.value = data;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }

    public void setSuccessed(boolean successed) {
        isSuccessed = successed;
    }

    public boolean isSuccessed() {
        return isSuccessed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ApiModel{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", value=" + value +
                ", date='" + date + '\'' +
                ", extraValue='" + extraValue + '\'' +
                '}';
    }
}
