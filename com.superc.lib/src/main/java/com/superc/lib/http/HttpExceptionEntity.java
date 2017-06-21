package com.superc.lib.http;

/**
 * Created by superchen on 2017/6/20.
 */
public class HttpExceptionEntity extends Exception {

    private String code;
    private String message;
    private String body = "";


    public String getCode() {
        return code;
    }

    public HttpExceptionEntity setCode(String code) {
        this.code = code;
        setMessageByCode();
        return this;
    }

    public String getMessage() {
        return message;
    }

    public HttpExceptionEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getBody() {
        return body;
    }

    public HttpExceptionEntity setBody(String body) {
        this.body = body;
        return this;
    }

    public void setMessageByCode() {
        if (code.equals("200")) {
            message = "加载成功";
        } else if (code.equals("404")) {
            message = "服务器走丢了，请稍后再试！";
        }
    }

    @Override
    public String toString() {
        return "HttpExceptionEntity{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
