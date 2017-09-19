package com.superc.lib.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.superc.lib.R;
import com.superc.lib.dialog.WaitDialog;

/**
 * 对话框工具类, 提供常用对话框显示, 使用support.v7包内的AlertDialog样式
 */
public class DialogUtil {

    public static WaitDialog createProgressDialog(Context context) {
        return createProgressDialog(context, null, true, true);
    }

    public static WaitDialog createProgressDialog(@NonNull Context context, String loadingReminderText,
                                                  boolean cancelEnable, boolean cancelTouchOutsideEnable) {
        WaitDialog dialog = new WaitDialog(context);
        dialog.setMessage(StringUtils.isEmpty(loadingReminderText) ?
                "加载中..." : loadingReminderText);
        dialog.setCancelable(cancelEnable);
        dialog.setCanceledOnTouchOutside(cancelTouchOutsideEnable);
        return dialog;
    }

    public static Dialog showCommonDialog(@NonNull Context context, String title, String message,
                                          @NonNull DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.dialog_positive), listener)
                .setNegativeButton(context.getString(R.string.dialog_negative), listener)
                .show();
    }

    public static Dialog showCustomDialog(@NonNull Context context, String title, String message,
                                          String positiveButtonText, String negativeButtonText,
                                          boolean cancelEnable,
                                          DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setTitle(StringUtils.isEmpty(title) ? context.getString(R.string.warm_reminder) : title)
                .setMessage(message)
                .setCancelable(cancelEnable)
                .setPositiveButton(StringUtils.isEmpty(positiveButtonText) ?
                        context.getString(R.string.dialog_positive) : positiveButtonText, listener)
                .setNegativeButton(StringUtils.isEmpty(negativeButtonText) ?
                        context.getString(R.string.dialog_negative) : negativeButtonText, listener)
                .show();
    }

    public static Dialog showConfirmDialog(@NonNull Context context, String message,
                                           DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.dialog_positive), listener)
                .show();
    }

}
