package com.pos.petra.app.Util;


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

import com.pos.petra.app.R;


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
