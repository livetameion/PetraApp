package com.pos.petra.app.Util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by bhushan on 11/1/16.
 *
 * @author Bhushan
 */
public class ToastUtils {

	public static void showToast(@NonNull Context context, @StringRes int messageResId) {
		Toast.makeText(context, context.getString(messageResId), Toast.LENGTH_LONG).show();
	}

	public static void showToast(@NonNull Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showToastOnUiThread(@NonNull final Context context, @StringRes final int messageResId) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, context.getString(messageResId), Toast.LENGTH_LONG).show();
			}
		});
	}

	public static void showToastOnUiThread(@NonNull final Context context, final String message) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
		});
	}
}
