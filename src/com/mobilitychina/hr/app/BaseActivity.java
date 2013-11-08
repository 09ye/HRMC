package com.mobilitychina.hr.app;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;



public class BaseActivity extends Activity {
	/**
	 * 标识测试环境
	 */
	private ImageView testMarkImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (this.shouldShowTestMark()) {
//			if (!CommonUtil.isMainServer(this)) {
//				this.setTestInfoHidden(false);
//			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (testMarkImageView != null && testMarkImageView.getParent() != null) {
			WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
			wm.removeView(testMarkImageView);
			testMarkImageView = null;
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}

	protected boolean shouldShowTestMark() {
		return true;
	}

	protected Dialog dialog;

	public void dismissDialog() {
		if (dialog != null)
			dialog.dismiss();
		dialog = null;
	}

	protected void onProgressDialogCancel() {

	}

	public void showProgressDialog(String title,String button,int which,DialogInterface.OnClickListener listener) {
		dismissDialog();
		ProgressDialog dlg = new ProgressDialog(this);
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onProgressDialogCancel();
			}
		});
		dlg.setButton(DialogInterface.BUTTON_NEGATIVE, button, listener);
		dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return false;
			}
		});
		dlg.setMessage(title);
		
		dialog = dlg;
		dlg.show();
	}
	public void showProgressDialog(String title,String button,int which,DialogInterface.OnClickListener listener,Boolean canCancel) {
		dismissDialog();
		ProgressDialog dlg = new ProgressDialog(this);
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onProgressDialogCancel();
			}
		});
		dlg.setButton(DialogInterface.BUTTON_NEGATIVE, button, listener);
		dlg.setCancelable(canCancel);
		dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return false;
			}
		});
		dlg.setMessage(title);
		dialog = dlg;
		dlg.show();
	}
	public void showProgressDialog(String title,Boolean canCancel) {
		dismissDialog();
		ProgressDialog dlg = new ProgressDialog(this);
		dlg.setCancelable(canCancel);
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onProgressDialogCancel();
			}
		});
		dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return false;
			}
		});
		dlg.setMessage(title);
		dialog = dlg;
		dlg.show();
	}
	public void showProgressDialog(String title) {
		dismissDialog();
		ProgressDialog dlg = new ProgressDialog(this);
		//dlg.setCancelable(false);
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onProgressDialogCancel();
			}
		});
		dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return false;
			}
		});
		dlg.setMessage(title);
		dialog = dlg;
		dlg.show();
	}
	public void editDialog(String title){
		if(dialog != null){
			if(dialog instanceof ProgressDialog){
				((ProgressDialog)dialog).setMessage(title);
			}
		}
	}

	public void showDialog(int titleResId, int messageResId, DialogInterface.OnClickListener listener) {
		Builder builder = new Builder(this);
		builder.setTitle(titleResId);
		builder.setMessage(messageResId);
		builder.setPositiveButton("确定", listener);
		builder.setCancelable(false);
		builder.show();
	}

	public void showDialog(String title, String message, DialogInterface.OnClickListener listener) {
		Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", listener);
		builder.setCancelable(false);
		builder.show();
	}
	public void showDialog(String title, String message, String posMessage,String negMessage,  DialogInterface.OnClickListener listener) {
		Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(posMessage, listener);
		builder.setNegativeButton(negMessage, listener);
		builder.setCancelable(false);
		builder.show();
	}
	
}
