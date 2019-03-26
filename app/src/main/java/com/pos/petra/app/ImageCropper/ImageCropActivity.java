package com.pos.petra.app.ImageCropper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.R;

import java.io.File;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ImageCropActivity extends GlobalActivity implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnGetCroppedImageCompleteListener {
    //private static final int DEFAULT_ASPECT_RATIO_VALUES = 20;
    private int mAspectRatioX;//DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY;//DEFAULT_ASPECT_RATIO_VALUES;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private CropImageView mIvCropView;
    private FrameLayout mImgCropScreenView;
    // Saves the state upon rotating the screen/restarting the activity
    private Uri imageUri;
    public static String EXTRA_IMAGE_PATH="imagePath",EXTRA_X_RATIO="xratio",EXTRA_Y_RATIO="yratio";
    @Override
    protected void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(@SuppressWarnings("NullableProblems") Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_crop);

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }

        mImgCropScreenView = (FrameLayout) findViewById(R.id.xImgCropView);
        Intent i = getIntent();
        Bundle bundle=i.getExtras();
        if(bundle!=null)
        {
            mAspectRatioX = bundle.getInt(EXTRA_X_RATIO, 16);
            mAspectRatioY = bundle.getInt(EXTRA_Y_RATIO, 26);
            if(bundle.containsKey(EXTRA_IMAGE_PATH))
                imageUri= Uri.fromFile(new File(bundle.getString(EXTRA_IMAGE_PATH)));
        }
        /*if (getIntent().hasExtra("uri")) {
            Constants.IMAGE_URI = Uri.parse(getIntent().getStringExtra("uri"));
        }*/

        mIvCropView = (CropImageView) findViewById(R.id.xIvCropView);
        mIvCropView.setCropShape(CropImageView.CropShape.RECTANGLE);
        mIvCropView.setFixedAspectRatio(true);
        mIvCropView.setAspectRatio(mAspectRatioX, mAspectRatioY);//(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
        mIvCropView.setGuidelines(2);
        mIvCropView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //StaticUtils.printErr("Image URI Image crop on create: " + StaticUtils.IMAGE_URI);

        /*if (Constants.IMAGE_URI != null)
            mIvCropView.setImageUriAsync(Constants.IMAGE_URI);
        else
            ToastHelper.getInstance().showToast(this,getString(R.string.msgFailedToLoadImage))*/;

        if (imageUri != null)
            mIvCropView.setImageUriAsync(imageUri);
        else
            Toast.makeText(this,"Fail to load image", Toast.LENGTH_SHORT).show();
//            ToastHelper.getInstance().showToast(this,getString(R.string.msgFailedToLoadImage));

        TextView btnCancel = (TextView) findViewById(R.id.xBtnCropCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView btnDone = (TextView) findViewById(R.id.xBtnCropOk);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Constants.BTN_OK_CLICKED = true;
                mIvCropView.getCroppedImageAsync(mIvCropView.getCropShape(), 0, 0);
                /*new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (Constants.CROPPED_BITMAP == null) {
                            Toast.makeText(ImageCropActivity.this, "error in crop", Toast.LENGTH_LONG).show();
                        }
                        ImageCropActivity.this.finish();
                    }
                }, 2000);*/
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIvCropView.setOnSetImageUriCompleteListener(this);
        mIvCropView.setOnGetCroppedImageCompleteListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIvCropView.setOnSetImageUriCompleteListener(null);
        mIvCropView.setOnGetCroppedImageCompleteListener(null);
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Log.e("IMAGE CROP", "IMAGE uri: : " + uri + " PATH : " + uri.getPath());
        } else {
//          StaticUtils.mShowToast(mImgCropScreenView, "Failed to load image!", Snackbar.LENGTH_SHORT);
//            ToastHelper.getInstance().showToast(this,getString(R.string.msgFailedToLoadImage));
            Toast.makeText(this,"Fail to load image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {
        if (error == null) {
           // Constants.CROPPED_BITMAP = bitmap;

            String croppedImagePath=FileUtils.getInstance().saveBitmapWithPath(bitmap,imageUri.getPath());
            Intent intent=new Intent();
            intent.putExtra(EXTRA_IMAGE_PATH,croppedImagePath);
            setResult(RESULT_OK,intent);
            finish();


        } else {
            Toast.makeText(this,"Fail to load image", Toast.LENGTH_SHORT).show();
//            ToastHelper.getInstance().showToast(this,getString(R.string.msgImageCropFailed));
//          StaticUtils.mShowToast(mImgCropScreenView, "Image crop failed: " + error.getMessage(), Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
//        Constants.BTN_OK_CLICKED = false;
        ImageCropActivity.this.finish();
    }
}