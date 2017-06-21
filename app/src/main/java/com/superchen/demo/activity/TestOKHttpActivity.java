package com.superchen.demo.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.superc.lib.ui.activity.SActivity;
import com.superchen.demo.R;
import com.superchen.demo.url.Url;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Callback;

public class TestOKHttpActivity extends SActivity {

    @BindViews({R.id.okhttp_btn_get, R.id.okhttp_btn_post})
    List<Button> btnList;

    @BindView(R.id.okhttp_cb_get_type)
    CheckBox cbGetType;

    @BindView(R.id.okhttp_iv_get_method)
    ImageView ivGetMehod;

    private int typeGet = 0;

    private String url1 = "http://pic1.win4000.com/wallpaper/a/53a79d791a52f.jpg";
    private String url2 = "http://4493bz.1985t.com/uploads/allimg/140924/4-140924115G1.jpg";


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_test_okhttp;
    }

    @Override
    protected void initViews() {
        cbGetType.setChecked(true);
    }

    @OnClick({R.id.okhttp_btn_get, R.id.okhttp_btn_post})
    void login(View view) {
        switch (view.getId()) {
            case R.id.okhttp_btn_get:
                okGet(typeGet);
                break;
            case R.id.okhttp_btn_post:
                okPost();
                break;
        }
    }

    @OnCheckedChanged({R.id.okhttp_cb_get_type})
    void check(CompoundButton v, boolean isChecked) {
        switch (v.getId()) {
            case R.id.okhttp_cb_get_type:
                if (isChecked) {
                    typeGet = 0;
                } else {
                    typeGet = 1;
                }
                break;
        }
    }

    private void okGet(int type) {
        String url;
        if (type == 0) {
            url = url1;
        } else {
            url = url2;
        }
        final OkHttpClient c = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();

        if (type == 0) {
            /* 同步请求 */
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Response response = c.newCall(request).execute();
                        Message m = Message.obtain();
                        if (response.isSuccessful()) {
                            InputStream is = response.body().byteStream();
                            if (is == null) return;
                            Bitmap b = BitmapFactory.decodeStream(is);
                            m.what = 0;
                            m.obj = b;
                            handler.sendMessage(m);
                        } else {
                            onfailure(response.message());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            /* 异步请求 */
            Call call = c.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    onfailure(e.toString());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    InputStream is = response.body().byteStream();
                    if (is == null) {
                        return;
                    }
                    Bitmap b = BitmapFactory.decodeStream(is);
                    Message m = Message.obtain();
                    m.what = 0;
                    m.obj = b;
                    handler.sendMessage(m);
                }
            });
        }
    }

    private void okPost() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
//                .add("", "")
                .build();
        Request request = new Request.Builder()
                .url(Url.GET_PUBLIC_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        Call c = client.newCall(request);
        c.enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                onfailure(e.toString());
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                Log.i("msgg", "response :" + response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            showToastShort(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void onfailure(String msg) {
        Message m = Message.obtain();
        m.what = -1;
        m.obj = msg;
        handler.sendMessage(m);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj == null) {
                        return;
                    }
                    Bitmap bitmap = (Bitmap) msg.obj;
                    ivGetMehod.setImageBitmap(bitmap);
                    break;
                case -1:
                    Object o = msg.obj;
                    showToastShort("network error :" + o.toString());
                    break;
            }
        }
    };

    @Override
    public Context getContext() {
        return this;
    }
}
