package com.pos.petra.app.Admin.Product.ProductDetail;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pos.petra.app.Util.PrefUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.ImageCropper.FileUtils;
import com.pos.petra.app.ImageCropper.ImageCropActivity;
import com.pos.petra.app.Admin.Product.Discount.DiscountActivity;
import com.pos.petra.app.Admin.Product.Modifer.ModifierActivity;
import com.pos.petra.app.Admin.Product.ProductDashboard.ProductDashboardActivity;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.TouchImageView;
import com.pos.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

public class ProductDetailActivity extends AppCompatActivity {
    private EditText d_product_name;
    private ImageView d_image;
    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    public RecyclerView listview;
    int  REQUEST_CAMERA = 3;
    private Utils util;
    private CustomDialog cd;
    private ProductDetailActivity context;
    Uri myuri;
    private Uri imageToUploadUri;
    String  productid = null;
    SimpleDateFormat sdf;
    boolean imageupdate = false, flag = false;
    private String admin_id;
    private String merchant_location_id;
    private LinearLayout  modifier, discount;
    public CardView cv_add;
    TextView p_name,p_price, p_stock,product_sku,p_description,txt_titledescription,txt_titleprice,txt_titlepcode,txt_titlepname, txt_add_modif,txt_add_discount;
    EditText d_price, d_stock,d_description,d_sku;
    ImageView p_img;
    ImageView edit,delete;
    private String categ_id;
    private String croppedImagePath;
    private SharedPreferences common_mypref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       
        context = ProductDetailActivity.this;
    
        util = new Utils(context);
        util = new Utils(context);
        cd = new CustomDialog(context);

