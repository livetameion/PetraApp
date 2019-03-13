package com.yagna.petra.app.Util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yagna.petra.app.Payment.PaymentActivity;
import com.yagna.petra.app.R;


public class CustomDialog /*implements android.view.View.OnClickListener*/ {
	Context context;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialog;
	AlertDialog alertDialogExit;
	boolean isExit = false;
	Dialog dialog;
	boolean flag;
	Utils util;
	public static View view ;
	//Typeface sansregular;

	public CustomDialog(Context context) {
		this.context = context;
		alertDialog = new AlertDialog.Builder(context);
		util=new Utils(context);
	//	sansregular = Utils.halvaticaRegular();
	}
	public void show(String title) {

		dialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar);
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setCancelable(true);
		Rect displayRectangle = new Rect();
		Window window =((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
		dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.util_loader);
		TextView txt_title_loader=(TextView)dialog.findViewById(R.id.txt_title_loader);
//		Typeface sansregular= Utils.halvaticaRegular();
		//txt_title_loader.setTypeface(sansregular);
		try {
			if (dialog != null) {
				if (!dialog.isShowing()) {
					dialog.show();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}



	public void hide() {
		if (dialog != null) {
			dialog.cancel();
			dialog.dismiss();
		}
	}

	public boolean isDialogShowing() {
		if (dialog != null && dialog.isShowing())
			return dialog.isShowing();
		else
			return false;
	}
	/*public void showinternetAlert(String msg) {

		dialog = new Dialog(context,R.style.DialogTheme);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.internetdialog, null);
		Rect displayRectangle = new Rect();
		Window window =((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
		view.setMinimumWidth((int)(displayRectangle.width() * 1f));
		view.setMinimumHeight((int)(displayRectangle.height() * 0.1f));

		TextView txtV = new TextView(context);
		txtV.setText(msg);
		txtV.setTextColor(context.getResources().getColor(R.color.d1purple));
		txtV.setTextSize(context.getResources().getDimension(R.dimen.textsize));
		txtV.setGravity(Gravity.CENTER_HORIZONTAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT ,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		txtV.setLayoutParams(lp);
		LinearLayout ln = (LinearLayout) view.findViewById(R.id.laymsgbox);
		ln.addView(txtV);
		Button btnOkPopup = (Button) view.findViewById(R.id.btnOkPopup);
//		btnOkPopup.setOnClickListener(this);
		dialog.setContentView(view);
		*//**Set Dialog width match parent*//*
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.setCanceledOnTouchOutside(false);
		try {
			if (dialog != null) {
				if (!dialog.isShowing()) {
					dialog.show();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void hide() {
		if (dialog != null) {
			dialog.cancel();
			dialog.dismiss();
		}
	}

	public boolean isDialogShowing() {
		if (dialog != null && dialog.isShowing())
			return dialog.isShowing();
		else
			return false;
	}*/


	public void showAlertmulti(boolean flag,String msg) {
		dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_alert_yes_no, null);
		Rect displayRectangle = new Rect();
		Window window = ((Activity)context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
		dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
		TextView txtV = view.findViewById(R.id.msg);
		txtV.setText(msg);
		dialog.setContentView(view);
		try {
			if (dialog != null) {
				if (!dialog.isShowing()) {
					dialog.show();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
