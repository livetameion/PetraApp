package com.pos.petra.app.Admin.Product.ProductDashboard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.pos.petra.app.Admin.Transaction.TransactionFragment;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.ImageCropper.FileUtils;
import com.pos.petra.app.ImageCropper.ImageCropActivity;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.Model.Product.Categories;
import com.pos.petra.app.Model.Product.Products;
import com.pos.petra.app.Model.Product.SubCategories;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.Utils;
import com.pos.petra.app.Views.TypefacedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;


/**
 * This activity controls the maint flow for the Custom Searchable behaviour. Its onCreate method
 * initialize the main UI components - such as the app bar and result list (RecyclerView). It should
 * be called through an intent and it's responses are also sent as intents
 */
public class ProductDashboardActivity extends GlobalActivity {
    // CONSTANTS
    private static final String TAG = "SearchActivity";
    public static final int VOICE_RECOGNITION_CODE = 15;
    public static boolean prod_deleted=false;
    public int[] selected_left={0,0,0,0};
    public String selected_cate_id="",selected_posit="";
    public String selected_subcate_id= "",selected_sub_posit="";
    public boolean isSeleteProduct;
    public boolean isdeleteProduct=false;

    // UI ELEMENTS
    private RecyclerView searchResultList;
    public AutoCompleteTextView searchInput;
    private RelativeLayout voiceInput;
    private RelativeLayout dismissDialog;
    private ImageView micIcon;

    private String query;
    private String providerName;
    private String providerAuthority;
    private String searchableActivity;
    private Boolean isRecentSuggestionsProvider = Boolean.TRUE;
    private RelativeLayout rl_bn_search;
    private SearchProductAdapter adapter;
    public SearchAdapter cat_adapter;

    private SharedPreferences common_mypref;
    private Utils util;
    public CustomDialog cd;
    public ArrayList<String> select_cat_array=new ArrayList<>();
    private String adminid ="",locationid="";
    private JSONObject returnedData;
    public static TagView searchTagGroup;
    private static LinearLayout ly_searchtag;
    private FloatingActionButton fab_cart;
    private RecyclerView.LayoutManager storiesLayoutManager;
    private boolean mTwoPane;
    public TypefacedTextView txt_add_new_categ,txt_all;

    private RecyclerView.LayoutManager subLayoutManager;
    public ArrayList<Categories.Category> cate_items=new ArrayList<>();
    private ProductFragment tr_fragment;
    int SELECT_FILE=2,REQUEST_CAMERA=3;
    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;

    private EditText categ_name, description;
    private Uri imageToUploadUri;
    String imageid=null,productid=null;
    private EditText p_code;
    public int position;
    public ArrayList<String[]> imageData;
    Uri myuri;
    String categ_id =null;
    SimpleDateFormat sdf;
    boolean flag=false;
    TextView txt_producttitle;
    private Categories.Category parent;
    private ImageView btn_add_product;
    private ImageView p_image;
    private EditText p_sku;
    private EditText product_name, p_price, p_description, p_stock;
    private boolean imageupdate=false;
    private NavigationView navigationView;
    private ImageView btn_menu;
    private DrawerLayout drawer;
    private final int REQUEST_PERMISSION_CODE=141;

