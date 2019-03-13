package com.yagna.petra.app.AdvncedCashier.Product;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.cunoraz.tagview.TagView;
import com.squareup.picasso.Picasso;
import com.yagna.petra.app.AdvncedCashier.AdvCashierActivity;
import com.yagna.petra.app.AdvncedCashier.DataKeeper;
import com.yagna.petra.app.AdvncedCashier.Search.SearchActivity;
import com.yagna.petra.app.Model.Product.Categories;
import com.yagna.petra.app.Model.Product.Products;
import com.yagna.petra.app.Model.Product.SubCategories;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Constants;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.PrefUtil;
import com.yagna.petra.app.Util.TouchImageView;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {
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
    public static ArrayList<Products.Product> product_array=new ArrayList<>();
    public TextView tv_no_record;
    private ProductAdapter adapter;
    private SharedPreferences common_mypref;
    private JSONObject returnedData;
    private Utils util;
    private CustomDialog cd;
    private TextView title,texttitle_product;
    private ProductActivity context;
    public int position;
    private JSONArray returnedImageData;
    public ArrayList<String[]> imageData;
    Uri myuri;
    private Uri imageToUploadUri;
    String imageid=null,productid=null;
    SimpleDateFormat sdf;
    String currentDateandTime;
    boolean imageupdate=false,flag=false;
    JSONArray j_arrayupdatearray;
    String productimagid[]=null;

    TextInputLayout txtxtitlep_discrption,txtxtitlep_name,txtxtitlep_code,txtxtitlep_price;
    TextView txt_producttitle;
    private LinearLayout ly_spinner;
    private TextView spn_category,spn_subcategory;
    private String admin_id;
    private String merchant_location_id;
    private ArrayList<Categories.Category> categories;
    private ArrayList<String> cate_array;
    private ArrayList<SubCategories.SubCategory> sub_categories;
    private ArrayList<String> sub_cate_array;
    private RecyclerView.LayoutManager storiesLayoutManager;
    private EditText p_sku;
    private String subcate_id;
    private String subcate_name;
    private String cate_id;
    private String cate_name;
    private TagView searchTagGroup;
    private LinearLayout ly_searchtag;
    private TextView tv_no_record_;
    public ArrayList<Integer> select_array= new ArrayList<>();
    private FloatingActionButton fab_cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // fillBitmapArrayList();
        //fillDataArraylist();
        context= ProductActivity.this;
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        util=new Utils(context);
        tv_no_record = (TextView) findViewById(R.id.tv_no_record_);
        util=new Utils(context);
        cd=new CustomDialog(context);
        txt_producttitle=(TextView)findViewById(R.id.txt_branchtitle);
        txt_producttitle.setText("Products");
        tv_no_record = (TextView) findViewById(R.id.tv_no_record_);
        ly_spinner = (LinearLayout) findViewById(R.id.ly_spinner);
        ly_spinner.setVisibility(View.VISIBLE);
        cate_id = (getIntent().getStringExtra("categ_id"));
        cate_name = (getIntent().getStringExtra("categ_name"));
        subcate_id = (getIntent().getStringExtra("subcateg_id"));
        subcate_name = (getIntent().getStringExtra("subcateg_name"));
        spn_category = (TextView) findViewById(R.id.spn_category);
        spn_category.setText(cate_name);
        spn_subcategory= (TextView) findViewById(R.id.spn_subcategory);
        spn_subcategory.setText(subcate_name);


        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            admin_id=jobj.getJSONObject("admin").getString("admin_id");
            merchant_location_id =jobj.getJSONObject("admin").getString("merchant_location_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bitmapArray=new ArrayList<>();
        product_array = new ArrayList<>();
        imageData = new ArrayList<>();
        listview=(RecyclerView) findViewById(R.id.listview);
        storiesLayoutManager = createLayoutManager(getResources());
        listview.setLayoutManager(storiesLayoutManager);
        fab_cart = (FloatingActionButton)findViewById(R.id.fab_cart);
        fab_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProductActivity.this,AdvCashierActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i);

            }
        });
        fillData(1);
        setupSearchTaglayout();
        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
      /*  listview.setAdapter(adapter = new ActionListAdpter(ProductActivity.this,product_array,imageData));
        listview.setVisibility(View.VISIBLE);
        tv_no_record.setVisibility(View.GONE);*/
    }
    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = resources.getInteger(R.integer.feed_columns);
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    private void fillData(int i){
        Log.e("called by",""+i);
        setUrlPayload(Constants.GETLIST,subcate_id);
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
        getMenuInflater().inflate( R.menu.menu_add, menu );
    //    MenuItem addOption = menu.findItem(R.id.add);
      //  addOption.setVisible(true);
        return true;
    }
    public void setUrlPayload(int category,String extra) {
        switch (category){
            
            case Constants.GETLIST:
                JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("category_id", extra);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonwebrequest(Constants.ProductAPI,j_obj,Constants.GETLIST,Constants.VIEW);
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
                    re_j_obj = returnedData.getJSONArray("products");
                    ArrayList<Products.Product> mContentItemss=new ArrayList<>();
                    select_array = new ArrayList<>();
                    for (int i= 0;i<re_j_obj.length();i++)
                        select_array.add(0);
                    if(product_array.size()==0){
                        product_array = (new Products(re_j_obj)).products;
                        listview.setAdapter(adapter = new ProductAdapter(ProductActivity.this,product_array,select_array));

                    }
                    else {
                        product_array.clear();
                        ArrayList<Products.Product> json = (new Products(re_j_obj)).products;
                        for(int i =0 ;i<json.size();i++){
                            product_array.add(json.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                    if(re_j_obj.length()!=0) {
                        txt_producttitle.setText("Products" + " (" + re_j_obj.length() + ")");
                        listview.setVisibility(View.VISIBLE);
                        tv_no_record.setVisibility(View.GONE);
                    }
                    else
                    {
                        txt_producttitle.setText("Products" + "(0)");
                        listview.setVisibility(View.GONE);
                        tv_no_record.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                                    if(!(new JSONObject(response)).isNull("data"))
                                        returnJSON = (new JSONObject(response)).getJSONObject("data");

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
                            Toast.makeText(ProductActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(ProductActivity.this);
            requestQueue.add(stringRequest);
        }
        else
        {
            util.customToast(getResources().getString(R.string.nointernet));

        }
    }



    public void showimage(int p){
        final Dialog Dialog = new Dialog(ProductActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.banner_full_image, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        rowView.setMinimumWidth((int)(displayRectangle.width() * 1f));
        rowView.setMinimumHeight((int)(displayRectangle.height() * 0.5f));
        TouchImageView imgview = (TouchImageView) rowView.findViewById(R.id.image_detail);
        if(product_array.get(position).image_full_path.trim().length()!=0)
        {
            Picasso.get()
                    .load(product_array.get(p).image_full_path.replace(" ","%20"))
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(imgview);
        }
        else{
            Picasso.get()
                    .load(R.mipmap.petra)
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(imgview);
        }


        imgview.setMaxZoom(4f);
      /*  Button backfullimg = (Button) rowView.findViewById(R.id.back_fullimg);
        backfullimg.setOnClickListener(new View.OnClickListener() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
        builder.setTitle("Add Product Image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=util.checkPermission(ProductActivity.this);
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
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
        startActivityForResult(chooserIntent, REQUEST_CAMERA);
    }

    public void myClickMethod(final View v) {
        // /Function purpose to Perform ClickEvent of Element Based on view
        // /PARAM 1.v=view of the clicked Element....
        switch (v.getId()) {

           /*
            case R.id.btnOkPopup:
                util.ButtonClickEffect(v);
                cd.hide();
                if(adapter!=null)
                    adapter.cd.hide();
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
                boolean result=util.checkPermissionwifi(ProductActivity.this);
                if(result) {
                    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    if (!wifi.isWifiEnabled()) {
                        wifi.setWifiEnabled(true);
                        cd.hide();
                    }
                }
                if(adapter!=null)
                    adapter.cd.hide();
                break;
              */

             /* case R.id.btnreconnect:
                util.ButtonClickEffect(v);
                if(adapter!=null)
                    adapter.cd.hide();
                cd.hide();
                setUrlPayload(Constants.DELETEITEM, Integer.parseInt(product_array.get(position)[1]));
                break*/

            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();
                if(adapter!=null)
                    adapter.cd.hide();
                break;
        }
    }

    private void setupSearchTaglayout() {
        searchTagGroup =(TagView)findViewById(R.id.tag_group);
        ly_searchtag=(LinearLayout) findViewById(R.id.ly_searchtag);
        searchTagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(com.cunoraz.tagview.Tag tag, int i) {

            }
        });
        //set delete listener
        searchTagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, com.cunoraz.tagview.Tag tag, int i) {
                searchTagGroup.remove(i);
                SearchActivity.searchTagGroup.remove(i);
                DataKeeper.product_name_selected.remove(i);
                DataKeeper.product_id_selected.remove(i);
                DataKeeper.products_array.remove(i);
                if(searchTagGroup.getChildCount()==0){
                    ly_searchtag.setVisibility(View.GONE);
                }
                else {
                    ly_searchtag.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });
        //set long click listener
        searchTagGroup.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(com.cunoraz.tagview.Tag tag, int i) {
            }
        });

        for(int i = 0;i<DataKeeper.product_name_selected.size();i++){
            com.cunoraz.tagview.Tag s_tag = new com.cunoraz.tagview.Tag(DataKeeper.product_name_selected.get(i));
            s_tag.isDeletable=true;
            s_tag.layoutColor = R.color.colorPrimaryDark;
            searchTagGroup.addTag(s_tag);
        }

    }

    public void addTag(Products.Product product){
        com.cunoraz.tagview.Tag s_tag = new com.cunoraz.tagview.Tag(product.title);
        //DataKeeper.product_id_selected.add(id);
        //DataKeeper.product_name_selected.add(id);
        s_tag.isDeletable=true;
        s_tag.layoutColor = R.color.colorPrimaryDark;
        searchTagGroup.addTag(s_tag);
        if(searchTagGroup.getChildCount()==0){
            ly_searchtag.setVisibility(View.GONE);
        }
        else {
            ly_searchtag.setVisibility(View.VISIBLE);
        }
        SearchActivity.addTag(product);

    }

}
