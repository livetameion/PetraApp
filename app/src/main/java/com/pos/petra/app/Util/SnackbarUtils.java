package com.pos.petra.app.Util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by bhushan on 11/1/16.
 *
 * @author Bhushan
 */
public class SnackbarUtils {

	public static synchronized void showSnackBar(@NonNull Activity activity, String msg) {
		if (null != msg && null != activity) {
			Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
		}
	}

	public static synchronized void showSnackBar(@NonNull Activity activity, int stringResId) {
		String msg = activity.getString(stringResId);
		if (null != activity) {
			Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
		}
	}

	public static synchronized void showSnackBar(@NonNull View view, String msg) {
		if (null != msg && null != view) {
			Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
		}
	}

	public static synchronized void showSnackBar(@NonNull View view, int stringResId) {
		String msg = view.getContext().getString(stringResId);
		if (null != view) {
			Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
		}
	}
}