    // Activity Callbacks __________________________________________________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mTwoPane = false;

        }
        else{
            mTwoPane = true;
        }
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.customise_product);
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        if (mTwoPane) {

            this.searchResultList = (RecyclerView) this.findViewById(R.id.cs_result_list);

        }
        else
        {

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Products");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            txt_add_new_categ = navigationView.findViewById(R.id.txt_add_new_categ);
            this.searchResultList = (RecyclerView) navigationView.findViewById(R.id.cs_result_list);

        }


        String[] str= PrefUtil.getSearch(common_mypref).split("<->");
        DataKeeper.old_tagsArray = new ArrayList<>();
        for(int i=0; i<str.length;i++)
        {   if(str[i].trim().length()!=0)
            DataKeeper.old_tagsArray.add(str[i]);
        }
        this.query = "";
        searchResultList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(ProductDashboardActivity.this);
            }
        });
        this.searchInput = (AutoCompleteTextView) this.findViewById(R.id.custombar_text);

       /* if( getIntent().getExtras().getString("arg").length()>0){
            this.searchInput.setText( getIntent().getExtras().getString("arg"));
            this.searchInput.setSelection( getIntent().getExtras().getString("arg").length());
        }*/
        common_mypref = getApplicationContext().getSharedPreferences("user", 0);
        util=new Utils(this);
        cd = new CustomDialog(this);
        ArrayAdapter<String> suggestAdapter = new ArrayAdapter<String>
                (this,R.layout.simple_list_item_1, DataKeeper.old_tagsArray);
        searchInput.setAdapter(suggestAdapter);
        this.voiceInput = (RelativeLayout) this.findViewById(R.id.custombar_mic_wrapper);
        this.rl_bn_search = (RelativeLayout) this.findViewById(R.id.rl_bn_search);

        this.dismissDialog = (RelativeLayout) this.findViewById(R.id.custombar_return_wrapper);
        this.micIcon = (ImageView) this.findViewById(R.id.custombar_mic);
        this.micIcon.setSelected(Boolean.FALSE);
        initializeUiConfiguration();
        // Initialize result list
        storiesLayoutManager = createLayoutManager(getResources());
        searchResultList.setLayoutManager(storiesLayoutManager);
        cat_adapter = new SearchAdapter(ProductDashboardActivity.this, cate_items,mTwoPane);
        searchResultList.setAdapter(cat_adapter);
        //adapter = new SearchAdapter(this,DataKeeper.old_tagsArray);
        //searchResultList.setAdapter(adapter);

        this.searchInput.setMaxLines(1);

        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            adminid =jobj.getJSONObject("admin").getString("admin_id");
            locationid =jobj.getJSONObject("admin").getString("merchant_location_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fab_cart = (FloatingActionButton)findViewById(R.id.fab_cart);
        btn_add_product = findViewById(R.id.btn_add_categ);
        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_cate_id.equalsIgnoreCase("")||selected_subcate_id.equalsIgnoreCase("")){
                    util.customToast("Please select Category & Subcategory first");
                }
                else{
                    showProductDialog(false,0);
                }


            }
        });

        fab_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        tr_fragment = new ProductFragment();
        Bundle arguments = new Bundle();
        arguments.putString(TransactionFragment.ARG_ITEM_ID, "");
        tr_fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, tr_fragment)
                .commit();
        txt_add_new_categ = findViewById(R.id.txt_add_new_categ);

        implementSearchTextListener();
        implementDismissListener();
        implementVoiceInputListener();
        txt_all = findViewById(R.id.txt_all);
        txt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataKeeper.search_product_array.clear();
                for(int i =0 ;i<DataKeeper.all_products_array.size();i++){
                    DataKeeper.search_product_array.add(DataKeeper.all_products_array.get(i));
                }
                txt_all.setTextColor(getResources().getColor(R.color.ink_blue));
                for (int j = 0; j < cate_items.size(); j++)
                {
                    cate_items.get(j).isSelected = false;
                    selected_cate_id="";
                    selected_subcate_id="";
                    selected_sub_posit= "";
                    selected_posit= "";

                    for (int i = 0; i < cate_items.get(j).subCategories.size(); i++) {
                        cate_items.get(j).subCategories.get(i).isSelected = false;

                    }
                }
                cat_adapter.notifyDataSetChanged();
                fillProducts("",true);


            }
        });
        txt_all.setTextColor(getResources().getColor(R.color.ink_blue));
        getAllProducts();

        txt_add_new_categ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategNewDialog(false,0);
            }
        });

        (findViewById(R.id.cv_add_new_categ)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategNewDialog(false,0);
            }
        });

        (findViewById(R.id. btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        // implementResultListOnItemClickListener();

        // getManifestConfig();
    }

    public void showAddCategNewDialog(final boolean flag, final int position){

        final Dialog alertDialog = new Dialog(ProductDashboardActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        final View rowView = (View) inflater.inflate(R.layout.dialog_add_category, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.translucent_black);
        categ_name =(EditText) rowView.findViewById(R.id.p_name);
        p_code =(EditText) rowView.findViewById(R.id.p_code);

        description =(EditText) rowView.findViewById(R.id.p_discrption);


        if(flag) {
            this.flag=true;
            categ_name.setText(cate_items.get(position).title);
            categ_name.setSelection(categ_name.getText().length());
            p_code.setText(cate_items.get(position).code);

            description.setText(cate_items.get(position).description);
            description.setSelection(description.getText().length());
            categ_id =cate_items.get(position).id;
        }
        Button done = (Button) rowView.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/////////////////////////////////////////////////////
                Log.e("calling","validation");
                if(validation())
                {
                    Log.e("called","validation");
                    if(flag){

                        setUrlPayload(Constants.UPDATEITEM,position);
                    }
                    else
                    {
                        setUrlPayload(Constants.ADDITEM,0);
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

        description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(validation())
                    {
                        Log.e("callied","validation");
                        if(flag){

                            setUrlPayload(Constants.UPDATEITEM,0);
                        }
                        else
                        {
                            setUrlPayload(Constants.ADDITEM,0);
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
    public void showAddSubCategNewDialog(final boolean flag,final int cat_position ,final int position){

        final Dialog alertDialog = new Dialog(ProductDashboardActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        final View rowView = (View) inflater.inflate(R.layout.dialog_add_category, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.translucent_black);
        categ_name =(EditText) rowView.findViewById(R.id.s_name);
        p_code =(EditText) rowView.findViewById(R.id.p_code);
        description =(EditText) rowView.findViewById(R.id.p_discrption);
        CardView ly_subcateg = (CardView) rowView.findViewById(R.id.ly_subcateg);
        ly_subcateg.setVisibility(View.VISIBLE);
        CardView ly_categ = (CardView) rowView.findViewById(R.id.ly_categ);
        ly_categ.setVisibility(View.GONE);

        ((TextView)rowView.findViewById(R.id.texttitle)).setText("Add/Edit Subcategory");
        ((TextView)rowView.findViewById(R.id.title_code)).setText("Subcategory Code");
        // ((TextInputLayout)rowView.findViewById(R.id.txtxtitlep_code)).setHint("SubCategory Code");
        parent=cate_items.get(cat_position);
        if(flag) {
            this.flag=true;
            categ_name.setText(cate_items.get(cat_position).subCategories.get(position).title);
            categ_name.setSelection(categ_name.getText().length());
            description.setText(cate_items.get(cat_position).subCategories.get(position).description);
            p_code.setText(cate_items.get(cat_position).subCategories.get(position).code);
            description.setSelection(description.getText().length());
            categ_id =cate_items.get(cat_position).subCategories.get(position).id;
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

                        setUrlPayload(Constants.UPDATESUBITEM, 0);
                    }
                    else
                    {
                        setUrlPayload(Constants.ADDSUBITEM,0);
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

        description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(validation())
                    {
                        Log.e("callied","validation");
                        if(flag){
                            setUrlPayload(Constants.UPDATESUBITEM,0);
                        }
                        else
                        {
                            setUrlPayload(Constants.ADDSUBITEM,0);
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

    public void showProductDialog(final boolean flag, final int position){

        final Dialog alertDialog = new Dialog(ProductDashboardActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        final View rowView = (View) inflater.inflate(R.layout.dialog_add_product, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.translucent_black);
        product_name=(EditText) rowView.findViewById(R.id.p_name);
        p_price =(EditText) rowView.findViewById(R.id.p_price);
        p_stock =(EditText) rowView.findViewById(R.id.p_stock);
        p_description =(EditText) rowView.findViewById(R.id.p_discrption);
        p_image = (ImageView) rowView.findViewById(R.id.p_cropimage);
        p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        p_sku = (EditText) rowView.findViewById(R.id.p_sku);
        searchTagGroup =(TagView)rowView.findViewById(R.id.tag_group);

        if(flag) {
            this.flag=true;
            if(DataKeeper.search_product_array.get(position).image_full_path.trim().length()!=0)
            {
                Picasso.get()
                        .load(DataKeeper.search_product_array.get(position).image_full_path.replace(" ","%20"))
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

            p_sku.setText(DataKeeper.search_product_array.get(position).sku);
            p_stock.setText(DataKeeper.search_product_array.get(position).stock);
            p_stock.setSelection(p_stock.getText().length());
            product_name.setText(DataKeeper.search_product_array.get(position).title);
            product_name.setSelection(product_name.getText().length());
            p_price.setText(DataKeeper.search_product_array.get(position).price);
            p_price.setSelection(p_price.getText().length());
            p_description.setText(DataKeeper.search_product_array.get(position).description);
            p_description.setSelection(p_description.getText().length());
            productid= DataKeeper.search_product_array.get(position).id;
            categ_id = DataKeeper.search_product_array.get(position).category;
        }

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

        p_description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
    private boolean validation(){
        boolean flag = true;

        if(categ_name.getText().toString().trim().length()==0){
            // categ_name.setError("Product Name should not be null!");
            util.customToast(getResources().getString(R.string.productnamenull));
            categ_name.requestFocus();
            flag = false;
        }

        else if(description.getText().toString().trim().length()==0){
            util.customToast(getResources().getString(R.string.descriptionnull));
            description.requestFocus();
            flag = false;
        }
        return flag;
    }

    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = 1;/*resources.getInteger(R.integer.feed_columns);*/
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }
    private void getAllProducts(){

        setUrlPayload(Constants.GETLIST_3,0);
    }
    private void fillCategories(int i){
        Log.e("called by",""+i);
        setUrlPayload(Constants.GETLIST,0);
    }
    public void fillSubCategories(int i){
        Log.e("called by",""+i);
        setUrlPayload(Constants.GETLIST_2,i);
    }
    public void ProdsAndSubCategories(int i){
        Log.e("called by",""+i);
        setUrlPayload(Constants.GETLIST_1,i);
    }
    // Receives the intent with the speech-to-text information and sets it to the InputText
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VOICE_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchInput.setText(text.get(0));
                }
                break;
            }
        }

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
                        .into(p_image);
            }
        }
    }



    // Sends an intent with the typed query to the searchable Activity
    private void sendSearchIntent(String trim) {
        boolean wannAdd=true;
        for(int i = 0; i<DataKeeper.old_tagsArray.size(); i++){
            if(DataKeeper.old_tagsArray.get(i).equalsIgnoreCase(trim)){
                wannAdd=false;
                break;
            }
        }
        if(wannAdd)
            DataKeeper.old_tagsArray.add(trim);
        saveSearchData();

        //CashierDashboardActivity.isSearch=true;
        if(searchInput.getText().toString().trim().length()!=0)
            // if(mTwoPane)
            //  {
            tr_fragment.callFreeSearch(searchInput.getText().toString(),locationid);

        // }
           /* else{
                Intent intent=new Intent(SearchActivity.this, ProductSingleViewActivity.class);
                intent.putExtra(ProductFragment.ARG_SEARCH, ""+searchInput.getText().toString());
                intent.putExtra(ProductFragment.LOC_ID, locationid);


                startActivity(intent);
            }*/



        //finish();
      /* try {
            Intent sendIntent = new Intent(this, Class.forName(searchableActivity));
            sendIntent.setAction(Intent.ACTION_SEARCH);
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            sendIntent.putExtra(SearchManager.QUERY, query);

            // If it is set one-line mode, directly saves the suggestion in the provider
            if (!CustomSearchableInfo.getIsTwoLineExhibition()) {
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, providerAuthority, SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES);
                suggestions.saveRecentQuery(query, null);
            }

            startActivity(sendIntent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } */
    }



    public void saveSearchData() {
        String saveme="";
        for(int i = 0; i<DataKeeper.old_tagsArray.size(); i++)
            saveme=i==0?DataKeeper.old_tagsArray.get(i):saveme+"<->"+DataKeeper.old_tagsArray.get(i);
        PrefUtil.saveSearch(common_mypref,saveme);
    }

    // Listeners implementation ____________________________________________________________________
    private void implementSearchTextListener() {
        // Gets the event of pressing search button on soft keyboard
        TextView.OnEditorActionListener searchListener = new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    sendSearchIntent(exampleView.getText().toString().trim());
                }
                return true;
            }
        };
        rl_bn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSearchIntent(searchInput.getText().toString().trim());

            }
        });

        searchInput.setOnEditorActionListener(searchListener);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (!"".equals(searchInput.getText().toString())) {
                    query = searchInput.getText().toString();

                    setClearTextIcon();
                    /*if (isRecentSuggestionsProvider) {
                        // Provider is descendant of SearchRecentSuggestionsProvider
                       // mapResultsFromRecentProviderToList();
                    } else {
                        // Provider is custom and shall follow the contract
                        mapResultsFromCustomProviderToList();
                    }*/
                } else {
                    setMicIcon();
                   /* getAllProducts();
                    selected_cate_id= "";
                    selected_subcate_id="";*/
                    for(int k =0 ;k<DataKeeper.all_products_array.size();k++){
                        try {
                            DataKeeper.search_product_array.add((Products.Product)DataKeeper.all_products_array.get(k).clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    fillProducts("",true);
                    txt_all.setTextColor(getResources().getColor(R.color.ink_blue));
                    selected_cate_id="";
                    selected_posit= "";
                }
            }

            // DO NOTHING
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            // DO NOTHING
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    // Finishes this activity and goes back to the caller
    private void implementDismissListener () {
        this.dismissDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Implements speech-to-text
    private void implementVoiceInputListener () {
        this.voiceInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (micIcon.isSelected()) {
                    searchInput.setText("");
                    query = "";
                    setReturnedData(Constants.GETLIST,returnedData, 0);
                    micIcon.setSelected(Boolean.FALSE);
                    micIcon.setImageResource(R.drawable.ic_mic_blue);
                } else {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");

                    ProductDashboardActivity.this.startActivityForResult(intent, VOICE_RECOGNITION_CODE);
                }
            }
        });
    }

    // Sends intent to searchableActivity with the selected result item
    private void implementResultListOnItemClickListener () {
        searchResultList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(this,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String clickedItem = DataKeeper.old_tagsArray.get(position);
                        sendSearchIntent(clickedItem);
                    }
                }));
    }


    // UI __________________________________________________________________________________________
    // Identifies if client have set any of the configuration attributes, if yes, reset UI element source, toherwise keep defaul value
    private void initializeUiConfiguration () {
        // Set activity background transparency
        if (CustomSearchableInfo.getTransparencyColor() != CustomSearchableConstants.UNSET_RESOURCES) {
            LinearLayout activityWrapper = (LinearLayout) this.findViewById(R.id.custom_searchable_wrapper);
            activityWrapper.setBackgroundColor(CustomSearchableInfo.getTransparencyColor());
        }

        if (CustomSearchableInfo.getPrimaryColor() != CustomSearchableConstants.UNSET_RESOURCES) {
            RelativeLayout headerWrapper = (RelativeLayout) this.findViewById(R.id.cs_header);
            headerWrapper.setBackgroundColor(CustomSearchableInfo.getPrimaryColor());
        }

        if (CustomSearchableInfo.getSearchTextSize() != CustomSearchableConstants.UNSET_RESOURCES) {
            searchInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, CustomSearchableInfo.getSearchTextSize());
        }

        if (CustomSearchableInfo.getTextPrimaryColor() != CustomSearchableConstants.UNSET_RESOURCES) {
            searchInput.setTextColor(CustomSearchableInfo.getTextPrimaryColor());
        }

        if (CustomSearchableInfo.getTextHintColor() != CustomSearchableConstants.UNSET_RESOURCES) {
            searchInput.setHintTextColor(CustomSearchableInfo.getTextHintColor());
        }

        if (CustomSearchableInfo.getBarDismissIcon() != CustomSearchableConstants.UNSET_RESOURCES) {
            ImageView dismissIcon = (ImageView) this.findViewById(R.id.custombar_return);
            dismissIcon.setImageResource(CustomSearchableInfo.getBarDismissIcon());
        }

        if (CustomSearchableInfo.getBarMicIcon() != CustomSearchableConstants.UNSET_RESOURCES) {
            ImageView micIcon = (ImageView) this.findViewById(R.id.custombar_mic);
            micIcon.setImageResource(CustomSearchableInfo.getBarMicIcon());
        }

        if (CustomSearchableInfo.getBarHeight() != CustomSearchableConstants.UNSET_RESOURCES) {
            RelativeLayout custombar = (RelativeLayout) this.findViewById(R.id.cs_header);
            android.view.ViewGroup.LayoutParams params = custombar.getLayoutParams();
            params.height = CustomSearchableInfo.getBarHeight().intValue();
            custombar.setLayoutParams(params);
        }
    }

    // Set X as the icon for the right icon in the app bar
    private void setClearTextIcon () {
        micIcon.setSelected(Boolean.TRUE);
        micIcon.setImageResource(R.drawable.delete_icon);
        micIcon.invalidate();
    }

    // Set the micrphone icon as the right icon in the app bar
    private void setMicIcon () {
        micIcon.setSelected(Boolean.FALSE);
        micIcon.setImageResource(R.drawable.ic_mic_blue);
        micIcon.invalidate();
    }


    public void setUrlPayload(int action,int extra) {
        switch (action){

            case Constants.GETLIST:
                JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("merchant_location_id", locationid);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonwebrequest(Constants.CategoryAPI,j_obj,Constants.GETLIST,Constants.VIEW,0);
                break;

            case Constants.GETLIST_1:
                j_obj = new JSONObject();
                try {
                    j_obj.put("category_id", cate_items.get(extra).id );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonwebrequest(Constants.CategoryAPI,j_obj,Constants.GETLIST_1,Constants.VIEW_SUB,extra);
                break;
            case Constants.GETLIST_2:
                j_obj = new JSONObject();
                try {
                    j_obj.put("category_id", cate_items.get(extra).id );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonwebrequest(Constants.CategoryAPI,j_obj,Constants.GETLIST_2,Constants.VIEW_SUB,extra);
                break;

            case Constants.GETLIST_3:
                j_obj = new JSONObject();
                try {
                    j_obj.put("merchant_location_id", locationid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonwebrequest(Constants.ProductAPI,j_obj,Constants.GETLIST_3,Constants.VIEW_All,0);
                break;

            case  Constants.ADDITEM:

                //sendDataToServer(Constants.b_Add_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", locationid);
                    data.put("admin_id", adminid);
                    JSONObject categ = new JSONObject();
                    categ.put("title", categ_name.getText().toString().trim());
                    categ.put("code", p_code.getText().toString().trim());
                    categ.put("parent", "0");
                    categ.put("description", description.getText().toString().trim());
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("categories",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.CategoryAPI,j_obj,Constants.ADDITEM,Constants.CREATE,0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case  Constants.UPDATEITEM:
                //  sendDataToServer(Constants.b_Update_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", locationid);
                    data.put("admin_id", adminid);
                    JSONObject categ = new JSONObject();
                    categ.put("id", categ_id);
                    categ.put("code", p_code.getText().toString().trim());
                    categ.put("parent", "0");
                    categ.put("title", categ_name.getText().toString().trim());
                    categ.put("description", description.getText().toString().trim());
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("categories",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.CategoryAPI,j_obj,Constants.UPDATEITEM,Constants.UPDATE,extra);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case  Constants.DELETEITEM:
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", locationid);
                    data.put("admin_id", adminid);
                    JSONObject categ = new JSONObject();
                    categ.put("id", categ_id);
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("categories",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.CategoryAPI,j_obj,Constants.DELETEITEM,Constants.DELETE,extra);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case  Constants.ADDSUBITEM:
                //sendDataToServer(Constants.b_Add_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", locationid);
                    data.put("admin_id", adminid);
                    JSONObject categ = new JSONObject();
                    categ.put("title", categ_name.getText().toString().trim());
                    categ.put("parent_id", parent.id);
                    categ.put("code",  p_code.getText().toString().trim());
                    categ.put("description", description.getText().toString().trim());
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("sub_categories",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.CategoryAPI,j_obj,Constants.ADDSUBITEM,Constants.CREATE,extra);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case  Constants.UPDATESUBITEM:
                //  sendDataToServer(Constants.b_Update_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", locationid);
                    data.put("admin_id", adminid);
                    JSONObject categ = new JSONObject();
                    categ.put("id", categ_id);
                    categ.put("code",  p_code.getText().toString().trim());
                    categ.put("title", categ_name.getText().toString().trim());
                    categ.put("description", description.getText().toString().trim());
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("sub_categories",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.CategoryAPI,j_obj,Constants.UPDATESUBITEM,Constants.UPDATE,0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case  Constants.ADDPRODITEM:
                Log.e("calliang","Add Product");
                //sendDataToServer(Constants.b_Add_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id",locationid);
                    data.put("admin_id", adminid);
                    JSONObject categ = new JSONObject();
                    categ.put("title", product_name.getText().toString().trim());
                    categ.put("description", p_description.getText().toString().trim());

                    categ.put("category",selected_subcate_id);

                    categ.put("stock", p_stock.getText().toString().trim());
                    categ.put("sku", p_sku.getText().toString().trim());
                    categ.put("price", p_price.getText().toString().trim());
                    categ.put("img", util.getBase64(p_image));
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("products",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.ProductAPI,j_obj,Constants.ADDPRODITEM,Constants.CREATE,extra);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case  Constants.UPDATEPRODITEM:
                Log.e("calliang","Add Product");
                //sendDataToServer(Constants.b_Add_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", locationid);
                    data.put("admin_id", adminid);
                    JSONObject categ = new JSONObject();
                    categ.put("id", productid);
                    categ.put("title", product_name.getText().toString().trim());
                    categ.put("description", p_description.getText().toString().trim());
                    categ.put("category",categ_id );
                    categ.put("stock", p_stock.getText().toString().trim());
                    categ.put("sku", p_sku.getText().toString().trim());
                    categ.put("price", p_price.getText().toString().trim());
                    categ.put("img", util.getBase64(p_image));
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
                    data.put("merchant_location_id", locationid);
                    data.put("admin_id", adminid);
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
            case Constants.GETLIST:
                Log.e("Apitransect",""+j_obj);
                JSONArray re_j_obj;
                try {
                    returnedData =j_obj;
                    re_j_obj = returnedData.getJSONArray("categories");
                    cate_items.clear();
                    for(int i = 0;i<re_j_obj.length();i++){
                        cate_items.add(new Categories(re_j_obj).categories.get(i));
                    }
                    cat_adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cd.hide();
                break;

            case Constants.GETLIST_1:
                Log.e("Apitransect",""+j_obj);
                cd.hide();
                try {
                    returnedData =j_obj;
                    re_j_obj = returnedData.getJSONArray("sub_categories");
                    ArrayList<SubCategories.SubCategory> sub_categories=(new SubCategories(re_j_obj)).sub_categories;
                    cate_items.get(posit).subCategories.clear();
                    for(int i =0 ;i<sub_categories.size();i++) {
                        cate_items.get(posit).subCategories.add(sub_categories.get(i));
                    }
                    cat_adapter.notifyDataSetChanged();

                    try{
                        DataKeeper.search_product_array.clear();
                        re_j_obj = returnedData.getJSONArray("products");
                        ArrayList<Products.Product> json = (new Products(re_j_obj)).products;
                        Collections.reverse(json);
                        for(int i =0 ;i<json.size();i++){
                            DataKeeper.search_product_array.add(json.get(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    fillProducts("",true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.GETLIST_2:
                Log.e("Apitransect",""+j_obj);
                cd.hide();
                try {
                    returnedData =j_obj;
                    re_j_obj = returnedData.getJSONArray("sub_categories");
                    ArrayList<SubCategories.SubCategory> sub_categories=(new SubCategories(re_j_obj)).sub_categories;
                    cate_items.get(posit).subCategories = sub_categories;
                    cat_adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.GETLIST_3:
                Log.e("Apitransect",""+j_obj);
                cd.hide();
                try {
                    returnedData =j_obj;
                    re_j_obj = returnedData.getJSONArray("products");
                    DataKeeper.search_product_array.clear();
                    ArrayList<Products.Product> data = (new Products(re_j_obj)).products;
                    Collections.reverse(data);
                    DataKeeper.all_products_array.clear();
                    for(int i =0 ;i<data.size();i++){
                        DataKeeper.all_products_array.add((Products.Product)data.get(i).clone());
                        DataKeeper.search_product_array.add((Products.Product)data.get(i).clone());
                    }
                    fillProducts("",true);
                    fillCategories(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.ADDITEM:
                Log.e("Apitransect",""+j_obj);
                fillCategories(2);
                break;
            case Constants.UPDATEITEM:
                Log.e("Apitransect",""+j_obj);
                fillCategories(3);
                break;
            case Constants.DELETEITEM:
                Log.e("Apitransect",""+j_obj);
                fillCategories(4);
                break;
            case Constants.ADDSUBITEM:
                Log.e("Apitransect",""+j_obj);
                fillCategories(5);
                break;
            case Constants.UPDATESUBITEM:
                Log.e("Apitransect",""+j_obj);
                fillCategories(6);
                break;
            case Constants.DELETESUBITEM:
                Log.e("Apitransect",""+j_obj);
                fillCategories(7);
                break;
            case Constants.ADDPRODITEM:
                Log.e("Apitransect",""+j_obj);
                fillProducts(selected_subcate_id,false);
                break;
            case Constants.UPDATEPRODITEM:
                Log.e("Apitransect",""+j_obj);
               refreshPage();

                break;
            case Constants.DELETEPRODITEM:
                Log.e("Apitransect",""+j_obj);
                refreshPage();

                break;



        }
    }

    private void refreshPage() {
        if(!selected_subcate_id.trim().equalsIgnoreCase("")){
            fillProducts(selected_subcate_id,false);

        }
        else if(!selected_cate_id.trim().equalsIgnoreCase("")){
            ProdsAndSubCategories(Integer.parseInt(selected_posit));
        }
        else {
            getAllProducts();
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
                            Toast.makeText(ProductDashboardActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(ProductDashboardActivity.this);
            requestQueue.add(stringRequest);
        }
        else
        {
            util.customToast(getResources().getString(R.string.nointernet));

        }
    }

    private void setupSearchTaglayout() {
        select_cat_array.clear();
        ly_searchtag=(LinearLayout) findViewById(R.id.ly_searchtag);
        searchTagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int i) {

            }
        });
        //set delete listener
        searchTagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, Tag tag, int i) {
                searchTagGroup.remove(i);
                select_cat_array.remove(i);

            }
        });
        //set long click listener
        searchTagGroup.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(Tag tag, int i) {
            }
        });

        ly_searchtag.setVisibility(View.VISIBLE);

    }

    public void addTag(String id){
        select_cat_array.add(0,id);
        List <Tag> tagiis = new ArrayList<>();
        searchTagGroup.removeAll();
        for(int i=0;i<select_cat_array.size();i++ ){
            Tag s_tag = new Tag(select_cat_array.get(i));
            s_tag.isDeletable=true;
            s_tag.layoutColor = R.color.colorPrimaryDark;
            tagiis.add(s_tag);
        }
        searchTagGroup.addTags(tagiis);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(prod_deleted)
        {
            prod_deleted=false;
            getAllProducts();
        }
    }

    public void dialogAsk(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        builder.setMessage("Are you sure want to remove this search filter?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchTagGroup.removeAll();
                //String scieTag = storiesPagerAdapter.getTag( headersPager.getCurrentItem());
                //     String tag = storiesPagerAdapter.getTag(0);
                //   ScienceFragment fragment = (ScienceFragment) getSupportFragmentManager().findFragmentByTag(tag);
                // ScienceFragment fragment = (ScienceFragment) storiesPagerAdapter.getItem(0);

            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }




    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProductDashboardActivity.this);
        builder.setTitle("Add Product Image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=util.checkPermission(ProductDashboardActivity.this);
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE);

            } else {

                myuri = null;
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
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void fillProducts(String id,boolean isSaved) {
        // if(mTwoPane)
        {
            tr_fragment.fillData(id, isSaved);
        }
           /* else{
                    Intent intent=new Intent(SearchActivity.this, ProductSingleViewActivity.class);
                    intent.putExtra(ProductFragment.ARG_ITEM_ID, ""+id);
                     intent.putExtra(ProductFragment.ARG_TYPE, ""+isSaved);

                startActivity(intent);
           }*/
    }

    public void myClickMethod(final View v) {

        switch (v.getId()) {
            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                if(isdeleteProduct){
                    setUrlPayload(Constants.DELETEPRODITEM,0);
                    isdeleteProduct=false;
                }
                else
                {
                    setUrlPayload(Constants.DELETEITEM,0);
                }
                if(cat_adapter!=null)
                    cat_adapter.cd.hide();

                cd.hide();
                break;

            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();
                if(cat_adapter!=null)
                    cat_adapter.cd.hide();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted)
                        util.customToast("Permission Granted, Now you can access camera. Try Again");
                    else {

                        util.customToast("Permission Denied, You cannot access camera.");

                                            }
                }


                break;
        }
    }

}

