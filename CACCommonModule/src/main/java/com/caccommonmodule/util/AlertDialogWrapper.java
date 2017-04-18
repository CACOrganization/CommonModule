package com.caccommonmodule.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.caccommonmodule.CACApplication;

public class AlertDialogWrapper {

	private Context context;
	private AlertDialog alertDialog;
	private int sdkVersion;

	public AlertDialogWrapper(Context context) {
		this.context = context;
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setCancelable(getMainApp(context).isDialogCancelable());
	    alertDialog.setCanceledOnTouchOutside(getMainApp(context).isDialogOnTouchable());
		sdkVersion = Build.VERSION.SDK_INT;
	}

	public AlertDialogWrapper(Context context, boolean isCancelable) {
		this.context = context;
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setCancelable(isCancelable);
//		alertDialog.setCanceledOnTouchOutside(getMainApp(context).isDialogOnTouchable());
		sdkVersion = Build.VERSION.SDK_INT;
	}

	private CACApplication getMainApp(Context context){
		return (CACApplication)context.getApplicationContext();
	}

//   public AlertDialogWrapper(Context context, boolean isCancelable) {
//        this.context = context;
//        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setCancelable(isCancelable);
//	    alertDialog.setCanceledOnTouchOutside(isCancelable);
//        sdkVersion = Build.VERSION.SDK_INT;
//    }
	   
   public AlertDialogWrapper(Context context, OnKeyListener onKeyListener,
           boolean isCancelable) {
       this.context = context;
       alertDialog = new AlertDialog.Builder(context).create();
       alertDialog.setCancelable(isCancelable);
       alertDialog.setOnKeyListener(onKeyListener);
   }
   