        p_stock =findViewById(R.id.p_stock);
        p_img =(ImageView) findViewById(R.id.p_img);
        p_name = findViewById(R.id.product_name);
        p_price = findViewById(R.id.product_price);
        p_description = findViewById(R.id.product_desc);
        edit= findViewById(R.id.edit_);
        delete= findViewById(R.id.delete_);
        txt_titledescription=findViewById(R.id.txt_titledescription);
        txt_titleprice=findViewById(R.id.txt_titleprice);
        txt_titlepcode=findViewById(R.id.txt_titlepcode);
        txt_titlepname=findViewById(R.id.txt_titlepname);
        txt_add_modif =findViewById(R.id.txt_add_modif);
        txt_add_discount =findViewById(R.id.txt_add_discount);
        discount=(LinearLayout) findViewById(R.id.discount);
        modifier=(LinearLayout) findViewById(R.id.modifier);
        product_sku= findViewById(R.id.product_sku);
        fillDetail();
        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            admin_id = jobj.getJSONObject("admin").getString("admin_id");
            merchant_location_id = jobj.getJSONObject("admin").getString("merchant_location_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void fillDetail() {

        if(DataKeeper.detail_product.image_full_path.trim().length()!=0)
        {
            Picasso.get()
                    .load(DataKeeper.detail_product.image_full_path.replace(" ","%20"))
                    // .placeholder(R.mipmap.petra)
                    //  .error(R.mipmap.petra)
                    .into(p_img );
        }
        else{
            Picasso.get()
                    .load(R.mipmap.petra)
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(p_img );
        }
        p_stock.setText(DataKeeper.detail_product.stock);
        product_sku.setText(DataKeeper.detail_product.sku);
        p_name.setText(DataKeeper.detail_product.title);
        p_price.setText("$ "+DataKeeper.detail_product.price);
        p_description.setText(DataKeeper.detail_product.description);
        p_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showimage();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showProductDialog();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.showAlertmulti(false,"Do you want to delete \""+p_name.getText().toString()+"\" Product?");
                context.productid=DataKeeper.detail_product.id;


            }
        });

      
        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DiscountActivity.class);
                context.startActivity(i);
            }
        });

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ModifierActivity.class);
                context.startActivity(i);
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);

        return true;
    }


    public void showimage() {
        final Dialog Dialog = new Dialog(ProductDetailActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.banner_full_image, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        rowView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        rowView.setMinimumHeight((int) (displayRectangle.height() * 0.5f));
        TouchImageView imgview = (TouchImageView) rowView.findViewById(R.id.image_detail);
        if ( DataKeeper.detail_product.image_full_path.trim().length() != 0) {
            Picasso.get()
                    .load(DataKeeper.detail_product.image_full_path.replace(" ", "%20"))
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(imgview);
        } else {
            Picasso.get()
                    .load(R.mipmap.petra)
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(imgview);
        }


        imgview.setMaxZoom(4f);

        Dialog.setContentView(rowView);
        Dialog.setCancelable(true);
        Dialog.setCanceledOnTouchOutside(true);
        Dialog.show();
    }


    public void myClickMethod(final View v) {

        switch (v.getId()) {

            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                setUrlPayload(Constants.DELETEPRODITEM,0);

                cd.hide();
                break;
            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();
                break;
        }
    }

    public void showProductDialog(){

        final Dialog alertDialog = new Dialog(ProductDetailActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        final View rowView = (View) inflater.inflate(R.layout.dialog_add_product, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.translucent_black);
        d_product_name=(EditText) rowView.findViewById(R.id.p_name);
        d_price =(EditText) rowView.findViewById(R.id.p_price);
        d_stock =(EditText) rowView.findViewById(R.id.p_stock);
        d_description =(EditText) rowView.findViewById(R.id.p_discrption);
        d_image = (ImageView) rowView.findViewById(R.id.p_cropimage);
        d_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        d_sku = (EditText) rowView.findViewById(R.id.p_sku);


            this.flag=true;
            if(DataKeeper.detail_product.image_full_path.trim().length()!=0)
            {
                Picasso.get()
                        .load(DataKeeper.detail_product.image_full_path.replace(" ","%20"))
                        .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(d_image);
            }
            else{
                Picasso.get()
                        .load(R.mipmap.petra)
                        .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(d_image );
            }

            d_sku.setText(DataKeeper.detail_product.sku);
            d_stock.setText(DataKeeper.detail_product.stock);
            d_stock.setSelection(p_stock.getText().length());
            d_product_name.setText(DataKeeper.detail_product.title);
            d_product_name.setSelection(d_product_name.getText().length());
            d_price.setText(DataKeeper.detail_product.price);
            d_description.setText(DataKeeper.detail_product.description);
            productid= DataKeeper.detail_product.id;
            categ_id = DataKeeper.detail_product.category;


        Button done = (Button) rowView.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/////////////////////////////////////////////////////
                Log.e("calling","validation");
                if(validationProd())
                {
                    Log.e("callied","validation");
                    if(flag){
                        setUrlPayload(Constants.UPDATEPRODITEM, Integer.parseInt(productid));

                    }
                    else
                    {
                        setUrlPayload(Constants.ADDPRODITEM,0);
                    }
                    myuri=null;
                    alertDialog.dismiss();

                }

            }
        });
        Button back = (Button) rowView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        d_description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(validationProd())
                    {
                        Log.e("callied","validation");
                        if(flag){
                            setUrlPayload(Constants.UPDATEPRODITEM, Integer.parseInt(productid));

                        }
                        else
                        {
                            setUrlPayload(Constants.ADDPRODITEM,0);
                        }
                        myuri=null;
                        alertDialog.dismiss();

                    }
                }
                return false;
            }
        });
        alertDialog.setContentView(rowView);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }

    private boolean validationProd(){
        boolean flag = true;
        /*if(myuri==null)
        {
            util.customToast("Please add your product's Image.");
            flag = false;
        }
        else*/ if(d_stock.getText().toString().trim().length()==0){
            // product_name.setError("Product Name should not be null!");
            util.customToast(getResources().getString(R.string.productcodenull));
            d_stock.requestFocus();
            flag = false;
        }
        else if(d_product_name.getText().toString().trim().length()==0){
            // product_name.setError("Product Name should not be null!");
            util.customToast(getResources().getString(R.string.productnamenull));
            d_product_name.requestFocus();
            flag = false;
        }
        else if (!(d_price.getText().toString().trim().length()>0)&& TextUtils.isDigitsOnly(d_price.getText().toString().trim())){
            // p_price.setError("Digit Only!");
            util.customToast(getResources().getString(R.string.productprice));
            d_price.requestFocus();

        }
        else if(d_description.getText().toString().trim().length()==0){
            util.customToast(getResources().getString(R.string.descriptionnull));
            d_description.requestFocus();
            flag = false;
        }
        return flag;
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProductDetailActivity.this);
        builder.setTitle("Add Product Image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=util.checkPermission(ProductDetailActivity.this);
                if (items[item].equals("Take Photo")) {
                    // userChoosenTask="Take Photo";
                    if(result)
                        // cameraIntent();
                        captureCameraImage();
                } else if (items[item].equals("Choose from Library")) {
                    //  userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void galleryIntent() {
        //startActivityForResult(MediaStoreUtils.getPickImageIntent(b_OffersActivity.this), REQUEST_PICTURE);
        myuri=null;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), REQUEST_PICTURE);
    }
    public void captureCameraImage() {
        myuri=null;
       /* Intent chooserIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        chooserIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        imageToUploadUri = getCaptureImageOutputUri();
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);
        startActivityForResult(chooserIntent, REQUEST_CAMERA);*/
        // Determine Uri of camera image to save.
        imageToUploadUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (imageToUploadUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }
        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);
        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        startActivityForResult(chooserIntent, REQUEST_CAMERA);
    }
    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    protected void onResume(){
        super.onResume();
        txt_add_modif.setText(" "+(DataKeeper.detail_product.modifier.size()==0?"(0)":"("+DataKeeper.detail_product.modifier.size()+")"));
        txt_add_discount.setText(" "+(DataKeeper.detail_product.discount.size()==0?"(0)":"("+DataKeeper.detail_product.discount.size()+")"));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            File croppedImageFile = new File(getFilesDir(), "test.jpg");
            if (requestCode == REQUEST_PICTURE && resultCode == RESULT_OK) {

                String copiedFilepath=null;
                try {
                    File actualImage = FileUtil.from(this, data.getData());
                    copiedFilepath=actualImage.getAbsolutePath();
                }catch (Exception e)
                {
                    util.customToast("Image Not Available");
                }
                File file = new File(copiedFilepath);
                long length = file.length();
                if(length>=1007200) {
                    util.customToast("Image Size very big");
                    File compressedImage = new Compressor.Builder(this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.PNG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .build()
                            .compressToFile(file);
                    copiedFilepath=compressedImage.getAbsolutePath();
                }
                Intent intent = new Intent(this, ImageCropActivity.class);
                intent.putExtra(ImageCropActivity.EXTRA_X_RATIO, 26);///26,15 for banner
                intent.putExtra(ImageCropActivity.EXTRA_Y_RATIO, 26);
                intent.putExtra(ImageCropActivity.EXTRA_IMAGE_PATH, copiedFilepath);
                startActivityForResult(intent, REQUEST_CROP_PICTURE);
            } else if (requestCode == REQUEST_CROP_PICTURE && resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                     croppedImagePath = bundle.getString(ImageCropActivity.EXTRA_IMAGE_PATH);
                    if(flag)
                    {
                        imageupdate=true;
                    }
                    myuri = Uri.parse(croppedImagePath);
                    Picasso.get().load(new File(croppedImagePath)).memoryPolicy(MemoryPolicy.NO_CACHE).into(d_image);
                }
            } else if (requestCode == REQUEST_CAMERA) {
                if (resultCode != RESULT_CANCELED) {
                    if (imageToUploadUri != null) {
                       /* Uri croppedImage = imageToUploadUri;
                        Intent intent = new Intent(this, ImageCropActivity.class);
                        String picturePath = util.getRealPathFromURI(this, croppedImage);
                        String copiedFilepath = FileUtils.getInstance().copyFile(picturePath, FileUtils.getInstance().getFilePath(this, FileUtils.MEDIA_TYPE.PICTURE));
                        intent.putExtra(ImageCropActivity.EXTRA_X_RATIO, 26);///26,15 for banner
                        intent.putExtra(ImageCropActivity.EXTRA_Y_RATIO, 26);
                        intent.putExtra(ImageCropActivity.EXTRA_IMAGE_PATH, copiedFilepath);
                        startActivityForResult(intent, REQUEST_CROP_PICTURE);*/
                        String copiedFilepath=null;
                        Uri croppedImage = imageToUploadUri;
                        String picturePath = util.getRealPathFromURI(this, croppedImage);
                        copiedFilepath = FileUtils.getInstance().copyFile(picturePath, FileUtils.getInstance().getFilePath(this, FileUtils.MEDIA_TYPE.PICTURE));
                        File file = new File(copiedFilepath);
                        util.customToast("Image Size very big");
                        File compressedImage = new Compressor.Builder(this)
                                .setMaxWidth(1280)
                                .setMaxHeight(960)
                                .setQuality(75)
                                .setCompressFormat(Bitmap.CompressFormat.PNG)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .build()
                                .compressToFile(file);
                        copiedFilepath=compressedImage.getAbsolutePath();
                        Intent intent = new Intent(this, ImageCropActivity.class);
                        intent.putExtra(ImageCropActivity.EXTRA_X_RATIO, 26);///26,15 for banner
                        intent.putExtra(ImageCropActivity.EXTRA_Y_RATIO, 26);
                        intent.putExtra(ImageCropActivity.EXTRA_IMAGE_PATH, copiedFilepath);
                        startActivityForResult(intent, REQUEST_CROP_PICTURE);
                    }
                }
            }
        }else
        {
            if(!imageupdate) {
                myuri = null;
                Picasso.get()
                        .load(myuri)
                        .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(d_image);
            }
        }
    }


    public void setUrlPayload(int action,int extra) {
        switch (action){


            case  Constants.UPDATEPRODITEM:
                Log.e("calliang","Add Product");
                //sendDataToServer(Constants.b_Add_Product);
                JSONObject j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", merchant_location_id);
                    data.put("admin_id", admin_id);
                    JSONObject categ = new JSONObject();
                    categ.put("id", productid);
                    categ.put("title", d_product_name.getText().toString().trim());
                    categ.put("description", d_description.getText().toString().trim());
                    categ.put("category",categ_id );
                    categ.put("stock", d_stock.getText().toString().trim());
                    categ.put("sku", d_sku.getText().toString().trim());
                    categ.put("price", d_price.getText().toString().trim());
                    categ.put("img", util.getBase64(d_image));
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("products",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.ProductAPI,j_obj,Constants.UPDATEPRODITEM,Constants.UPDATE,extra);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case  Constants.DELETEPRODITEM:

                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", merchant_location_id);
                    data.put("admin_id", admin_id);
                    JSONObject categ = new JSONObject();
                    categ.put("id", productid);
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("product_id",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.ProductAPI,j_obj,Constants.DELETEPRODITEM,Constants.DELETE,extra);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    public void setReturnedData(int category, JSONObject j_obj, int posit) {
        switch (category){

            case Constants.UPDATEPRODITEM:
                try {
                    Log.e("Apitransect", "" + j_obj);
                    p_name.setText(d_product_name.getText().toString().trim());
                    p_description.setText(d_description.getText().toString().trim());
                    p_stock.setText(d_stock.getText().toString().trim());
                    product_sku.setText(d_sku.getText().toString().trim());
                    p_price.setText(d_price.getText().toString().trim());
                    Picasso.get().load(new File(croppedImagePath)).memoryPolicy(MemoryPolicy.NO_CACHE).into(p_img);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                ProductDashboardActivity.prod_deleted= true;


                break;
            case Constants.DELETEPRODITEM:
                Log.e("Apitransect",""+j_obj);
                ProductDashboardActivity.prod_deleted= true;
                finish();

                break;



        }
    }


    public void jsonwebrequest(String api_url , final JSONObject j_obj, final int action, final String action_text, final int posit) {
        if (util.checkInternetConnection()) {
            cd.show("");
            String url =  api_url;

            Log.e("url ", ""+url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                    new Response.Listener<String>() {
                        public JSONObject data;

                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", ""+response);
                            try {
                                if((new JSONObject(response)).getString("status").equalsIgnoreCase("1")) {

                                    JSONObject returnJSON =new JSONObject();
                                    if(!(new JSONObject(response)).isNull("data"))
                                        returnJSON = (new JSONObject(response)).getJSONObject("data");
                                    else
                                        returnJSON = (new JSONObject(response));

                                    if(!(new JSONObject(response)).isNull("message"))
                                        util.customToast((new JSONObject(response)).getString("message"));

                                    setReturnedData(action, returnJSON,posit);

                                }
                                else{
                                    cd.hide();
                                    util.customToast("Failed");
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(ProductDetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("data",j_obj.toString());
                    params.put("action",action_text);
                    Log.e("getParams", String.valueOf(params));
                    return params;
                }
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 100000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 100000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailActivity.this);
            requestQueue.add(stringRequest);
        }
        else
        {
            util.customToast(getResources().getString(R.string.nointernet));

        }
    }




}
