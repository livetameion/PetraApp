package com.pos.petra.app.AdvancedCashier.BrowseAndSearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.pos.petra.app.Admin.Transaction.TransactionFragment;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.Model.Roles.Cashiers;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchActivity extends GlobalActivity {
    private static final String TAG = "SearchActivity";
    public static final int VOICE_RECOGNITION_CODE = 15;
    public String selected_cate_id = "";
    public String selected_subcate_id = "";
    public boolean isdeleteProduct = false;
    private RecyclerView searchResultList;
    public AutoCompleteTextView searchInput;
    private RelativeLayout voiceInput;
    private RelativeLayout dismissDialog;
    private ImageView micIcon;
    private RelativeLayout rl_bn_search;
    public SearchAdapter cat_adapter;
    private SharedPreferences common_mypref;
    private Utils util;
    private CustomDialog cd;
    private String locationid = "";
    private JSONObject returnedData;
    public static TagView searchTagGroup;
    private static LinearLayout ly_searchtag;
    private FloatingActionButton fab_cart;
    private RecyclerView.LayoutManager layoutManager;
    private boolean mTwoPane;
    public TypefacedTextView txt_add_new_categ, txt_all;
    private RecyclerView.LayoutManager subLayoutManager;
    public ArrayList<Categories.Category> cate_items = new ArrayList<>();
    private ProductFragment tr_fragment;
    public int position;
    public ArrayList<String[]> imageData;
    private ImageView btn_add_categ;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    // Activity Callbacks __________________________________________________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mTwoPane = false;

        } else {
            mTwoPane = true;
        }
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.customise_product);
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        if (mTwoPane) {

            this.searchResultList = this.findViewById(R.id.cs_result_list);

        } else {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Products");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            drawer =  findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView = findViewById(R.id.nav_view);
            txt_add_new_categ = navigationView.findViewById(R.id.txt_add_new_categ);
            this.searchResultList = navigationView.findViewById(R.id.cs_result_list);
        }
        String[] str = PrefUtil.getSearch(common_mypref).split("<->");
        DataKeeper.old_tagsArray = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            if (str[i].trim().length() != 0)
                DataKeeper.old_tagsArray.add(str[i]);
        }
        searchResultList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(SearchActivity.this);
            }
        });
        this.searchInput = this.findViewById(R.id.custombar_text);
        common_mypref = getApplicationContext().getSharedPreferences("user", 0);
        util = new Utils(this);
        cd = new CustomDialog(this);
        ArrayAdapter<String> suggestAdapter = new ArrayAdapter<String>
                (this, R.layout.simple_list_item_1, DataKeeper.old_tagsArray);
        searchInput.setAdapter(suggestAdapter);
        this.voiceInput =  this.findViewById(R.id.custombar_mic_wrapper);
        this.rl_bn_search =  this.findViewById(R.id.rl_bn_search);
        this.dismissDialog =  this.findViewById(R.id.custombar_return_wrapper);
        this.micIcon =  this.findViewById(R.id.custombar_mic);
        this.micIcon.setSelected(Boolean.FALSE);
        initializeUiConfiguration();
        layoutManager = createLayoutManager();
        searchResultList.setLayoutManager(layoutManager);
        cat_adapter = new SearchAdapter(SearchActivity.this, cate_items, mTwoPane);
        searchResultList.setAdapter(cat_adapter);
        setupSearchTaglayout();
        this.searchInput.setMaxLines(1);
        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            Cashiers cashier = new Cashiers(jobj.getJSONObject("cashier"));
            locationid = cashier.merchant_location_id;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        fab_cart = (FloatingActionButton) findViewById(R.id.fab_cart);
        btn_add_categ = findViewById(R.id.btn_add_categ);
        btn_add_categ.setVisibility(View.GONE);
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
        (findViewById(R.id.cv_add_new_categ)).setVisibility(View.GONE);

        (findViewById(R.id.rl_add_categ)).setVisibility(View.GONE);

        implementSearchTextListener();
        implementDismissListener();
        implementVoiceInputListener();
        txt_all = findViewById(R.id.txt_all);
        txt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataKeeper.search_product_array.clear();
                for (int i = 0; i < DataKeeper.all_products_array.size(); i++) {
                    DataKeeper.search_product_array.add(DataKeeper.all_products_array.get(i));
                }
                txt_all.setTextColor(getResources().getColor(R.color.ink_blue));
                for (int j = 0; j < cate_items.size(); j++) {
                    cate_items.get(j).isSelected = false;
                    selected_cate_id = "";
                    selected_subcate_id = "";

                    for (int i = 0; i < cate_items.get(j).subCategories.size(); i++) {
                        cate_items.get(j).subCategories.get(i).isSelected = false;

                    }
                }
                cat_adapter.notifyDataSetChanged();
                fillProducts("", true);
            }
        });
        txt_all.setTextColor(getResources().getColor(R.color.ink_blue));

        getAllProducts();


    }


    private RecyclerView.LayoutManager createLayoutManager() {
        int spans = 1;/*resources.getInteger(R.integer.feed_columns);*/
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }


    //Load Products
    private void getAllProducts() {

        setUrlPayload(Constants.GETLIST_3, 0);
    }

    //Load categories
    private void fillCategories(int i) {
        Log.e("called by", "" + i);
        setUrlPayload(Constants.GETLIST, 0);
    }

    public void fillSubCategories(int i) {
        Log.e("called by", "" + i);
        setUrlPayload(Constants.GETLIST_2, i);
    }

    //Load All categories - subcategories - products
    public void ProdsAndSubCategories(int i) {
        Log.e("called by", "" + i);
        setUrlPayload(Constants.GETLIST_1, i);
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

    }


    // Sends an intent with the typed query to the searchable Activity
    private void sendSearchIntent(String trim) {
        boolean wannAdd = true;
        for (int i = 0; i < DataKeeper.old_tagsArray.size(); i++) {
            if (DataKeeper.old_tagsArray.get(i).equalsIgnoreCase(trim)) {
                wannAdd = false;
                break;
            }
        }
        if (wannAdd)
            DataKeeper.old_tagsArray.add(trim);
        saveSearchData();

        //CashierDashboardActivity.isSearch=true;
        if (searchInput.getText().toString().trim().length() != 0)
            tr_fragment.callFreeSearchApi(searchInput.getText().toString(), locationid);
    }

    // save the serched keyword
    public void saveSearchData() {
        String saveme = "";
        for (int i = 0; i < DataKeeper.old_tagsArray.size(); i++)
            saveme = i == 0 ? DataKeeper.old_tagsArray.get(i) : saveme + "<->" + DataKeeper.old_tagsArray.get(i);
        PrefUtil.saveSearch(common_mypref, saveme);
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


                    setClearTextIcon();

                } else {
                    setMicIcon();
                    //setReturnedData(Constants.GETLIST,returnedData, 0);
                    getAllProducts();
                    txt_all.setTextColor(getResources().getColor(R.color.ink_blue));
                    selected_cate_id = "";

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
    private void implementDismissListener() {
        this.dismissDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Implements speech-to-text
    private void implementVoiceInputListener() {
        this.voiceInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (micIcon.isSelected()) {
                    searchInput.setText("");
                    setReturnedData(Constants.GETLIST, returnedData, 0);
                    micIcon.setSelected(Boolean.FALSE);
                    micIcon.setImageResource(R.drawable.ic_mic_blue);
                } else {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");

                    SearchActivity.this.startActivityForResult(intent, VOICE_RECOGNITION_CODE);
                }
            }
        });
    }


    // UI __________________________________________________________________________________________
    // Identifies if client have set any of the configuration attributes, if yes, reset UI element source, toherwise keep defaul value
    private void initializeUiConfiguration() {
        // Set activity background transparency
        if (CustomSearchableInfo.getTransparencyColor() != CustomSearchableConstants.UNSET_RESOURCES) {
            LinearLayout activityWrapper = (LinearLayout) this.findViewById(R.id.custom_searchable_wrapper);
            activityWrapper.setBackgroundColor(CustomSearchableInfo.getTransparencyColor());
        }

        if (CustomSearchableInfo.getPrimaryColor() != CustomSearchableConstants.UNSET_RESOURCES) {
            RelativeLayout headerWrapper =  this.findViewById(R.id.cs_header);
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
            ImageView dismissIcon =  this.findViewById(R.id.custombar_return);
            dismissIcon.setImageResource(CustomSearchableInfo.getBarDismissIcon());
        }

        if (CustomSearchableInfo.getBarMicIcon() != CustomSearchableConstants.UNSET_RESOURCES) {
            ImageView micIcon =  this.findViewById(R.id.custombar_mic);
            micIcon.setImageResource(CustomSearchableInfo.getBarMicIcon());
        }

        if (CustomSearchableInfo.getBarHeight() != CustomSearchableConstants.UNSET_RESOURCES) {
            RelativeLayout custombar =  this.findViewById(R.id.cs_header);
            android.view.ViewGroup.LayoutParams params = custombar.getLayoutParams();
            params.height = CustomSearchableInfo.getBarHeight().intValue();
            custombar.setLayoutParams(params);
        }
    }

    // Set X as the icon for the right icon in the app bar
    private void setClearTextIcon() {
        micIcon.setSelected(Boolean.TRUE);
        micIcon.setImageResource(R.drawable.delete_icon);
        micIcon.invalidate();
    }

    // Set the micrphone icon as the right icon in the app bar
    private void setMicIcon() {
        micIcon.setSelected(Boolean.FALSE);
        micIcon.setImageResource(R.drawable.ic_mic_blue);
        micIcon.invalidate();
    }


    //Prepare the payload for API
    public void setUrlPayload(int action, int extra) {
        switch (action) {

            case Constants.GETLIST:
                JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("merchant_location_id", locationid);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiWebrequest(Constants.CategoryAPI, j_obj, Constants.GETLIST, Constants.VIEW, 0);
                break;

            case Constants.GETLIST_1:
                j_obj = new JSONObject();
                try {
                    j_obj.put("category_id", cate_items.get(extra).id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiWebrequest(Constants.CategoryAPI, j_obj, Constants.GETLIST_1, Constants.VIEW_SUB, extra);
                break;
            case Constants.GETLIST_2:
                j_obj = new JSONObject();
                try {
                    j_obj.put("category_id", cate_items.get(extra).id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiWebrequest(Constants.CategoryAPI, j_obj, Constants.GETLIST_2, Constants.VIEW_SUB, extra);
                break;

            case Constants.GETLIST_3:
                j_obj = new JSONObject();
                try {
                    j_obj.put("merchant_location_id", locationid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiWebrequest(Constants.ProductAPI, j_obj, Constants.GETLIST_3, Constants.VIEW_All, 0);
                break;
        }

    }

    //Handle Retrived data from API
    public void setReturnedData(int category, JSONObject j_obj, int posit) {
        switch (category) {
            case Constants.GETLIST:
                Log.e("Apitransect", "" + j_obj);
                JSONArray re_j_obj;
                try {
                    returnedData = j_obj;
                    re_j_obj = returnedData.getJSONArray("categories");
                    cate_items.clear();
                    for (int i = 0; i < re_j_obj.length(); i++) {
                        cate_items.add(new Categories(re_j_obj).categories.get(i));
                    }
                    cat_adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cd.hide();
                break;

            case Constants.GETLIST_1:
                Log.e("Apitransect", "" + j_obj);
                cd.hide();
                try {
                    returnedData = j_obj;
                    re_j_obj = returnedData.getJSONArray("sub_categories");
                    ArrayList<SubCategories.SubCategory> sub_categories = (new SubCategories(re_j_obj)).sub_categories;
                    cate_items.get(posit).subCategories.clear();
                    for (int i = 0; i < sub_categories.size(); i++) {
                        cate_items.get(posit).subCategories.add(sub_categories.get(i));
                    }
                    cat_adapter.notifyDataSetChanged();
                    try {
                        DataKeeper.search_product_array.clear();
                        re_j_obj = returnedData.getJSONArray("products");
                        ArrayList<Products.Product> json = (new Products(re_j_obj)).products;
                        Collections.reverse(json);
                        for (int i = 0; i < json.size(); i++) {
                            DataKeeper.search_product_array.add(json.get(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    fillProducts("", true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.GETLIST_2:
                Log.e("Apitransect", "" + j_obj);
                cd.hide();
                try {
                    returnedData = j_obj;
                    re_j_obj = returnedData.getJSONArray("sub_categories");
                    ArrayList<SubCategories.SubCategory> sub_categories = (new SubCategories(re_j_obj)).sub_categories;
                    cate_items.get(posit).subCategories = sub_categories;
                    cat_adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.GETLIST_3:
                Log.e("Apitransect", "" + j_obj);
                cd.hide();
                try {
                    returnedData = j_obj;
                    re_j_obj = returnedData.getJSONArray("products");
                    DataKeeper.search_product_array.clear();
                    ArrayList<Products.Product> data = (new Products(re_j_obj)).products;
                    Collections.reverse(data);

                    DataKeeper.all_products_array.clear();
                    for (int i = 0; i < data.size(); i++) {
                        DataKeeper.all_products_array.add((Products.Product) data.get(i).clone());
                        DataKeeper.search_product_array.add((Products.Product) data.get(i).clone());
                    }
                    fillProducts("", true);
                    fillCategories(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                break;


        }
    }

    //Api function to fetch the data
    public void apiWebrequest(String api_url, final JSONObject j_obj, final int action, final String action_text, final int posit) {
        if (util.checkInternetConnection()) {
            cd.show("");
            String url = api_url;

            Log.e("url ", "" + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        public JSONObject data;

                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", "" + response);
                            try {
                                if ((new JSONObject(response)).getString("status").equalsIgnoreCase("1")) {

                                    JSONObject returnJSON = new JSONObject();
                                    if (!(new JSONObject(response)).isNull("data"))
                                        returnJSON = (new JSONObject(response)).getJSONObject("data");
                                    else
                                        returnJSON = (new JSONObject(response));

                                    if (!(new JSONObject(response)).isNull("message"))
                                        util.customToast((new JSONObject(response)).getString("message"));

                                    setReturnedData(action, returnJSON, posit);

                                } else {
                                    cd.hide();
                                    util.customToast("Failed");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("data", j_obj.toString());
                    params.put("action", action_text);
                    Log.e("getParams", String.valueOf(params));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
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
            RequestQueue requestQueue = Volley.newRequestQueue(SearchActivity.this);
            requestQueue.add(stringRequest);
        } else {
            util.customToast(getResources().getString(R.string.nointernet));

        }
    }

    //Seat up the taglayout for selected and added products
    private void setupSearchTaglayout() {
        searchTagGroup = (TagView) findViewById(R.id.tag_group);
        ly_searchtag = (LinearLayout) findViewById(R.id.ly_searchtag);
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
                DataKeeper.product_name_selected.remove(i);
                DataKeeper.product_id_selected.remove(i);
                DataKeeper.products_array.remove(i);

            }
        });
        //set long click listener
        searchTagGroup.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(Tag tag, int i) {
            }
        });

        for (int i = 0; i < DataKeeper.product_name_selected.size(); i++) {
            Tag s_tag = new Tag(DataKeeper.product_name_selected.get(i));
            s_tag.isDeletable = true;
            s_tag.layoutColor = R.color.colorPrimaryDark;
            searchTagGroup.addTag(s_tag);
        }
        ly_searchtag.setVisibility(View.VISIBLE);

    }

    //Add tag to taglayout and add product to cart
    public static void addTag(Products.Product product) {
        DataKeeper.product_id_selected.add(0, product.id);
        DataKeeper.product_name_selected.add(0, product.title);
        DataKeeper.products_array.add(0, product);
        List<Tag> tagiis = new ArrayList<>();
        searchTagGroup.removeAll();
        for (int i = 0; i < DataKeeper.product_name_selected.size(); i++) {
            Tag s_tag = new Tag(DataKeeper.product_name_selected.get(i));
            s_tag.isDeletable = true;
            s_tag.layoutColor = R.color.colorPrimaryDark;
            tagiis.add(s_tag);
        }
        searchTagGroup.addTags(tagiis);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    public void fillProducts(String id, boolean isSaved) {

        tr_fragment.fillData(id, isSaved);

    }


    //Handle the click event of dialog
    public void myClickMethod(final View v) {

        switch (v.getId()) {
            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                if (isdeleteProduct) {
                    setUrlPayload(Constants.DELETEPRODITEM, 0);
                    isdeleteProduct = false;
                } else {
                    setUrlPayload(Constants.DELETEITEM, 0);
                }
                if (cat_adapter != null)
                    cat_adapter.cd.hide();

                cd.hide();
                break;

            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();
                if (cat_adapter != null)
                    cat_adapter.cd.hide();
                break;
        }
    }
}

