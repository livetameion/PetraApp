package com.yagna.petra.app.Util;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.TimeZoneNames;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yagna.petra.app.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Utils {

    private final Context ctx;

    public Utils(Context logInActivity) {
        ctx =logInActivity;
    }
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static void ButtonClickEffect(final View v)
    {
        //		  v.setEnabled(false);

        AlphaAnimation obja = new AlphaAnimation(1.0f, 0.3f);
        obja.setDuration(5);
        obja.setFillAfter(false);
        v.startAnimation(obja);
    }


    /**
     * Convert Dp to Pixel
     * 将dp转换为pixel
     */
    public static int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static float dipOrDpToFloat(String value) {
        if (value.contains("dp")) {
            value = value.replace("dp", "");
        }
        else {
            value = value.replace("dip", "");
        }
        return Float.parseFloat(value);
    }

    public boolean checkInternetConnection()
    {

        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    public static String getConvetredDate(String str)
    {



        DateFormat formatt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatt.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String datestring=null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        try {
            format.setTimeZone(TimeZone.getDefault());
            datestring = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datestring;
    }

    public static boolean checkNextday(String str)
    {
        String[] timess =str.split(" ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(timess[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);  // number of days to add
        timess[0]=sdf.format(c.getTime());

        String newdate = timess[0]+ " 04:00:00";


        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar cal =Calendar.getInstance(timeZone);
        Date today = cal.getTime();

        DateFormat formatt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatt.parse(newdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }




       // @SuppressLint("SimpleDateFormat")
       /* SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        try {
            format.setTimeZone(TimeZone.getDefault());
            datestring = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return today.after(date);
    }


        public void setSpans(Spannable text,int start,int end) {

            int blueColor = ctx.getResources().getColor(R.color.ink_blue)/*0xff0000ff*//*Color.parseColor(String.valueOf(R.color.textColorPrimary))*/;
            ForegroundColorSpan blue = new ForegroundColorSpan(blueColor);
            //text.setSpan(new StyleSpan(Typeface.BOLD), start,  end, 0);
            text.setSpan(blue, start, end, 0);

             Typeface tp;
             tp=  Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Regular.ttf");
              text.setSpan (new CustomTypefaceSpan("", tp), 0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

            //text.setSpan(red, 0, namesplit[1].length(), 0);
        }

    public void customToast(String text)
    {
        Toast toast = Toast.makeText(ctx,
                text,
                Toast.LENGTH_LONG);
        TextView tv=(TextView) toast.getView().findViewById(android.R.id.message);
        tv.setTextColor(ctx.getResources().getColor(R.color.white));
        toast.getView().setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimaryDark));
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        View vieew = toast.getView();
        //  vieew.setBackgroundColor(Color.parseColor("#BD8BDC"));
        vieew.setBackgroundResource(R.drawable.toastback);
        toast.setView(vieew);
        toast.show();
    }

    public String getRealPathFromURI(Context context, Uri contentURI) {
//        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentURI, projection, null, null, null);
        //Some of gallery application is return null
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return picturePath;
        }
    }


    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    public static boolean checkPermissionwifi(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_WIFI_STATE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Access Wifi permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public String getBase64(ImageView p_image) {
        BitmapDrawable drawable = (BitmapDrawable) p_image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
        byte[] bb = bos.toByteArray();
        return Base64.encodeToString(bb,0);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