	public void showAlertDialog(int titleId, int messageId, int okTextId,
			OnClickListener okListener, int cancelTextId,
			OnClickListener cancelListener) {
		if(titleId != -1)
			alertDialog.setTitle(titleId);
		alertDialog.setMessage(context.getResources().getString(messageId)
				.replace("<br>", "\n"));
		if(sdkVersion >= 14){
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context
					.getResources().getString(okTextId), okListener);
			if (cancelTextId != -1) {
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context
						.getResources().getString(cancelTextId), cancelListener);
			}
		}else{
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context
					.getResources().getString(okTextId), okListener);
			if (cancelTextId != -1) {
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context
						.getResources().getString(cancelTextId), cancelListener);
			}
		}
		alertDialog.show();
		TextView v = (TextView) alertDialog.findViewById(android.R.id.message);
		if (v != null) {
			v.setText(Html
					.fromHtml(context.getResources().getString(messageId)));
			v.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}

	public void showAlertDialog(int titleId, String message, int okTextId,
			OnClickListener okListener, int cancelTextId,
			OnClickListener cancelListener) {
		if(titleId != -1)
			alertDialog.setTitle(titleId);
		alertDialog.setMessage(message.replace("<br>", "\n"));
		if(sdkVersion >= 14){
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context
					.getResources().getString(okTextId), okListener);
			if (cancelTextId != -1) {
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context
						.getResources().getString(cancelTextId), cancelListener);
			}
		}else{
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context
					.getResources().getString(okTextId), okListener);
			if (cancelTextId != -1) {
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context
						.getResources().getString(cancelTextId), cancelListener);
			}
		}
		alertDialog.show();
		TextView v = (TextView) alertDialog.findViewById(android.R.id.message);
		if (v != null) {
			v.setText(Html.fromHtml(message));
			v.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}

   public void showAlertDialog(String titleId, String message, int okTextId,
            OnClickListener okListener, int cancelTextId,
            OnClickListener cancelListener, int isHtmlFormat) {
        alertDialog.setTitle(titleId);
        if(isHtmlFormat == -1)
            alertDialog.setMessage(message);
        else
            alertDialog.setMessage(message.replace("<br>", "\n"));
        if(sdkVersion >= 14){
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context
                    .getResources().getString(okTextId), okListener);
            if (cancelTextId != -1) {
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context
                        .getResources().getString(cancelTextId), cancelListener);
            }
        }else{
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context
                    .getResources().getString(okTextId), okListener);
            if (cancelTextId != -1) {
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context
                        .getResources().getString(cancelTextId), cancelListener);
            }
        }
        alertDialog.show();
        TextView v = (TextView) alertDialog.findViewById(android.R.id.message);
        if (v != null) {
            v.setText(message);
//	            v.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
	   
	public void showAlertDialog(String title, String message, int okTextId,
			OnClickListener okListener, int cancelTextId,
			OnClickListener cancelListener) {
		alertDialog.setTitle(title);
		alertDialog.setMessage(message.replace("<br>", "\n"));
		if(sdkVersion >= 14){
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context
					.getResources().getString(okTextId), okListener);
			if (cancelTextId != -1) {
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context
						.getResources().getString(cancelTextId), cancelListener);
			}
		}else{
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context
					.getResources().getString(okTextId), okListener);
			if (cancelTextId != -1) {
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context
						.getResources().getString(cancelTextId), cancelListener);
			}
		}
		alertDialog.show();
		TextView v = (TextView) alertDialog.findViewById(android.R.id.message);
		if (v != null) {
		    v.setText(Html.fromHtml(message));
			v.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}
	
	public void showAlertDialog(String title, String message, int okTextId,
			OnClickListener okListener, int cancelTextId,
			OnClickListener cancelListener, boolean cancelable) {
		alertDialog.setTitle(title);
		alertDialog.setMessage(message.replace("<br>", "\n"));
		alertDialog.setCancelable(cancelable);
		if(sdkVersion >= 14){
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context
					.getResources().getString(okTextId), okListener);
			if (cancelTextId != -1) {
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context
						.getResources().getString(cancelTextId), cancelListener);
			}
		}else{
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context
					.getResources().getString(okTextId), okListener);
			if (cancelTextId != -1) {
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context
						.getResources().getString(cancelTextId), cancelListener);
			}
		}
		alertDialog.show();
		TextView v = (TextView) alertDialog.findViewById(android.R.id.message);
		if (v != null) {
			v.setText(Html.fromHtml(message));
			v.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}
	
	public void showAlertDialog(String title, String message, String okText,
			OnClickListener okListener, String cancelText,
			OnClickListener cancelListener) {
		alertDialog.setTitle(title);
		alertDialog.setMessage(message.replace("<br>", "\n"));
		if(sdkVersion >= 14){
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, okText , okListener);
			if (cancelText != null && cancelText.length() > 0) {
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, cancelText , cancelListener);
			}
		}else{
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, okText , okListener);
			if (cancelText != null && cancelText.length() > 0) {
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelText , cancelListener);
			}
		}
		alertDialog.show();
		TextView v = (TextView) alertDialog.findViewById(android.R.id.message);
		if (v != null) {
//			v.setText(Html.fromHtml("<font color='#FFFFFF'>" + message + "</font>"));
			v.setText(Html.fromHtml(message));
			//v.setMovementMethod(LinkMovementMethod.getInstance());
			v.setMovementMethod(new innerWebViewLink());
			
		}
	}
	
	public void dismissAlertDialog() {
		if(isShowing())
			alertDialog.dismiss();
	}
	
	private class innerWebViewLink extends LinkMovementMethod {

		@Override
		public boolean onTouchEvent(TextView widget, Spannable buffer , MotionEvent event) {

			int action = event.getAction();
			if (action == MotionEvent.ACTION_UP) {

				int x = (int) event.getX();
				int y = (int) event.getY();

				x -= widget.getTotalPaddingLeft();
				y -= widget.getTotalPaddingTop();

				x += widget.getScrollX();
				y += widget.getScrollY();

				Layout layout = widget.getLayout();
				int line = layout.getLineForVertical(y);
				int off = layout.getOffsetForHorizontal(line, x);
				ClickableSpan[] link = buffer.getSpans(off, off,
						ClickableSpan.class);
				if (link.length > 0) {
					URLSpan urlSpan = (URLSpan) link[0];
					final String linkUrl = urlSpan.getURL();
					if(linkUrl.indexOf("play.google.com") != -1){
						String url = "";
						if (Build.VERSION.SDK_INT >= 8) {
							url = "market://details?id=" + context.getApplicationContext().getPackageName();
						} else {
							url = "https://play.google.com/store/apps/details?id=" + context.getApplicationContext().getPackageName() + "&feature=search_result";
						}
						Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
						context.startActivity(intent);
					}else{
					 // 另開網頁
//						if(){
//							Intent intent = new Intent(context,
//									WebViewActivity.class);
//							intent.putExtra("url", link);
//							context.startActivity(intent);
//						}
                        
					    Uri uri = Uri.parse(linkUrl);
					    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					    context.startActivity(intent);
					}
				}
			}

			return false;
		}

	}
	
	public boolean isShowing(){
	    return alertDialog.isShowing();
	}

}
