package com.superc.lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by superchen on 2017/8/2.
 */
public class PermissionUtil {

    public static int[] checkSelfPermissions(@NonNull Context context, @NonNull String... permissions) {
        int[] result = new int[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            result[i] = ContextCompat.checkSelfPermission(context, permissions[i]);
        }
        return result;
    }

    public static boolean checkGroupPermissionsGroupStatus(@NonNull Context context, @NonNull String permission) {
        return checkPermissionsGroupStatus(checkSelfPermissions(context, permission));
    }

    public static boolean checkGroupPermissionsGroupStatus(@NonNull Context context, @NonNull String... permission) {
        return checkPermissionsGroupStatus(checkSelfPermissions(context, permission));
    }

    public static boolean checkPermissionStatus(int result) {
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermissionsGroupStatus(@NonNull int[] result) {
        boolean tag = true;
        for (Integer k : result) {
            if (!checkPermissionStatus(k)) {
                tag = false;
                break;
            }
        }
        return tag;
    }

    public static void requestPermissions(@NonNull Activity activity, final @IntRange(from = 0) int requestCode,
                                          final @NonNull String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static void checkPermissionsAndAutoRequestPermissions(@NonNull Activity activity, final @IntRange(from = 0) int requestCode,
                                                                 final @NonNull String... permissions) {
        if (!checkGroupPermissionsGroupStatus(activity, permissions)) {
            requestPermissions(activity, requestCode, permissions);
        }
    }
}
