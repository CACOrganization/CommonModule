package com.caccommonmodule.bulletin;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.caccommonmodule.CACApplication;
import com.caccommonmodule.util.DialogHelper;

/**
 * Created by ac on 2017/1/13.
 */

public abstract class CACBulletinTaskTabActivity extends TabActivity {
    private final String TAG = getClass().getSimpleName();
    protected Context context;

    public boolean callIntent = false;
    //    protected static final int REQUESTCODE = 101;
    private String objectSharepreferenceKey = "hashObject";
    private Object object = new Object();
    private DialogHelper mDialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setNotDoLoadingBulletin();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (NetWorkCheckUtil.checkNetWork(this)) {
//            Log.e("getClass", getClass().toString());
//            if (getMainApp().getSharedPrefUtil().getInt(objectSharepreferenceKey).equals(object.hashCode())
//                    && !callIntent) {
//                // 表示使用者正從Home Screen進入到本App,在這個時機點上,要做點事情
//                Log.i(TAG, "load sync");
//                doLoadingBulletinTask();
//                doAnotherThingForBackground2Foreground();
//            }
//            // }
//            // imgNoNetwork.setVisibility(View.GONE);
//        } else {
//            // imgNoNetwork.setVisibility(View.VISIBLE);
//        }
//        getMainApp().getSharedPrefUtil().put(objectSharepreferenceKey, object.hashCode());
//        callIntent = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDialogHelper != null)
            mDialogHelper.release();
        object = null;
        context = null;
    }

    private CACApplication getMainApp(){
        return (CACApplication)getApplicationContext();
    }

//    protected abstract void doLoadingBulletinTask();
//    protected abstract void doAnotherThingForBackground2Foreground();
//
    protected void setNotDoLoadingBulletin(){
        callIntent = true;
    };

    protected void showLoadingDialog(int messageId, DialogHelper.DismissListener dismissListener) {
        if(context != null)
            getDialogHelper().addDismissListener(dismissListener).showLoadingProgressDialog(messageId);
    }

    protected void showLoadingDialog(int messageId) {
        if(context != null)
            getDialogHelper().showLoadingProgressDialog(messageId);
    }

    public void showLoadingDialog(int messageId, boolean isCancelable) {
        if(context != null)
            getDialogHelper().showLoadingProgressDialog(messageId, isCancelable);
    }

    public void hideLoadingDialog() {
        if(context != null)
            getDialogHelper().hideLoadingProgressDialog();
    }

    protected void dismissLoadingDialog() {
        if(context != null)
            getDialogHelper().dismissLoadingProgressDialog();
    }

    protected boolean isShowLoadingDialog() {
        if(context != null)
            return getDialogHelper().isShowLoadingProgressDialog();
        else
            return false;
    }

    protected void showAlertDialog(int titleId, int messageId, int okTextId,
                                   DialogInterface.OnClickListener okListener, int cancelTextId,
                                   DialogInterface.OnClickListener cancelListener) {
        if(context != null)
            getDialogHelper().showAlertDialog(titleId, messageId, okTextId,
                    okListener, cancelTextId, cancelListener);
    }

    protected void showAlertDialog(int titleId, String message, int okTextId,
                                   DialogInterface.OnClickListener okListener, int cancelTextId,
                                   DialogInterface.OnClickListener cancelListener) {
        if(context != null)
            getDialogHelper().showAlertDialog(titleId, message, okTextId,
                    okListener, cancelTextId, cancelListener);
    }

    public void showAlertDialog(String title, String message, int okTextId,
                                DialogInterface.OnClickListener okListener, int cancelTextId,
                                DialogInterface.OnClickListener cancelListener) {
        if(context != null)
            getDialogHelper().showAlertDialog(title, message, okTextId,
                    okListener, cancelTextId, cancelListener);
    }

    public void showAlertDialog(String title, String message, int okTextId,
                                DialogInterface.OnClickListener okListener, int cancelTextId,
                                DialogInterface.OnClickListener cancelListener, int isHtmlFormat) {
        if(context != null)
            getDialogHelper().showAlertDialog(title, message, okTextId,
                    okListener, cancelTextId, cancelListener, isHtmlFormat);
    }

    public void showAlertDialog(String title, String message, int okTextId,
                                DialogInterface.OnClickListener okListener, int cancelTextId,
                                DialogInterface.OnClickListener cancelListener, boolean cancelable) {
        if(context != null)
            getDialogHelper().showAlertDialog(title, message, okTextId,
                    okListener, cancelTextId, cancelListener, cancelable);
    }

    public void showAlertDialog(String title, String message, String okText,
                                DialogInterface.OnClickListener okListener, String cancelText,
                                DialogInterface.OnClickListener cancelListener) {
        if(context != null)
            getDialogHelper().showAlertDialog(title, message, okText,
                    okListener, cancelText, cancelListener);
    }

    public void dismissAlerDialog() {
        if(context != null)
            getDialogHelper().dismissAlerDialog();
    }

    protected DialogHelper getDialogHelper(){
        if(mDialogHelper == null)
            mDialogHelper = new DialogHelper(this, getMainApp().isDialogCancelable());
        return mDialogHelper;
    }

}