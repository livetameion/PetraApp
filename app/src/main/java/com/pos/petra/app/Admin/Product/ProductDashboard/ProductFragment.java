package com.pos.petra.app.Admin.Product.ProductDashboard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.pos.petra.app.AdvancedCashier.CashierDashboardActivity;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.Model.Product.Categories;
import com.pos.petra.app.Model.Product.Products;
import com.pos.petra.app.Model.Product.SubCategories;
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
import java.util.Map;

public class ProductFragment extends Fragment {
    public static CustomDialog cd;
    public static String categ_id="";
    public ArrayList<Bitmap> bitmapArray;
    private EditText product_name, p_price, p_description, p_stock;
    private ImageView p_image;
    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    private File lastCaptured;
    private Uri imageUri;
    public RecyclerView listview;
    int REQUEST_CAMERA=3;
    public TextView tv_no_record;
    private SearchProductAdapter adapter;
    private SharedPreferences common_mypref;
    private JSONObject returnedData;
    private Utils util;
    private TextView title,texttitle_product;
    private Context context;
    public static int position;
    private JSONArray returnedImageData;
    public ArrayList<String[]> imageData;
    Uri myuri;
    private Uri imageToUploadUri;
    String imageid=null;
    static String productid=null;
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

    private TagView searchTagGroup;
    private LinearLayout ly_searchtag;
    private TextView tv_no_record_;
    public ArrayList<Integer> select_array= new ArrayList<>();
    private FloatingActionButton fab_cart;
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_SEARCH= "item_search";
    public static final String LOC_ID= "loc_id";
    public static final String ARG_TYPE = "type";


    private View rootView;
    private String sub_cate_id;
    public ProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            sub_cate_id= getArguments().getString(ARG_ITEM_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.c_activity_product, container, false);

            // fillBitmapArrayList();
            //fillDataArraylist();
            context = getActivity();
            common_mypref = getActivity().getSharedPreferences(
                    "user", 0);
            util = new Utils(context);
            tv_no_record = (TextView) rootView.findViewById(R.id.tv_no_record_);
            cd = new CustomDialog(context);
            // txt_producttitle = (TextView) rootView.findViewById(R.id.txt_branchtitle);
            //txt_producttitle.setText("Products");
            tv_no_record = (TextView) rootView.findViewById(R.id.tv_no_record_);


            try {
                JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
                admin_id = jobj.getJSONObject("admin").getString("admin_id");
                merchant_location_id = jobj.getJSONObject("admin").getString("merchant_location_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bitmapArray = new ArrayList<>();
            imageData = new ArrayList<>();
            listview = (RecyclerView) rootView.findViewById(R.id.listview);
            storiesLayoutManager = createLayoutManager(getResources());
            listview.setLayoutManager(storiesLayoutManager);
            listview.setAdapter(adapter = new SearchProductAdapter(this,DataKeeper.search_product_array));

            fab_cart = (FloatingActionButton) rootView.findViewById(R.id.fab_cart);
            fab_cart.setVisibility(View.GONE);
            fab_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), CashierDashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(i);
                }
            });
            //fillData(1);
            sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        }
        return rootView;

    }
    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = resources.getInteger(R.integer.product_columns);
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    public void fillData(String i, boolean type_saved){
        Log.e("called by",""+i);
        if(type_saved){
            //setReturnedData(Constants.GETLIST, );

            adapter.notifyDataSetChanged();
            if(DataKeeper.search_product_array.size()!=0){

                listview.setVisibility(View.VISIBLE);
                tv_no_record.setVisibility(View.GONE);
            }
            else {
                listview.setVisibility(View.GONE);
                tv_no_record.setVisibility(View.VISIBLE);
            }

        }
        else if(i!=null)
        {
            setUrlPayload(Constants.GETLIST,i);
            listview.setVisibility(View.VISIBLE);
            tv_no_record.setVisibility(View.GONE);
        }
        else {

            tv_no_record.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                break;
        }

        return super.onOptionsItemSelected(item);
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
                try {
                    returnedData =j_obj;
                    JSONArray re_j_obj = returnedData.getJSONArray("products");
                    DataKeeper.search_product_array.clear();
                    ArrayList<Products.Product> json = (new Products(re_j_obj)).products;
                    for(int i =0 ;i<json.size();i++){
                        DataKeeper.search_product_array.add(json.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    if(DataKeeper.search_product_array.size()!=0){

                        listview.setVisibility(View.VISIBLE);
                        tv_no_record.setVisibility(View.GONE);
                    }
                    else {

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
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
        else
        {
            util.customToast(getResources().getString(R.string.nointernet));

        }
    }


    public void callFreeSearch(final String q, final String locationid) {


        if (util.checkInternetConnection()) {

            cd.show("");
            Log.e("search", Constants.searchUrl);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.searchUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("search", ""+response);
                            try {

                                JSONObject deviceData=new JSONObject(response);
                                JSONArray products = deviceData.getJSONArray("products");
                                setReturnedData(Constants.GETLIST,deviceData);

                                DataKeeper.search_product_array.clear();
                                ArrayList<Products.Product> json = (new Products(products)).products;
                                for(int i =0 ;i<json.size();i++){
                                    DataKeeper.search_product_array.add(json.get(i));
                                }
                                adapter.notifyDataSetChanged();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("q",""+q.trim());
                    params.put("merchant_location_id", locationid);
                    params.put("action","search");

                    Log.e("getParams", String.valueOf(params));
                    return params;
                }
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> params = new HashMap< >();
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
                public void retry(VolleyError error)  {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(context,getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }


    }


    public void showimage(int p){
        final Dialog Dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.banner_full_image, null);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        rowView.setMinimumWidth((int)(displayRectangle.width() * 1f));
        rowView.setMinimumHeight((int)(displayRectangle.height() * 0.5f));
        TouchImageView imgview = (TouchImageView) rowView.findViewById(R.id.image_detail);
        if(DataKeeper.search_product_array.get(position).image_full_path.trim().length()!=0)
        {
            Picasso.get()
                    .load(DataKeeper.search_product_array.get(p).image_full_path.replace(" ","%20"))
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








    public void myClickMethod(final View v) {

        switch (v.getId()) {
            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                setUrlPayload(Constants.DELETEITEM,""+productid);
                cd.hide();
                break;

            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();

                break;
        }
    }


}
