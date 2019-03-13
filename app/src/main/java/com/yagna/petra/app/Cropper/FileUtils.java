package com.yagna.petra.app.Cropper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import com.yagna.petra.app.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rushil on 14/12/16.
 */

public class FileUtils {

    private String TAG=FileUtils.class.getName();
    private static FileUtils fileUtils;
    public enum MEDIA_TYPE{PICTURE,VIDEO}
    public static FileUtils getInstance()
    {
        if(fileUtils==null)
            fileUtils=new FileUtils();
        return fileUtils;
    }


    /**
     * Save bitmap to Application name folder and return Path
     * @param context Context of activity or application
     * @param bitmap Bitmap image
     * @param subDir Sub directory name which will create under App name folder
     */
    public String saveBitmap(@NonNull Context context, @NonNull Bitmap bitmap, @Nullable String subDir) {
        try {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));

            File myDir;
            if (subDir != null && subDir.length() > 0)
                myDir = new File(mediaStorageDir + File.separator + subDir);
            else
                myDir = new File(mediaStorageDir + "");

            if (!myDir.exists())
                myDir.mkdirs();


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imgName = "IMG-" + timeStamp + ".jpg";

            File file = new File(myDir, imgName);
            if (file.exists())
                file.delete();


            if (bitmap != null) {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            }

            return file.getPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String saveBitmapWithPath(@NonNull Bitmap bitmap, String filePathWithName) {
        try {

            File file = new File(filePathWithName);
            if (file.exists())
                file.delete();

            if (bitmap != null) {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            }

            return file.getPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**Return File URI according media type*/
    public Uri getFileUri(Context context, MEDIA_TYPE mediaType)
    {
        try {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
            //Create Media Directory
            File mediaDir=new File(mediaStorageDir + File.separator + context.getString(R.string.dirMedia));
            if(!mediaDir.exists())
                mediaDir.mkdirs();

            String subDirName="",fileExtension="";
            switch (mediaType)
            {
                case PICTURE:
                    subDirName=context.getString(R.string.dirImages);
                    fileExtension=".jpg";
                    break;
                case VIDEO:
                    subDirName=context.getString(R.string.dirVideos);
                    fileExtension=".mp4";
                    break;
            }
            File subDir = new File(mediaDir + File.separator + subDirName);
            if(!subDir.exists())
                subDir.mkdirs();

            String mediaFileName = System.currentTimeMillis() + fileExtension;

            File mediaFile = new File(subDir, mediaFileName);

            return Uri.fromFile(mediaFile);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**Return File path according media type*/
    public String getFilePath(Context context, MEDIA_TYPE mediaType)
    {
        try {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
            //Create Media Directory
            File mediaDir=new File(mediaStorageDir + File.separator + context.getString(R.string.dirMedia));
            if(!mediaDir.exists())
                mediaDir.mkdirs();

            String subDirName="",fileExtension="";
            switch (mediaType)
            {
                case PICTURE:
                    subDirName=context.getString(R.string.dirImages);
                    fileExtension=".jpg";
                    break;
                case VIDEO:
                    subDirName=context.getString(R.string.dirVideos);
                    fileExtension=".mp4";
                    break;
            }
            File subDir = new File(mediaDir + File.separator + subDirName);
            if(!subDir.exists())
                subDir.mkdirs();

            String mediaFileName = System.currentTimeMillis() + fileExtension;

            File mediaFile = new File(subDir, mediaFileName);

            return mediaFile.getAbsolutePath();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**Return File directory path*/
    public String getFileDirPath(Context context, MEDIA_TYPE mediaType)
    {
        try {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
            //Create Media Directory
            File mediaDir=new File(mediaStorageDir + File.separator + context.getString(R.string.dirMedia));
            if(!mediaDir.exists())
                mediaDir.mkdirs();

            String subDirName="";
            switch (mediaType)
            {
                case PICTURE:
                    subDirName=context.getString(R.string.dirImages);
                    break;
                case VIDEO:
                    subDirName=context.getString(R.string.dirVideos);
                    break;
            }
            File subDir = new File(mediaDir + File.separator + subDirName);
            if(!subDir.exists())
                subDir.mkdirs();

            return subDir.getAbsolutePath();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**Copy file from source to target location with target file name and extension
     * @param sourcePath Source file path
     * @param targetPath Target File path with file name e.g path/filName.jpg
     * */
    public String copyFile(String sourcePath, String targetPath) {
        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            /*File dir = new File (targetPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }*/
            //File sourceFile=new File(inputPath);
            File outputFile=new File(targetPath);
//            File outputFile=new File(outputPath + File.separator + sourceFile.getName());

            in = new FileInputStream(sourcePath);
            out = new FileOutputStream(outputFile.getAbsoluteFile());

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            //in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            //out = null;

            return outputFile.getAbsolutePath();

        }  catch (FileNotFoundException fnfe1) {
            Log.e(TAG, fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return "";
    }

    public  void copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);

            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }


}
