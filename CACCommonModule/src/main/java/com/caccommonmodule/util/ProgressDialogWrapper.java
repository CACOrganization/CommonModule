package com.caccommonmodule.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.caccommonmodule.CACApplication;

public class ProgressDialogWrapper {

	private Context context;
	private ProgressDialog loadingDialog;
//	private boolean isCancelable = false;

	public ProgressDialogWrapper(Context context) {
		this.context = context;
//		loadingDialog = new ProgressDialog(context, R.style.MyProgressDialog);
		loadingDialog = new ProgressDialog(context);
		loadingDialog.setIndeterminate(true);
		loadingDialog.setCanceledOnTouchOutside(getMainApp(context).isDialogOnTouchable());
		loadingDialog.setCancelable(getMainApp(context).isDialogCancelable());
	}

	private CACApplication getMainApp(Context context){
		return (CACApplication)context.getApplicationContext();
	}

	public void showLoadingDialog(int messageId) {
		loadingDialog.setMessage(context.getResources().getString(messageId));
		loadingDialog.show();
	}

	public void showLoadingDialog(int messageId, boolean isCancelable) {
	    loadingDialog.setCancelable(isCancelable);
        loadingDialog.setMessage(context.getResources().getString(messageId));
        loadingDialog.show();
    }
	
	public void hideLoadingDialog() {
		loadingDialog.hide();
	}

	public void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	public ProgressDialog getLoadingDialog(){
		return loadingDialog;
	}
}
