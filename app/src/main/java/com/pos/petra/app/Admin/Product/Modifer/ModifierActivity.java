package com.pos.petra.app.Admin.Product.Modifer;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.ImageCropper.FileUtils;
import com.pos.petra.app.ImageCropper.ImageCropActivity;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.Model.Product.Categories;
import com.pos.petra.app.Model.Product.Modifier;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
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

public class ModifierActivity extends GlobalActivity {
    public ArrayList<Bitmap> bitmapArray;
    private EditText product_name, p_price, p_description, p_stock;
    private ImageView p_image;
    private static final int CAMERA_REQUEST = 1888;
    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    private File lastCaptured;
    private Uri imageUri;
    public RecyclerView listview;
    int SELECT_FILE=2,REQUEST_CAMERA=3;
    public TextView tv_no_record;
    private ModifierAdapter adapter;
    private SharedPreferences common_mypref;
    private JSONObject returnedData;
    private Utils util;
    private CustomDialog cd;
    private TextView title,texttitle_product;
    private ModifierActivity context;
    public int position;
    private JSONArray returnedImageData;
    Uri myuri;
    private Uri imageToUploadUri;
    String imageid=null,productid=null;
    SimpleDateFormat sdf;
    String currentDateandTime;
    boolean imageupdate=false,flag=false;
    JSONArray j_arrayupdatearray;
    String productimagid[]=null;
    TextInputLayout txtxtitlep_discrption,txtxtitlep_name,txtxtitlep_code,txtxtitlep_price;
    TextView txt_producttitle,product_nam,product_sku;
    private LinearLayout ly_prod_details;
    private String admin_id;
    private String merchant_location_id;
    private ArrayList<Categories.Category> categories;
    private ArrayList<String> cate_array;
    private CardView cv_subcategory;
    private RecyclerView.LayoutManager storiesLayoutManager;
    private EditText p_type;
    private ArrayList<Modifier> modif_array;
    private CardView cv_sku;
    private TextInputLayout type;
    private ImageView btn_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // fillBitmapArrayList();
        //fillDataArraylist();
        context= ModifierActivity.this;
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        util=new Utils(context);
        tv_no_record = (TextView) findViewById(R.id.tv_no_record_);
        util=new Utils(context);
        cd=new CustomDialog(context);
        txt_producttitle=(TextView)findViewById(R.id.txt_branchtitle);
        txt_producttitle.setText("Modifier");
        tv_no_record = (TextView) findViewById(R.id.tv_no_record_);
        ly_prod_details = (LinearLayout) findViewById(R.id.ly_prod_details);
        ly_prod_details.setVisibility(View.VISIBLE);
        product_sku = (TextView) findViewById(R.id.product_sku);
        product_nam = (TextView) findViewById(R.id.product_nam);
        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            admin_id=jobj.getJSONObject("admin").getString("admin_id");
            merchant_location_id =jobj.getJSONObject("admin").getString("merchant_location_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        bitmapArray=new ArrayList<>();
        modif_array = new ArrayList<>();
        listview=(RecyclerView) findViewById(R.id.listview);
        storiesLayoutManager = createLayoutManager(getResources());
        listview.setLayoutManager(storiesLayoutManager);
        btn_add =findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(false,0);
            }
        });
        fillData(1);
        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
         /*  listview.setAdapter(adapter = new ActionListAdpter(DiscountActivity.this,modif_array,imageData));
             listview.setVisibility(View.VISIBLE);
             tv_no_record.setVisibility(View.GONE);*/
    }
    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = resources.getInteger(R.integer.feed_columns);
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    private void fillData(int i){
        Log.e("called by",""+i);
         String title =  DataKeeper.detail_product.title;
        product_nam.setText(title);
        product_sku.setText(DataKeeper.detail_product.sku);
         modif_array = DataKeeper.detail_product.modifier;
        listview.setAdapter(adapter = new ModifierAdapter(ModifierActivity.this, DataKeeper.detail_product.modifier));

        if(adapter==null) {
             if (modif_array.size() != 0) {
                 txt_producttitle.setText("Modifier" + " (" + modif_array.size() + ")");
                 listview.setVisibility(View.VISIBLE);
                 tv_no_record.setVisibility(View.GONE);
             } else {
                 txt_producttitle.setText("Modifier" + "(0)");
                 listview.setVisibility(View.GONE);
                 tv_no_record.setVisibility(View.VISIBLE);

             }
         }
         else{
             adapter.notifyDataSetChanged();
         }


    }
    public void addData(final boolean flag, final int position){

        final Dialog alertDialog = new Dialog(ModifierActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        final View rowView = (View) inflater.inflate(R.layout.dialog_add_modifier, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.translucent_black);
        product_name=(EditText) rowView.findViewById(R.id.p_name);
        p_price =(EditText) rowView.findViewById(R.id.p_price);
        p_stock =(EditText) rowView.findViewById(R.id.p_stock);
        p_description =(EditText) rowView.findViewById(R.id.p_discrption);
        p_image = (ImageView) rowView.findViewById(R.id.p_cropimage);
        p_type =(EditText) rowView.findViewById(R.id.p_sku);
        p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



        if(flag) {
            this.flag=true;
            if(modif_array.get(position).image_full_path.trim().length()!=0)
            {
                Picasso.get()
                        .load(modif_array.get(position).image_full_path.replace(" ","%20"))
                        .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(p_image);
            }
            else{
                Picasso.get()
                        .load(R.mipmap.petra)
                        .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(p_image );
            }
            p_stock.setText(modif_array.get(position).stock);
            p_type.setText(modif_array.get(position).type);
            p_stock.setSelection(p_stock.getText().length());
            product_name.setText(modif_array.get(position).title);
            product_name.setSelection(product_name.getText().length());
            p_price.setText(modif_array.get(position).price);
            p_price.setSelection(p_price.getText().length());
            p_description.setText(modif_array.get(position).description);
            p_description.setSelection(p_description.getText().length());
            productid= modif_array.get(position).id;
            p_type.setText(modif_array.get(position).type);

            try {
                JSONObject j_obj = new JSONObject();
                j_obj.put("ProductImageId", imageid);
                 j_arrayupdatearray=new JSONArray();
                j_arrayupdatearray.put(imageid);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        Button done = (Button) rowView.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/////////////////////////////////////////////////////
                Log.e("calling","validation");
                if(validation())
                {
                    Log.e("callied","validation");
                    if(flag){
                        setUrlPayload(Constants.UPDATEITEM,DataKeeper.detail_product.id);

                    }
                    else
                    {
                        setUrlPayload(Constants.ADDITEM,DataKeeper.detail_product.id);
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

        p_description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(validation())
                    {
                        Log.e("callied","validation");
                        if(flag){
                            setUrlPayload(Constants.UPDATEITEM,DataKeeper.detail_product.id);

                        }
                        else
                        {
                            setUrlPayload(Constants.ADDITEM,DataKeeper.detail_product.id);
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
                if(length>=307200) {
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
                    String croppedImagePath = bundle.getString(ImageCropActivity.EXTRA_IMAGE_PATH);
                    if(flag)
                    {
                        imageupdate=true;
                    }
                    myuri = Uri.parse(croppedImagePath);
                    Picasso.get().load(new File(croppedImagePath)).memoryPolicy(MemoryPolicy.NO_CACHE).into(p_image);
                }
            } else if (requestCode == REQUEST_CAMERA) {
                if (resultCode != RESULT_CANCELED) {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);

                    /*if (getPickImageResultUri(data) != null) {
                      //  myuri = getPickImageResultUri(data);
                        String copiedFilepath=null;
                        Uri croppedImage =  getPickImageResultUri(data);
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



                    } else {

                        try {
                            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                            p_image.setImageBitmap(bitmap);
                        }catch (Exception e ){
                            e.printStackTrace();
                        }

                    }*/



                    if (imageToUploadUri != null) {

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
                        .into(p_image);
            }
        }
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**Return image path of given URI*/

    private boolean validation(){
        boolean flag = true;
        /*if(myuri==null)
        {
            util.customToast("Please add your product's Image.");
            flag = false;
        }
        else*/ if(p_stock.getText().toString().trim().length()==0){
            // product_name.setError("Product Name should not be null!");
            util.customToast(getResources().getString(R.string.productcodenull));
            p_stock.requestFocus();
            flag = false;
        }
       else if(product_name.getText().toString().trim().length()==0){
           // product_name.setError("Product Name should not be null!");
            util.customToast(getResources().getString(R.string.productnamenull));
            product_name.requestFocus();
            flag = false;
        }
       else if (!(p_price.getText().toString().trim().length()>0)&& TextUtils.isDigitsOnly(p_price.getText().toString().trim())){
           // p_price.setError("Digit Only!");
            util.customToast(getResources().getString(R.string.productprice));
            p_price.requestFocus();

        }
        else if(p_description.getText().toString().trim().length()==0){
            util.customToast(getResources().getString(R.string.descriptionnull));
            p_description.requestFocus();
            flag = false;
        }
        return flag;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // addData(false, 0);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

      /*  int id = item.getItemId();
        if (id == R.id.add) {
            if (modif_array.size()==10){
                util.customToast("Sorry!!You can add Only 10 Products");
            }
            else {
                addData(false, 0);
            }
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate( R.menu.menu_add, menu );
      //  MenuItem addOption = menu.findItem(R.id.add);
       // addOption.setVisible(true);
        return true;
    }
    public void setUrlPayload(int category,String extra) {
        switch (category){

            case Constants.GETLIST:
                JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("product_id", extra);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonwebrequest(Constants.ModifierAPI,j_obj,Constants.GETLIST,Constants.VIEW);
                break;
            case  Constants.ADDITEM:
                Log.e("calliang","Add Product");
                //sendDataToServer(Constants.b_Add_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", merchant_location_id);
                    data.put("admin_id", admin_id);
                    JSONObject categ = new JSONObject();
                    categ.put("title", product_name.getText().toString().trim());
                    categ.put("product_id", extra);
                    categ.put("type", p_type.getText().toString().trim());
                    categ.put("description", p_description.getText().toString().trim());
                    categ.put("stock", p_stock.getText().toString().trim());
                    categ.put("sku", p_type.getText().toString().trim());
                    categ.put("price", p_price.getText().toString().trim());
                    categ.put("img", util.getBase64(p_image));
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("modifier",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.ModifierAPI,j_obj,Constants.ADDITEM,Constants.CREATE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case  Constants.UPDATEITEM:
                Log.e("calliang","Add Product");
                //sendDataToServer(Constants.b_Add_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", merchant_location_id);
                    data.put("admin_id", admin_id);
                    JSONObject categ = new JSONObject();
                    categ.put("id", productid);
                    categ.put("product_id",extra);
                    categ.put("type", p_type.getText().toString().trim());
                    categ.put("title", product_name.getText().toString().trim());
                    categ.put("description", p_description.getText().toString().trim());
                    categ.put("stock", p_stock.getText().toString().trim());
                    categ.put("sku", p_type.getText().toString().trim());
                    categ.put("price", p_price.getText().toString().trim());
                    categ.put("img", util.getBase64(p_image));
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("modifier",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.ModifierAPI,j_obj,Constants.UPDATEITEM,Constants.UPDATE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

            case  Constants.DELETEITEM:

                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", merchant_location_id);
                    data.put("admin_id", admin_id);
                    JSONObject categ = new JSONObject();
                    categ.put("id", productid);
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("modifier_id",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.ModifierAPI,j_obj,Constants.DELETEITEM,Constants.DELETE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }



    }

    public void setReturnedData(int category, JSONObject j_obj) {
        switch (category){

            case Constants.GETLIST:
                Log.e("Apitransect",""+j_obj);

                JSONArray re_j_obj;
                try {
                    returnedData =j_obj;
                    re_j_obj = returnedData.getJSONArray("data");

                    DataKeeper.detail_product.modifier.clear();
                    for(int i =0 ;i<re_j_obj.length();i++){
                        try {
                            DataKeeper.detail_product.modifier.add(new Modifier(re_j_obj.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                   fillData(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.ADDITEM:
                Log.e("Apitransect",""+j_obj);
                setUrlPayload(Constants.GETLIST, DataKeeper.detail_product.id);
                //fillData(2);
                break;
            case Constants.UPDATEITEM:
                Log.e("Apitransect",""+j_obj);
                setUrlPayload(Constants.GETLIST, DataKeeper.detail_product.id);
                break;
            case Constants.DELETEITEM:
                Log.e("Apitransect",""+j_obj);
                setUrlPayload(Constants.GETLIST, DataKeeper.detail_product.id);
                break;
        }
    }



    public void jsonwebrequest(String url ,final JSONObject j_obj, final int action, final String action_text) {
        if (util.checkInternetConnection()) {
            cd.show("");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        public JSONObject data;

                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", ""+response);
                            try {
                                if((new JSONObject(response)).getString("status").equalsIgnoreCase("1")) {

                                    JSONObject returnJSON =new JSONObject(response);

                                    if(!(new JSONObject(response)).isNull("message"))
                                        util.customToast((new JSONObject(response)).getString("message"));


                                    setReturnedData(action, returnJSON);

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
                            Toast.makeText(ModifierActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(ModifierActivity.this);
            requestQueue.add(stringRequest);
        }
        else
        {
            util.customToast(getResources().getString(R.string.nointernet));

        }
    }



    public void showimage(int p){
        final Dialog Dialog = new Dialog(ModifierActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.banner_full_image, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        rowView.setMinimumWidth((int)(displayRectangle.width() * 1f));
        rowView.setMinimumHeight((int)(displayRectangle.height() * 0.5f));
        TouchImageView image_full_pathview = (TouchImageView) rowView.findViewById(R.id.image_detail);
        if(modif_array.get(position).image_full_path.trim().length()!=0)
        {
            Picasso.get()
                    .load(modif_array.get(p).image_full_path.replace(" ","%20"))
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(image_full_pathview);
        }
        else{
            Picasso.get()
                    .load(R.mipmap.petra)
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(image_full_pathview);
        }


        image_full_pathview.setMaxZoom(4f);
      /*  Button backfullimage_full_path = (Button) rowView.findViewById(R.id.back_fullimage_full_path);
        backfullimage_full_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dismiss();
            }
        });*/
        Dialog.setContentView(rowView);
        Dialog.setCancelable(true);
        Dialog.setCanceledOnTouchOutside(true);
        Dialog.show();
    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ModifierActivity.this);
        builder.setTitle("Add Product Image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=util.checkPermission(ModifierActivity.this);
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
    private void captureCameraImage() {
        myuri=null;
        /*Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
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


    public void myClickMethod(final View v) {
        // /Function purpose to Perform ClickEvent of Element Based on view
        // /PARAM 1.v=view of the clicked Element....
        switch (v.getId()) {

/*
            case R.id.btnOkPopup:
                util.ButtonClickEffect(v);
                progressbar.hide();
                if(adapter!=null)
                    adapter.progressbar.hide();
                break;
*/
            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                setUrlPayload(Constants.DELETEITEM,productid);
                if(adapter!=null)
                    adapter.cd.hide();
                cd.hide();
                break;              /*

            case R.id.btnwifi:
                util.ButtonClickEffect(v);
                boolean result=util.checkPermissionwifi(DiscountActivity.this);
                if(result) {
                    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    if (!wifi.isWifiEnabled()) {
                        wifi.setWifiEnabled(true);
                        progressbar.hide();
                    }
                }
                if(adapter!=null)
                    adapter.progressbar.hide();
                break;
              */

         /*   case R.id.btnreconnect:
                util.ButtonClickEffect(v);
                if(adapter!=null)
                    adapter.progressbar.hide();
                progressbar.hide();
                setUrlPayload(Constants.DELETEITEM, Integer.parseInt(modif_array.get(position)[1]));
                break*/

            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();
                if(adapter!=null)
                    adapter.cd.hide();
                break;
        }
    }


}
