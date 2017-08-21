package com.superchen.demo.activity;

import android.net.Uri;

//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.drawable.ScalingUtils;
//import com.facebook.drawee.generic.GenericDraweeHierarchy;
//import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
//import com.facebook.drawee.interfaces.DraweeController;
//import com.facebook.drawee.view.SimpleDraweeView;
import com.superc.lib.ui.activity.SActivity;
import com.superchen.demo.R;

import java.util.List;

import butterknife.BindViews;

public class TestFrescoActivity extends SActivity {

//    @BindViews({R.id.my_image_view, R.id.my_image_gif_view, R.id.my_image_view2})
//    List<SimpleDraweeView> draweeViewLists;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_test_fresco;
    }

    @Override
    protected void initViews() {
//        showNormalImageView();
        showNormalGIFImageView();
//        showGenericDrawee();
    }

    private void showNormalImageView() {
        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality" +
                "=80&size=b9999_10000&sec=1497433405&di=7733d549553dddac3db5aa429d9a70a6&imgtype=" +
                "jpg&er=1&src=http%3A%2F%2Fd.5857.com%2Fxz_160627%2F001.jpg");
//        draweeViewLists.get(0).setImageURI(uri);
    }

    private void showNormalGIFImageView() {
        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&" +
                "size=b9999_10000&sec=1496838752580&di=d4b8570fe5efac54b72e2c12254a6ab5&imgtype=0&" +
                "src=http%3A%2F%2Fs7.rr.itc.cn%2Fg%2FwapChange%2F20156_20_7%2Fa76udy4138666869519.gif");
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setUri(uri)
//                .setAutoPlayAnimations(true)
//                .setTapToRetryEnabled(true)
//                .build();
//        draweeViewLists.get(1).setController(controller);
    }

//    private void showGenericDrawee() {
//        GenericDraweeHierarchyBuilder b = new GenericDraweeHierarchyBuilder(getResources());
//        GenericDraweeHierarchy hierarchy = b
//                .setFadeDuration(3000)
//                .setPlaceholderImage(R.mipmap.ic_default_image_holder, ScalingUtils.ScaleType.CENTER)
//                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER)
//                .setFailureImage(R.mipmap.ic_default_download_error, ScalingUtils.ScaleType.CENTER_INSIDE)
//                .setProgressBarImage(R.mipmap.ic_image_progress, ScalingUtils.ScaleType.CENTER_INSIDE)
//                .setRetryImage(R.mipmap.ic_image_refresh, ScalingUtils.ScaleType.CENTER_INSIDE)
//                .build();
//        draweeViewLists.get(2).setHierarchy(hierarchy);
//        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&" +
//                "size=b9999_10000&sec=1496838970642&di=65fecb11d507bdf70390ba660651e330&imgtype=0&" +
//                "src=http%3A%2F%2Fy1.ifengimg.com%2Fa%2F2014%2F1031%2F6e320604a9f61b9.gif");
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setUri(uri)
//                .setAutoPlayAnimations(true)
//                .build();
//        draweeViewLists.get(2).setController(controller);
//    }
}
