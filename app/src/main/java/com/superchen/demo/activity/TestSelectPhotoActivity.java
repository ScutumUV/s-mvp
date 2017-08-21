package com.superchen.demo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.superchen.demo.R;
import com.superchen.demo.activity.picture_select.ImageFloder;
import com.superchen.demo.activity.picture_select.adapter.GirdItemAdapter;
import com.superchen.demo.activity.picture_select.adapter.ImageFloderAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * 相册图片选择
 *
 * @author Administrator
 */
public class TestSelectPhotoActivity extends Activity {

    private GridView photoGrid;//图片列表

    private Button photoBtn;//底部下一步按钮

    private TextView titleName;//头部的标题

    private ImageView titleIcon;//头部的三角图标

    private ProgressDialog mProgressDialog;

    private HashSet<String> mDirPaths = new HashSet<String>();// 临时的辅助类，用于防止同一个文件夹的多次扫描

    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();// 扫描拿到所有的图片文件夹

    int totalCount = 0;

    private File mImgDir = new File("");// 图片数量最多的文件夹

    private int mPicsSize;//存储文件夹中的图片数量

    private List<String> mImgs = new ArrayList<String>();//所有的图片

    private int mScreenHeight;

    private LinearLayout dirLayout;

    private TextView quxiaoBtn;

    private ListView dirListView;

    private RelativeLayout headLayout;

    private int mScreenWidth;

    private ImageFloderAdapter floderAdapter;

    private GirdItemAdapter girdItemAdapter;

    private PopupWindow popupWindow;

    private View dirview;

    private static final int TAKE_CAMERA_PICTURE = 1000;//拍照

    private String path;

    private File dir;

    private String imagename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photo_select_view);
        GirdItemAdapter.mSelectedImage.clear();
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        mScreenWidth = outMetrics.widthPixels;
        setView();
        getImages();
        initEvent();
    }

    /**
     * 获取控件
     */
    private void setView() {
        photoGrid = (GridView) findViewById(R.id.gird_photo_list);
        photoBtn = (Button) findViewById(R
                .id.selected_photo_btn);
        titleName = (TextView) this.findViewById(R.id.selected_photo_name_text);
        quxiaoBtn = (TextView) findViewById(R.id.quxiao_btn);
        titleIcon = (ImageView) findViewById(R.id.selected_photo_icon);
        headLayout = (RelativeLayout) findViewById(R.id.selected_photo_header_layout);
        titleIcon.setBackgroundResource(R.mipmap.navigationbar_arrow_down);
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = TestSelectPhotoActivity.this.getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    if (parentFile.list() == null) continue;
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount += picSize;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressDialog.dismiss();
            setDataView();// 为View绑定数据
        }
    };


    /**
     * 为View绑定数据
     */
    public void setDataView() {
        path = Environment.getExternalStorageDirectory() + "/" + "test/photo/";
        dir = new File(path);
        if (!dir.exists()) dir.mkdirs();
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "一张图片没扫描到", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mImgDir.exists()) {
            mImgs = Arrays.asList(mImgDir.list());
        }


        girdItemAdapter = new GirdItemAdapter(this, mImgs, mImgDir.getAbsolutePath());
        photoGrid.setAdapter(girdItemAdapter);
        girdItemAdapter.setOnPhotoSelectedListener(new GirdItemAdapter.OnPhotoSelectedListener() {
            @Override
            public void takePhoto() {
                imagename = getTimeName(System.currentTimeMillis()) + ".jpg";
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(dir, imagename);//localTempImgDir和localTempImageFileName是自己定义的名字
                    Uri u = Uri.fromFile(f);
                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                    startActivityForResult(intent, TAKE_CAMERA_PICTURE);
                    Log.e("888888", "-------------0-------------------");
                } else {
                    Log.e("888888", "------------请插入SD卡-------------------");
                    Toast.makeText(TestSelectPhotoActivity.this, "请插入SD卡", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void photoClick(List<String> number) {
                photoBtn.setText("下一步(" + number.size() + "张)");
            }
        });
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        if (popupWindow == null) {
            dirview = LayoutInflater.from(TestSelectPhotoActivity.this).inflate(R.layout.image_select_dirlist, null);
            dirListView = (ListView) dirview.findViewById(R.id.id_list_dirs);
            popupWindow = new PopupWindow(dirview, LayoutParams.MATCH_PARENT, mScreenHeight * 3 / 5);
            floderAdapter = new ImageFloderAdapter(TestSelectPhotoActivity.this, mImageFloders);
            dirListView.setAdapter(floderAdapter);
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        titleIcon.setBackgroundResource(R.mipmap.navigationbar_arrow_up);
        popupWindow.showAsDropDown(headLayout, 0, 0);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                titleIcon.setBackgroundResource(R.mipmap.navigationbar_arrow_down);
            }
        });
        dirListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                titleName.setText(mImageFloders.get(position).getName());
                mImgDir = new File(mImageFloders.get(position).getDir());
                mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".png")
                                || filename.endsWith(".jpeg"))
                            return true;
                        return false;
                    }
                }));
                for (int i = 0; i < mImageFloders.size(); i++) {
                    mImageFloders.get(i).setSelected(false);
                }
                mImageFloders.get(position).setSelected(true);
                floderAdapter.changeData(mImageFloders);
                girdItemAdapter.changeData(mImgs, mImageFloders.get(position).getDir());
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });

    }

    /**
     * 监听事件
     */
    private void initEvent() {
        quxiaoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        titleName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 初始化展示文件夹的popupWindw
                initListDirPopupWindw();
            }
        });
        photoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestSelectPhotoActivity.this, GirdItemAdapter.mSelectedImage.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String getTimeName(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(time);
        return formatter.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("888888", "-------------1-------------------");
        switch (requestCode) {
            case TAKE_CAMERA_PICTURE:
                Log.e("888888", "-------------2-------------------");
                Toast.makeText(this, path + imagename, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
