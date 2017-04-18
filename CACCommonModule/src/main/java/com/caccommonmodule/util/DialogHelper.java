package com.caccommonmodule.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

/**
 * Created by ac on 2017/1/13.
 */

public class DialogHelper {

    private ProgressDialogWrapper progressDialogWrapper;
    private AlertDialogWrapper alertDialogWrapper;
    private Context context;
    private DismissListener dismissListener;
    private boolean isCancelabe = false;

    public DialogHelper(Context context, boolean isCancelabe){
        this.context = context;
        this.isCancelabe = isCancelabe;
    }

    public DialogHelper addDismissListener(DismissListener dismissListener){
        this.dismissListener = dismissListener;
        return this;
    }

    public void showLoadingProgressDialog(int messageId) {
        if (progressDialogWrapper == null) {
            progressDialogWrapper = new ProgressDialogWrapper(context);
        }
        progressDialogWrapper.showLoadingDialog(messageId);
        progressDialogWrapper.getLoadingDialog().setOnDismissListener(
                new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        if (dismissListener != null)
                            dismissListener.dialogDismiss();
                    }
                });
    }

    public void showLoadingProgressDialog(int messageId, boolean isCancelable) {
        if (!isFinishing()) {
            if (progressDialogWrapper == null) {
                progressDialogWrapper = new ProgressDialogWrapper(context);
            }
            progressDialogWrapper.showLoadingDialog(messageId, isCancelable);
        }
    }

    public void hideLoadingProgressDialog() {
        if (progressDialogWrapper == null) {
            progressDialogWrapper = new ProgressDialogWrapper(context);
        }
        progressDialogWrapper.hideLoadingDialog();
    }

    public void dismissLoadingProgressDialog() {
        if (progressDialogWrapper != null) {
            progressDialogWrapper.dismissLoadingDialog();
        }
    }

    public boolean isShowLoadingProgressDialog() {
        if (progressDialogWrapper != null) {
            return progressDialogWrapper.getLoadingDialog().isShowing();
        } else
            return false;
    }

    public void showAlertDialog(int titleId, int messageId, int okTextId,
                                   DialogInterface.OnClickListener okListener, int cancelTextId,
                                   DialogInterface.OnClickListener cancelListener) {
        if (!isFinishing()) {
            alertDialogWrapper = new AlertDialogWrapper(context);
            alertDialogWrapper.showAlertDialog(titleId, messageId, okTextId,
                    okListener, cancelTextId, cancelListener);
        }
    }

    public void showAlertDialog(int titleId, String message, int okTextId,
                                   DialogInterface.OnClickListener okListener, int cancelTextId,
                                   DialogInterface.OnClickListener cancelListener) {
        if (!isFinishing()) {
            alertDialogWrapper = new AlertDialogWrapper(context);
            alertDialogWrapper.showAlertDialog(titleId, message, okTextId,
                    okListener, cancelTextId, cancelListener);
        }
    }

    public void showAlertDialog(String title, String message, int okTextId,
                                DialogInterface.OnClickListener okListener, int cancelTextId,
                                DialogInterface.OnClickListener cancelListener) {
        if (!isFinishing()) {
            alertDialogWrapper = new AlertDialogWrapper(context);
            alertDialogWrapper.showAlertDialog(title, message, okTextId,
                    okListener, cancelTextId, cancelListener);
        }
    }

    public void showAlertDialog(String title, String message, int okTextId,
                                DialogInterface.OnClickListener okListener, int cancelTextId,
                                DialogInterface.OnClickListener cancelListener, int isHtmlFormat) {
        if (!isFinishing()) {
            alertDialogWrapper = new AlertDialogWrapper(context);
            alertDialogWrapper.showAlertDialog(title, message, okTextId,
                    okListener, cancelTextId, cancelListener, isHtmlFormat);
        }
    }

    public void showAlertDialog(String title, String message, int okTextId,
                                DialogInterface.OnClickListener okListener, int cancelTextId,
                                DialogInterface.OnClickListener cancelListener, boolean cancelable) {
        if (!isFinishing()) {
            alertDialogWrapper = new AlertDialogWrapper(context);
            alertDialogWrapper.showAlertDialog(title, message, okTextId,
                    okListener, cancelTextId, cancelListener, cancelable);
        }
    }

    public void showAlertDialog(String title, String message, String okText,
                                DialogInterface.OnClickListener okListener, String cancelText,
                                DialogInterface.OnClickListener cancelListener) {
        if (!isFinishing()) {
            alertDialogWrapper = new AlertDialogWrapper(context);
            alertDialogWrapper.showAlertDialog(title, message, okText,
                    okListener, cancelText, cancelListener);
        }
    }

    public void showAlertDialog(String title, String message, String okText,
                                DialogInterface.OnClickListener okListener, String cancelText,
                                DialogInterface.OnClickListener cancelListener, boolean cancelable) {
        if (!isFinishing()) {
            alertDialogWrapper = new AlertDialogWrapper(context, cancelable);
            alertDialogWrapper.showAlertDialog(title, message, okText,
                    okListener, cancelText, cancelListener);
        }
    }

    public void dismissAlerDialog() {
        if (alertDialogWrapper != null)
            alertDialogWrapper.dismissAlertDialog();
    }

    public void dismissAlerDialog(int time) {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (alertDialogWrapper != null)
                    alertDialogWrapper.dismissAlertDialog();
            }
        };
        handler.sendMessageDelayed(new Message(), time);
    }

    public void dismissAlerDialog(final String url, int time) {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (alertDialogWrapper != null)
                    alertDialogWrapper.dismissAlertDialog();

                if (url != null && url.indexOf("market://") != -1) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            uri);
                    context.startActivity(intent);
                    // isStartActivity = true;
                } else {
                    // Intent intent = new Intent(
                    // BaseActivity.this, AdActivity.class);
                    // intent.putExtra("myAdFullUrl", url);
                    // startActivity(intent);
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            uri);
                    context.startActivity(intent);
                    // isStartActivity = true;
                }
            }
        };
        handler.sendMessageDelayed(new Message(), time);
    }

    private boolean isFinishing(){
        if(context instanceof Activity)
            return ((Activity)context).isFinishing();
        return true;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (alertDialogWrapper != null)
                alertDialogWrapper.dismissAlertDialog();
        }
    };

    public interface DismissListener {
        void dialogDismiss();
    }

    public void release(){
        if(progressDialogWrapper != null) {
            progressDialogWrapper.dismissLoadingDialog();
            progressDialogWrapper= null;
        }

        if(alertDialogWrapper != null) {
            alertDialogWrapper.dismissAlertDialog();
            progressDialogWrapper= null;
        }

//        context = null;
        dismissListener = null;
    }
}


